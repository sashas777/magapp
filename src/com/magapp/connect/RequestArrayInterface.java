package com.magapp.connect;

public interface RequestArrayInterface { 
	
  void doPostExecute(Object[] result);
  void onPreExecute();
  void RequestFailed(String error);	
  
}
