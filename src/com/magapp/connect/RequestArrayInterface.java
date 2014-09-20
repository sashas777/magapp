package com.magapp.connect;

public interface RequestArrayInterface { 
	
  void doPostExecute(Object[] result);
  void onPreExecute();
  String GetApiRoute(); 
  void RequestFailed(String error);	
  
}
