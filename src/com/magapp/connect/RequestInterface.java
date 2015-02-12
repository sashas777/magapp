package com.magapp.connect;

public interface RequestInterface { 
	
  void doPostExecute(Object result);
  void onPreExecute();
  void RequestFailed(String error);	
  
}
