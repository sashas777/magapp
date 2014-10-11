package com.magapp.connect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlrpc.android.XMLRPCException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.magapp.connect.LoginTask.FinishLogin;
import com.magapp.main.NewOrderService;


public class MagAuth implements FinishLogin{
 	
	private  String   accountType = "com.magapp.main";
    private  static String   static_accountType = "com.magapp.main";
	private  String desired_preferense_file = "magapp";
    private  static String static_desired_preferense_file = "magapp";
	private  static String url;
	private  static String api_session;
	private  String api_username = null;
	private  String api_password = null;
    private  static String static_api_username = null;
    private  static String static_api_password = null;
    private Context activity;
	private LoginTask task;
	 
	GetSession GetSessionCallBack;
	
	
	public MagAuth(GetSession callback, Context act ) {
		GetSessionCallBack = callback;
		activity=act;
		
		if (getSession()!=null && isOnline()) 
			GetSessionCallBack.SessionReturned(getSession(), true);
		else {
            setSession(null);
            login();
        }
	}

    public MagAuth(Context act ) {
        activity=act;
        getCredentials();
    }


    public void getCredentials(){
        SharedPreferences settings = activity.getSharedPreferences(desired_preferense_file, 0);
        String selected_account_name = settings.getString("selected_account_name", null);
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
        if (url!=null) {
            url = url.concat("/index.php/api/xmlrpc/");
        }
    }
	
	public void login() {

        getCredentials();
		
		if (api_username!=null && api_password!=null && url!=null && isOnline()) {
			task = new LoginTask(this,  api_username, api_password,url);
			task.execute(); 
		}else if (!isOnline() && !(activity instanceof NewOrderService)) {
			Log.e("Sashas","No Internet Connection.");			
			makeToast("Oops. No network connection.");
		}else if ((api_password==null ||url==null) &&  !(activity instanceof NewOrderService) ){
			makeToast("Please magento account settings");
		}else if (!(activity instanceof NewOrderService)){		
			makeToast("Please select default magento account");
			Log.e("Sashas","Default Account Not Choosed.");
		}
 
	}	
	
	public void onFinishLoginPreExecute(){			 
		  GetSessionCallBack.ShowProgressBar();		 	            
	};  

	 @Override
	 public void doFinishLoginPostExecute(Object session) {
	 if (session instanceof XMLRPCException) {		 	 
		 	XMLRPCException exp=(XMLRPCException) session;
		 	String res =HandleError(exp);
		 	Log.e("Sashas",res);
			GetSessionCallBack.SessionReturned(res, false);
	 	} else if (session instanceof Exception){
	 		GetSessionCallBack.SessionReturned(((Exception) session).getMessage().toString(), false);
		} else if (session instanceof  String) { 
			api_session=session.toString();
			GetSessionCallBack.SessionReturned(session.toString(), true);			 
		}
 
	 }	
		 
	public static String getApiUrl(Context act){
        if (url!=null && url.length()<1) {
            new MagAuth(act).getCredentials();
        }

		return url;
	}
	
	public static void setSession(String ses){
		api_session=ses;		 
	}	
	
	public static String getSession(){
		return api_session;
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
	
	public String HandleError(XMLRPCException error_obj) {
		
		String error = error_obj.toString(); 		 
		
		String regex_script = "\\[code (.*?)\\]";
		Pattern p = Pattern.compile(regex_script);
		 
		Matcher m = p.matcher(error);
		String error_code = "0";
		if (m.find()) {
			error_code = m.group(1).toString();
		}
		 
		if (error_code.equals("5")) {
			error="Session expired. Try to relogin.";
		} else if (error_code.equals("2")) {
			error="Access denied. Please check credentials.";
		}		
		 
		return error;
	}
}
