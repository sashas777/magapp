package com.magapp.main;

import java.net.URI;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.magapp.connect.RequestArrayInterface;
import com.magapp.connect.RequestArrayTask;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NewOrderService extends Service  implements RequestArrayInterface{

	NotificationManager nm;
	final String LOG_TAG = "Sashas";

	Timer timer;
	TimerTask tTask;
	long interval = 5000;

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
		if (tTask != null)
			tTask.cancel();
		if (interval > 0) {
			tTask = new TimerTask() {
				public void run() {
					CheckOrders();
				}
			};
			timer.schedule(tTask, 5000, interval);
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

		HashMap map_filter = new HashMap();

		RequestArrayTask task;	 
		Vector params = new Vector();		
		params.add(map_filter);
		task = new RequestArrayTask(this, this,"magapp_sales_order.notifications");
		task.execute(params);
		// stopSelf();
	}
	
	
	public void onPreExecute(){				
	};  

	 @Override
	 public void doPostExecute(Object[] result) {			
		 PrepareOrdersData(result);
	 }		

	 
	 public void RequestFailed(String error) {
	 }		

	public void PrepareOrdersData(Object[] new_info) {
		for (Object o : new_info) {

			HashMap map = (HashMap) o;
			ShowNotification(map);
			// Log.e("Sashas",map.get("order_id").toString());
		}
	}

	void ShowNotification(HashMap order) {

		String increment_id = order.get("increment_id").toString();
		String order_status = order.get("status").toString();
		String order_amount = order.get("amount").toString();
		Integer order_id = Integer.parseInt(order.get("order_id").toString());
		// 1nd part
		Notification notif = new Notification(R.drawable.ic_launcher, "New Order #"+increment_id, System.currentTimeMillis());

		// set activity
		Intent OrderInfo = new Intent(this, OrderInfoActivity.class);
		OrderInfo.putExtra("order_increment_id", increment_id);

		PendingIntent pIntent = PendingIntent.getActivity(this, 0, OrderInfo, PendingIntent.FLAG_UPDATE_CURRENT);

		// 2nd part
		notif.setLatestEventInfo(this, "Order #" + increment_id, "Status: " + order_status + " | Amount: " + order_amount, pIntent);

		// set flag
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		notif.defaults |= Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
		// send
		nm.notify(order_id, notif);
	}

}
