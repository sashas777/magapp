package com.magapp.connect;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.magapp.connect.LoginTask.FinishLogin;


public class MagAuth implements FinishLogin{
 	
	private  String   accountType = "com.magapp.main";
	private  String desired_preferense_file = "magapp";
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
		String used_session = settings.getString("session", null); // -- modernize
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
		
		
		if (api_username!=null && isOnline()) {
			url = url.concat("/index.php/api/xmlrpc/");
			task = new LoginTask(this,  api_username, api_password,url);
			task.execute(); 
		}else if (!isOnline()) {
			Log.e("Sashas","No Internet Connection.");			
			GetSessionCallBack.NoInternet(this);
		}else{		 
			makeToast("Please select default magento account");
			Log.e("Sashas","Default Account Not Choosed.");
		}
 
	}	
	
	public void onPreExecute(){			 
		  GetSessionCallBack.ShowProgressBar();		 	            
	};  

	 @Override
	 public void doPostExecute(String session) {
	 if (session.contains(" ")) {
			makeToast("Error: Please check your credentials");
			GetSessionCallBack.SessionReturned(session, false);
		} else { 
			GetSessionCallBack.SessionReturned(session, true);
		}
 
	 }	
		 
	public  String getApiUrl(){
		return url;
	}
	
	public void makeToast(String text){		
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        
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
