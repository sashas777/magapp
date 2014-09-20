package com.magapp.connect;

import java.net.URI;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		URI uri = URI.create(MagAuth.getApiUrl());
		XMLRPCClient client = new XMLRPCClient(uri);		
		
		try {
			result_info = (Object) client.callEx("call", new Object[] { MagAuth.getSession(), RequestCallBack.GetApiRoute(), params[0] });
			return result_info;
		} catch (XMLRPCException e) {
			Log.e("Sashas", e.getMessage());
			return new Object[] {e};
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
		MagAuth.setSession(null);
		MagAuth auth=new MagAuth(this,  activity);			 
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
	
}
