/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.connect;

public interface RequestInterface { 
	
  void doPostExecute(Object result, String api_route);
  void onPreExecute();
  void RequestFailed(String error);
  
}
