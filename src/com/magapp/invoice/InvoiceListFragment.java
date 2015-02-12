package com.magapp.invoice;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.magapp.connect.RequestArrayInterface;
import com.magapp.connect.RequestArrayTask;
import com.magapp.main.LoginActivity;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;
import com.magapp.order.OrderInfoFragment;

public class InvoiceListFragment extends ListFragment implements RequestArrayInterface     {  
	
	public View rootView;
	private Integer order_id;
	private String order_increment_id;
	private ArrayList<HashMap<String, String>> InvoiceList;
	private InvoiceListAdapter adapter;
	 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.invoice_list, null);

			InvoiceList = new ArrayList<HashMap<String, String>>(); 

			adapter=new InvoiceListAdapter(getActivity(), InvoiceList);			
			setListAdapter(adapter);

		    order_id=((OrderInfoActivity)getActivity()).GetOrderId();
			RequestArrayTask task;	 
			Vector params = new Vector();		 
			HashMap map_filter = new HashMap(); 
			map_filter.put("order_id ", order_id);
			params.add(map_filter);			
			task = new RequestArrayTask(this, getActivity(),"sales_order_invoice.list");
			task.execute(params);

		 	setHasOptionsMenu(true);
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
					e.printStackTrace();
				}				
				String created_at_date_string= new SimpleDateFormat("LLL dd, yyy HH:mm:ss") .format(created_at_date);
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
				String Invoice_Line_1="Invoice #"+increment_id+" | "+created_at_date_string;
				String Invoice_Line_2=TotalAmount+" "+status;
				
				HashMap<String, String> list_map = new HashMap<String, String>();
				 	   
				list_map.put("invoice_number",Invoice_Line_1);
				list_map.put("description",Invoice_Line_2);
				list_map.put("increment_id",increment_id); 	
				InvoiceList.add(list_map);
			}

             if (invoices.length<1) {
                 HashMap<String, String> list_map = new HashMap<String, String>();
                 list_map.put("invoice_number","There are no invoices for this order yet.");
                 list_map.put("description","");
                 list_map.put("increment_id","");
                 InvoiceList.add(list_map);
             }

			adapter.notifyDataSetChanged();
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

		  String selected = ((TextView) v.findViewById(R.id.increment_id)).getText().toString();
		  if (!selected.isEmpty()) {
			  Intent Invoice = new Intent(getActivity(), InvoiceInfoActivity.class);
			  Invoice.putExtra("order_id", order_id);
			  Invoice.putExtra("increment_id", selected);
			  getActivity().startActivity(Invoice);
		  }
         // getActivity().finish();

	  }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	 	switch (item.getItemId()) {
			case android.R.id.home:
				getActivity().getActionBar().setSelectedNavigationItem(0);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}

