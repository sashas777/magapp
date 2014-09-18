package com.magapp.connect;

import java.net.URI;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<Void, Void, String> {

	 interface FinishLogin {		 
		  void doPostExecute(String result);
		  void onPreExecute();
	 }
	
	 FinishLogin FinishLoginCallBack;
	 String api_url;
	 String api_username = null;
	 String api_password = null;	
	 
	 LoginTask(FinishLogin callback, String username,String password,String url){
	  FinishLoginCallBack = callback;
	  api_username=username;
	  api_password=password;
	  api_url=url;
	 }	 
	 
	protected void onPreExecute() {
		super.onPreExecute();
		FinishLoginCallBack.onPreExecute();		
	};

	protected String doInBackground(Void... params) {
		String session="";		 
		URI uri = URI.create(api_url);
		XMLRPCClient client = new XMLRPCClient(uri);
		try {
			session = (String) client.call("login", api_username,api_password);
			return session;
		} catch (XMLRPCException e) {
			Log.e("Sashas", e.getMessage());
			session = e.getMessage().toString();
		} catch (Exception e) {
			Log.e("Sashas", e.getMessage());
			session = e.getMessage().toString();
		}
		return session;
	}

	@Override
	protected void onPostExecute(String result) {
		FinishLoginCallBack.doPostExecute(result);		 
	}
}

