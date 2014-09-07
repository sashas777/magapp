package com.magapp.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadReceiv extends BroadcastReceiver {

	final String LOG_TAG = "Sashas";

	public void onReceive(Context context, Intent intent) {
		Log.d(LOG_TAG, "onReceive " + intent.getAction());
		context.startService(new Intent(context, NewOrderService.class));
	}
}
