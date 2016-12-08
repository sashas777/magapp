/*
 * Copyright (c) 2016.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.connect;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import java.net.URI;
import java.util.Vector;

public class RequestTask extends  AsyncTask<Vector, Void, Object> implements GetSession {

	
	
	RequestInterface RequestCallBack;	
	private Vector[] stored_params;
	private Context activity;
	private  String desired_preferense_file = "magapp";
	private SharedPreferences settings;
    private String api_route;

	public RequestTask(RequestInterface callback, Context act,String api_point){
		RequestCallBack = callback; 
		activity=act;
        api_route=api_point;
		settings = activity.getSharedPreferences(desired_preferense_file, 0);
	}		
	
	protected void onPreExecute() {
		super.onPreExecute();
		RequestCallBack.onPreExecute();		
	}

    protected Object doInBackground(Vector... params) {
	
		Object result_info;
		stored_params=params;
		String store_url = settings.getString("store_url", null);
		URI uri = URI.create(store_url);
		XMLRPCClient client = new XMLRPCClient(uri);		
		
		try {
            if (!isOnline())
                throw new XMLRPCException("No internet connection");

			String session_id = settings.getString("session_id", null);

			result_info = client.callEx("call", new Object[] {session_id, api_route, params[0] });
			return result_info;
		} catch (XMLRPCException e) {
            Log.e("Sashas", "RequestTask::doInBackground::XMLRPCException");
			Log.e("Sashas", e.getMessage());
            cancel(true);
            return e;
		}catch (Exception e) {
            Log.e("Sashas", "RequestTask::doInBackground::Exception");
            Log.e("Sashas", e.getMessage());
            cancel(true);
            return e;
        }
	}

    @Override
    protected void onCancelled(Object result) {
        Log.e("Sashas", "RequestTask::onCancelled");
        if (result instanceof XMLRPCException) {
            HandleError((XMLRPCException) result);
        }
    }

    @Override
	protected void onPostExecute(Object result) {
        Log.e("Sashas", "RequestTask::onPostExecute");
		if (result instanceof XMLRPCException) {			 
			HandleError((XMLRPCException) result);
		}  else if (result instanceof Exception)  {
            Log.e("Sashas", "RequestTask::onPostExecute::Exception");
            HandleError((Exception) result);
        } else {
            Log.e("Sashas", "RequestTask::onPostExecute::doPostExecute");
            RequestCallBack.doPostExecute(result,api_route );
            //MagAuth auth = new MagAuth(this, activity,1);
		}
	}	
	
	public void HandleError(Exception error_obj) {
        Log.e("Sashas", "RequestTask::HandleError");
        /*@todo change behavior*/
        /* Set session null */
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("session_id", null);
        editor.commit();
		/* Set session null */
		if (error_obj.getMessage().toString().indexOf("code")>0) {
            RequestCallBack.RequestFailed(error_obj.getMessage().toString());
        } else if (!error_obj.getMessage().toString().equals("No internet connection")) {
            Log.e("Sashas", "RequestTask::HandleError::MagAuth");
            MagAuth auth = new MagAuth(this, activity,1);
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


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Log.e("Sashas","RequestTask::isOnline");
        Log.e("Sashas",netInfo.toString());
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
	
}
