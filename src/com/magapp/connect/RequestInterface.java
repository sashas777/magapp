package com.magapp.connect;

public interface RequestInterface { 
	
  void doPostExecute(Object result);
  void onPreExecute();
  String GetApiRoute(); //"magapp_dashboard.charts"
  void RequestFailed(String error);	
  
}
