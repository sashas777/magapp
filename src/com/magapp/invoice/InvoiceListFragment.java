package com.magapp.invoice;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.magapp.connect.RequestArrayInterface;
import com.magapp.connect.RequestArrayTask;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.LoginActivity;
import com.magapp.main.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InvoiceListFragment extends ListFragment implements RequestArrayInterface     {  
	
	public View rootView;
	private Integer order_id;
	private String order_increment_id,api_point, entityName;
	private ArrayList<HashMap<String, String>> InvoiceList;
	private InvoiceListAdapter adapter;
	 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.invoice_list, null);

			InvoiceList = new ArrayList<HashMap<String, String>>(); 

			adapter=new InvoiceListAdapter(getActivity(), InvoiceList);			
			setListAdapter(adapter);

            Bundle params = new Bundle();
            params=getArguments();

		    order_id=params.getInt("order_id");
            api_point=params.getString("api_point");
            entityName=params.getString("entity_name");
			RequestArrayTask task;

			Vector task_params = new Vector();
			HashMap map_filter = new HashMap(); 
			map_filter.put("order_id ", order_id);
            task_params.add(map_filter);
			task = new RequestArrayTask(this, getActivity(),api_point);
			task.execute(task_params);

		 	setHasOptionsMenu(true);
			return rootView;
	}
	 
		public void onPreExecute(){
            ((ActivityLoadInterface)getActivity()).showProgressBar();
		};  

		 @Override
		 public void doPostExecute(Object[] result) {
            ((ActivityLoadInterface)getActivity()).hideProgressBar();
			AddIvoices(result);
		 }			
 	
		 public void AddIvoices(Object[]  invoices){

			for (Object o : invoices) {
				HashMap map = (HashMap) o;
				
				String increment_id=map.get("increment_id").toString();
                String created_at=map.get("created_at").toString();

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
				/*Total*/
                String TotalAmount ="";
                if (api_point.equals("sales_order_invoice.list")) {
                    String total = map.get("grand_total").toString();
                    NumberFormat currency_format = NumberFormat.getCurrencyInstance(Locale.US);
                    TotalAmount = currency_format.format(Double.valueOf(total).doubleValue());
                }else if (api_point.equals("sales_order_shipment.list")) {
                    Integer qty_val=(int) Double.valueOf(map.get("total_qty").toString()).doubleValue();
                    TotalAmount="Total Qty: "+qty_val.toString();
                }
				/*Total*/
				/*State*/
                String status="";
                if (api_point.equals("sales_order_invoice.list")) {
                    int state = Integer.parseInt(map.get("state").toString());
                    status=status+" ";
                    switch (state) {
                    case 0:
                        status=status+"Not Defined";
                        break;
                    case 1:
                        status=status+"Pending";
                        break;
                    case 2:
                        status=status+"Paid";
                        break;
                    case 3:
                        status=status+"Canceled";
                        break;
                    }
                }
				/*State*/

				String Invoice_Line_1=entityName+" #"+increment_id+" | "+created_at_date_string;
				String Invoice_Line_2=TotalAmount+status;
				
				HashMap<String, String> list_map = new HashMap<String, String>();
				 	   
				list_map.put("invoice_number",Invoice_Line_1);
				list_map.put("description",Invoice_Line_2);
				list_map.put("increment_id",increment_id); 	
				InvoiceList.add(list_map);
			}

             if (invoices.length<1) {
                 HashMap<String, String> list_map = new HashMap<String, String>();
                 list_map.put("invoice_number","There are no items for this order yet.");
                 list_map.put("description","");
                 list_map.put("increment_id","");
                 InvoiceList.add(list_map);
             }

			adapter.notifyDataSetChanged();
		 }
		 
		 public void RequestFailed(String error) {
			((ActivityLoadInterface)getActivity()).ShowMessage(error);
            ((ActivityLoadInterface)getActivity()).hideProgressBar();
            Intent screen = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(screen);
			getActivity().finish();		 
		 }		 
	 
	 
	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {

		  String selected = ((TextView) v.findViewById(R.id.increment_id)).getText().toString();
		  if (!selected.isEmpty()) {
              Intent screen = null;
              if (api_point.equals("sales_order_invoice.list")) {
                  screen = new Intent(getActivity(), InvoiceInfoActivity.class);
              }else if (api_point.equals("sales_order_shipment.list")) {
                /*change it */
                  screen = new Intent(getActivity(), InvoiceInfoActivity.class);
              }
              screen.putExtra("order_id", order_id);
              screen.putExtra("increment_id", selected);
			  getActivity().startActivity(screen);
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

