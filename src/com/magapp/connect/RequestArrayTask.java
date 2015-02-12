package com.magapp.connect;

import java.net.URI;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.magapp.connect.LoginTask.FinishLogin;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RequestArrayTask extends  AsyncTask<Vector, Void, Object[]> implements GetSession {

	
	
	RequestArrayInterface RequestCallBack;	
	private Vector[] stored_params;
	private Context activity;
	private  String desired_preferense_file = "magapp";
	private SharedPreferences settings;
    private String api_route;

	public RequestArrayTask(RequestArrayInterface callback, Context act,String api_point){
		RequestCallBack = callback; 
		activity=act;
        api_route=api_point;
		settings = activity.getSharedPreferences(desired_preferense_file, 0);
	}		
	
	protected void onPreExecute() {
		super.onPreExecute();
		RequestCallBack.onPreExecute();		
	};		
	
	protected Object[] doInBackground(Vector... params) {
	
		Object[] result_info;
		stored_params=params;
		String store_url = settings.getString("store_url", null);
		URI uri = URI.create(store_url);
		XMLRPCClient client = new XMLRPCClient(uri);		
		
		try {
            if (!isOnline())
                throw new XMLRPCException("No internet connection");


			String session_id = settings.getString("session_id", null);

			result_info = (Object[]) client.callEx("call", new Object[] { session_id, api_route, params[0] });
			return result_info;
		} catch (XMLRPCException e) {
			Log.e("Sashas", e.getMessage());
			return new Object[] {e};
		}
	}

	@Override
	protected void onPostExecute(Object[] result) {
		 
		if ( result.length > 0 && result[0] instanceof XMLRPCException) {						
			HandleError((XMLRPCException) result[0]);
		} else {

			RequestCallBack.doPostExecute(result);	
		}
	}	
	
	public void HandleError(XMLRPCException error_obj) {
	 	/* Set session null */
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("session_id", null);
		editor.commit();
		/* Set session null */
        if (!error_obj.getMessage().toString().equals("No internet connection")) {
            MagAuth auth = new MagAuth(this, activity);
        }else{
            RequestCallBack.RequestFailed(error_obj.getMessage().toString());
        }
	}	
	
	public void SessionReturned(String result, Boolean status){
		 if (status) {
			 doInBackground(stored_params);
		 }else {
			 RequestCallBack.RequestFailed(result);
		 }
	}
	
	public void ShowProgressBar(){
		
	}

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
