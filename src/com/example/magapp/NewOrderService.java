package com.example.magapp;

import java.net.URI;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NewOrderService extends Service {

	NotificationManager nm;
	final String LOG_TAG = "Sashas";
	private String session = null;
	private XMLRPCClient client;
	private URI uri;
	private String url;
	private String api_username = null;
	private String api_password = null;
	private String last_order_id = "0";
	private String accountType = "com.example.magapp";
	private String desired_preferense_file = "magapp";
	Timer timer;
	TimerTask tTask;
	long interval = 360000;
	  
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	}

	public int onStartCommand(Intent intent, int flags, int startId) {

		timer = new Timer();
		schedule();
		return super.onStartCommand(intent, flags, startId);
	}

	 void schedule() {
		    if (tTask != null) tTask.cancel();
		    if (interval > 0) {
		      tTask = new TimerTask() {
		        public void run() {
		        	CheckOrders();
		        }
		      };
		      timer.schedule(tTask,   5000, interval);
		    }
		  }
	 
	public void onDestroy() {
		super.onDestroy();
		tTask.cancel();
	}

	public IBinder onBind(Intent intent) {

		return null;
	}

	void CheckOrders() {

		SharedPreferences settings = this.getSharedPreferences(
				desired_preferense_file, 0);
		String selected_account_name = settings.getString("selected_account_name", null);
		last_order_id=settings.getString("last_order_id", "0");
		String used_session = settings.getString("session", null);
		AccountManager manager = AccountManager.get(this);
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

		if (!api_username.equals(null)) {
			url = url.concat("/index.php/api/xmlrpc/");
			uri = URI.create(url);
			client = new XMLRPCClient(uri);
			
			LoginTask task=new LoginTask();
			task.execute(api_username,api_password);
		}

		 

		//stopSelf();

	}

	public void OrdersRequest(){
		OrdersInfo task=new OrdersInfo();
		task.execute(last_order_id);
	}
	
	public void PrepareOrdersData(Object[] new_info) {
		for (Object o : new_info) {
			
			HashMap map = (HashMap) o;		 
			 
			 if (map.get("last_id")!=null) {
				  SharedPreferences settings = this.getSharedPreferences(desired_preferense_file, 0);
			      SharedPreferences.Editor editor = settings.edit();
			      editor.putString("last_order_id", map.get("last_id").toString());
			      editor.commit();		
			      last_order_id=map.get("last_id").toString();
			     // Log.e("Sashas",map.get("last_id").toString());
			 }else {				 
				 ShowNotification(map);
			 }
			 
			//Log.e("Sashas",map.get("order_id").toString());
		}
	}
 
	
  class LoginTask extends AsyncTask<String, Void, String> {  
		  
		  protected void onPreExecute()
		    {			 			 
			  super.onPreExecute();		              
		    };   
		    		    
		    protected String doInBackground(String... credentials) {	
		     	    	 
		      try {           
		        	 session = (String) client.call("login",credentials[0], credentials[1]);        	
		        	 return session;	
		        } catch (XMLRPCException e) {   
		         	Log.e("Sashas",e.getMessage());	      
		         	session=e.getMessage().toString();	          	    	     	     
		       }  	catch (Exception e) { 
		         	Log.e("Sashas",e.getMessage());	         		     
		         	session=e.getMessage().toString();
		        }	     
		      return session;	
		    }	     
		    @Override
		      protected void onPostExecute(String result) {		    
			    	if (result.contains(" ")) {
			    		ShowMessage(result);		    		 
			    	}else {
			    		//ShowMessage("You are logged in");
			    		OrdersRequest();
			    	}
		    	}	     
		}
  
  class OrdersInfo extends AsyncTask<String, Void, Object[]> {  
	  
	  protected void onPreExecute()
	    {			 			 
		  super.onPreExecute();		              
	    };   
	    		    
	    protected Object[] doInBackground(String... order_id) {	
	     	    	 
			 	 	 
			HashMap map_filter = new HashMap();
			map_filter.put("order_id",last_order_id);

			Object[] orders;
			try {
				orders = (Object[]) client.callEx("call", new Object[] { session, "magapp_sales_order.notifications",  new Object[] {map_filter} });				 
				return orders;	
				
			} catch (XMLRPCException e) {
				Log.e("Sashas", e.getMessage());
				return new Object[] {e};
			}    
	     	
	    }	     
	    @Override
	      protected void onPostExecute(Object[] result) {		    
	    	if (result[0] instanceof XMLRPCException ) {				 			 
				ShowMessage(result.toString());					 
			} else {			 				 
				PrepareOrdersData(result);				 
			}
	    }	     
	}
	
	void ShowNotification(HashMap order) {
		
		String increment_id=order.get("increment_id").toString();
		String order_status=order.get("status").toString();
		String order_amount=order.get("amount").toString();
		Integer order_id=Integer.parseInt(order.get("order_id").toString());
		// 1nd part
		Notification notif = new Notification(R.drawable.ic_launcher,
				"New order was placed", System.currentTimeMillis());

		// set activity
		Intent intent = new Intent(this, LoginActivity.class);
		// intent.putExtra(LoginActivity.FILE_NAME, "somefile");
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// 2nd part
		notif.setLatestEventInfo(this, "Order #"+increment_id, "Status: "+order_status+" | Amount: "+order_amount, pIntent);

		// set flag
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		notif.defaults|=Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
		// send
		nm.notify(order_id, notif);
	}
	
	  public void ShowMessage(String text) {
		  Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	  }
}
