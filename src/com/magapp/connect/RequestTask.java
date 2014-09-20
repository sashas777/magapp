package com.magapp.connect;

import java.net.URI;
import java.util.HashMap;
import java.util.Vector;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.magapp.connect.LoginTask.FinishLogin;

import android.os.AsyncTask;
import android.util.Log;

public class RequestTask extends  AsyncTask<Vector, Void, Object>  {

	
	
	RequestInterface RequestCallBack;	
	
	public RequestTask(RequestInterface callback){
		RequestCallBack = callback; 
		
	}		
	
	protected void onPreExecute() {
		super.onPreExecute();
		RequestCallBack.onPreExecute();		
	};		
	
	protected Object doInBackground(Vector... params) {

		//Vector params = new Vector();
		//HashMap req_type = new HashMap();
		//req_type.put("type", request_type[0]);
		//if (request_type[0].equals("orders")) {
			//progressBar.setVisibility(View.VISIBLE);
		//} else {
			//progressBar2.setVisibility(View.VISIBLE);
	//	}

	//	params.add(req_type);
		
		Object charts_info;
		
		URI uri = URI.create(MagAuth.getApiUrl());
		XMLRPCClient client = new XMLRPCClient(uri);		
		
		try {
			charts_info = (Object) client.callEx("call", new Object[] { MagAuth.getSession(), RequestCallBack.GetApiRoute(), params[0] });
			return charts_info;
		} catch (XMLRPCException e) {
			Log.e("Sashas", e.getMessage());
			return e;
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		RequestCallBack.doPostExecute(result);	
		if (result instanceof XMLRPCException) {
			//ShowMessage(result.toString());
			//HandleError(result.toString());
		} else {
			//SetChartData(result);
		}
	}	
	
}
