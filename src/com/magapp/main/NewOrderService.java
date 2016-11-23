/*
 * Copyright (c) 2016.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import com.magapp.connect.RequestArrayInterface;
import com.magapp.connect.RequestArrayTask;
import com.magapp.order.OrderInfoActivity;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class NewOrderService extends Service  implements RequestArrayInterface{

	NotificationManager nm;
	final String LOG_TAG = "Sashas";
	private static final int NOTIFY_ID = 101;

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
	}

    @Override
	 public void doPostExecute(Object[] result, String result_api_point) {
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

		/*New Way*/
		Intent notificationIntent = new Intent(this, OrderInfoActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this,
				0, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		Resources res = this.getResources();
		Notification.Builder builder = new Notification.Builder(this);

		builder.setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
				.setTicker("New Order #"+increment_id)
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true)
				.setContentTitle("New Order #"+increment_id)
				.setContentText("Status: " + order_status + " | Amount: " + order_amount);

		Notification notification = builder.build();
		notification.defaults |= Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
		NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFY_ID, notification);

	}

}
