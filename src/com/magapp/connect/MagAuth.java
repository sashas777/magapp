package com.magapp.connect;

import java.net.URI;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.magapp.connect.LoginTask.FinishLogin;


public class MagAuth implements FinishLogin{
 
	
	private  String   accountType = "com.magapp.main";
	private  String desired_preferense_file = "magapp";
	//private  XMLRPCClient client;
	private  URI uri;
	//private  String session = null;
	private  String url;
	private  String api_username = null;
	private  String api_password = null;	
	private Context activity;
	private LoginTask task;
	 
	GetSession GetSessionCallBack;
	
	
	public MagAuth(GetSession callback, Context act ) {
		GetSessionCallBack = callback;
		activity=act;
		login();
	}
 
	
	public void login() {
		 
		SharedPreferences settings = activity.getSharedPreferences(desired_preferense_file, 0);
		String selected_account_name = settings.getString("selected_account_name", null);
		String used_session = settings.getString("session", null);
		AccountManager manager = AccountManager.get(activity);
		Account[] accounts = manager.getAccountsByType(accountType);
		/* Login with account specified */
		for (int i = 0; i < accounts.length; i++) {
			Account account = accounts[i];
			if (selected_account_name.equals(account.name)) {
				api_username = manager.getUserData(account, "username");
				api_password = manager.getPassword(account);
				url = manager.getUserData(account, "store_url");
				break;
			}
		}
		
		
		if (api_username!=null) {
			url = url.concat("/index.php/api/xmlrpc/");
			task = new LoginTask(this,  api_username, api_password,url);
			task.execute(); 
		}else{
			//session="failed";
			Log.e("Sashas","failed");
		}
/*
		if (api_username!=null) {
			url = url.concat("/index.php/api/xmlrpc/");
			uri = URI.create(url);
			client = new XMLRPCClient(uri);			 
			task.execute(api_username, api_password);			 
		 	while (session==null) {
			} 
			/*How to get result from AsyncTask*/
			//Log.e("Sashas",task.getStatus().toString());			 
	/*	}else{
			session="failed";
		} */
		
		//return session;
	}	
	
 

	 @Override
	 public void doPostExecute(String result) {
	 if (result.contains(" ")) {
			makeToast(result);
		} else {
			Log.e("Sashas","task done - "+result);
			// makeToast("You are logged in");
			// OrdersRequest();
			GetSessionCallBack.SessionReturned(result);
		}
 
	 }	
	
	/*
	class LoginTask extends AsyncTask<String, Void, String> {

		protected void onPreExecute() {
			super.onPreExecute();
		};

		protected String doInBackground(String... credentials) {
			
 
		    
			try {
				session = (String) client.call("login", credentials[0], credentials[1]);
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
			if (result.contains(" ")) {
				 makeToast(result);
			} else {
				 makeToast("You are logged in11");				
				// OrdersRequest();
			}
			return;
			 
		}
		
	    
	}
	*/
	public  String getApiUrl(){
		return url;
	}
	
	public void makeToast(String text){
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        
    }
}
