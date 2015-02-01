package com.magapp.connect;

import java.net.URI;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.magapp.connect.LoginTask.FinishLogin;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RequestTask extends  AsyncTask<Vector, Void, Object> implements GetSession {

	
	
	RequestInterface RequestCallBack;	
	private Vector[] stored_params;
	private Context activity;
	
	public RequestTask(RequestInterface callback, Context act){
		RequestCallBack = callback; 
		activity=act;
	}		
	
	protected void onPreExecute() {
		super.onPreExecute();
		RequestCallBack.onPreExecute();		
	};		
	
	protected Object doInBackground(Vector... params) {
	
		Object result_info;
		stored_params=params;
		Log.e("Sashas",activity.toString());
		URI uri = URI.create(MagAuth.getApiUrl(activity));
		XMLRPCClient client = new XMLRPCClient(uri);		
		
		try {
            if (!isOnline())
                throw new XMLRPCException("No internet connection");
			result_info = (Object) client.callEx("call", new Object[] { MagAuth.getSession(activity), RequestCallBack.GetApiRoute(), params[0] });
			return result_info;
		} catch (XMLRPCException e) {
			Log.e("Sashas", e.getMessage());
			return e;
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		 
		if (result instanceof XMLRPCException) {			 
			HandleError((XMLRPCException) result);
		} else {
			RequestCallBack.doPostExecute(result);
		}
	}	
	
	public void HandleError(XMLRPCException error_obj) {
		MagAuth.setSession(activity, null);
        if (!error_obj.getMessage().toString().equals("No internet connection")) {
            MagAuth auth = new MagAuth(this, activity);
        }else {
            RequestCallBack.RequestFailed("No internet connection");
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
