package com.magapp.invoice;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.magapp.connect.RequestArrayInterface;
import com.magapp.connect.RequestArrayTask;
import com.magapp.main.LoginActivity;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;

public class InvoiceListFragment extends ListFragment implements RequestArrayInterface     {  
	
	public View rootView;
	 

	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.invoice_list, null);
			
			String[] values = new String[] { "Android", "iPhone", "WindowsMobile","Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X","Linux", "OS/2", "aa","sdd","ccc" };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
			setListAdapter(adapter);
			//getActiviy().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		    int order_id=((OrderInfoActivity)getActivity()).GetOrderId();
			RequestArrayTask task;	 
			Vector params = new Vector();		 
			HashMap map_filter = new HashMap(); 
			map_filter.put("order_id", order_id);		
			params.add(map_filter);			
			task = new RequestArrayTask(this, getActivity());
			task.execute(params);					    
				    
			return rootView;
	}
	 
		public void onPreExecute(){		
			ProgressBar progressBar =(ProgressBar)  getActivity().findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.VISIBLE);
		};  

		 @Override
		 public void doPostExecute(Object[] result) {		
			ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.INVISIBLE); 	 			
			AddIvoices(result);
		 }			
 	
		 public void AddIvoices(Object[]  invoices){
			 
			for (Object o : invoices) {
				HashMap map = (HashMap) o;
				
				String increment_id=map.get("increment_id").toString();
				String created_at=map.get("created_at").toString();
				int state=Integer.parseInt(map.get("state").toString());  				 
				String total=map.get("grand_total").toString();
				
				/*Date*/
				DateFormat created_at_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				created_at_format.setTimeZone(TimeZone.getTimeZone("UTC"));			
				Date created_at_date = new Date();
				try {
					created_at_date =created_at_format.parse(created_at);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				String created_at_date_string= new SimpleDateFormat("MM-dd-yyy HH:mm:ss") .format(created_at_date);
				/*Date*/
				/*State*/
				String status="";
				switch (state) {
				case 0:
					status="Not Defined";	
					break;
				case 1:
					status="Pending";			 
					break;
				case 2:
					status="Paid";	
					break;
				case 3:
					status="Canceled";	
					break;					
				}							
				/*State*/
				
				/*Total*/
				NumberFormat currency_format = NumberFormat.getCurrencyInstance(Locale.US);
				String TotalAmount=currency_format.format(Double.valueOf(total).doubleValue());
				/*Total*/
				String Invoice_LIne_1=increment_id+" | "+created_at_date_string;
				String Invoice_LIne_2=TotalAmount+" "+status;
				
				 
				/*Log.e("Sashas",map.get("increment_id").toString());
				Log.e("Sashas",map.get("created_at").toString());
				Log.e("Sashas",map.get("state").toString());
				Log.e("Sashas",map.get("grand_total").toString());*/
				 
			}			 
			 
		 }

		
		 public  String GetApiRoute() {
			 return "sales_order_invoice.list";
		 }
		 
		 public void RequestFailed(String error) {
			((OrderInfoActivity)getActivity()).ShowMessage(error);
			ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.INVISIBLE); 			
			Intent Login = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(Login);
			getActivity().finish();		 
		 }		 
	 
	 
	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		  Toast.makeText(getActivity(),String.valueOf(getListView().getCheckedItemCount()),Toast.LENGTH_LONG).show();
	  }	 
}

