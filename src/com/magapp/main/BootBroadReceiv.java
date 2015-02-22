/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadReceiv extends BroadcastReceiver {


	public void onReceive(Context context, Intent intent) {
		Log.d("Sashas", "onReceive " + intent.getAction());
		context.startService(new Intent(context, NewOrderService.class));
	}
}
