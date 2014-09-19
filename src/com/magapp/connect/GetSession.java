package com.magapp.connect;

import android.view.View;

public interface GetSession {
	 void SessionReturned(String result, Boolean status);
	 void ShowProgressBar();
	 void NoInternet(MagAuth a);
}
