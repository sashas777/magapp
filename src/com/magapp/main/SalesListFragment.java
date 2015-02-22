/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.main;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.magapp.common.InvoiceListAdapter;
import com.magapp.connect.RequestArrayInterface;
import com.magapp.connect.RequestArrayTask;
import com.magapp.order.OrderInfoActivity;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class SalesListFragment extends ListFragment implements RequestArrayInterface {


	public View rootView;
	private ArrayList<HashMap<String, String>> OrderList;
	private InvoiceListAdapter adapter;
	private int year;
	private int month;
	private int day;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_sales_list, null);
		setHasOptionsMenu(true);

		OrderList = new ArrayList<HashMap<String, String>>();

		adapter=new InvoiceListAdapter(getActivity(), OrderList);
		setListAdapter(adapter);

		/*  Prepare parameters  for query */
		RequestArrayTask task;	 
		Vector params = new Vector();

		Bundle arguments = getArguments();
		if (arguments != null)
		{
			String date_utc_from = arguments.getString("date_utc_from");
			HashMap day_filter = new HashMap();
			day_filter.put("day", date_utc_from);
			params.add(day_filter);

			DateFormat date_local = new SimpleDateFormat("yyyy-MM-dd");
			Date formatDate = null;
			try {
				formatDate = date_local.parse(date_utc_from);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			DateFormat correctFormat = new SimpleDateFormat("MM/dd/yyyy");
			String formattedDate = correctFormat.format(formatDate);

			String title = "Orders for "+formattedDate;

			((BaseActivity) getActivity()).setActionBarTitle(title);

		}
		/* @comment
			parameters can be day=value or day=array('from'=>date_from,'to'=>date_to) */
		task = new RequestArrayTask(this, getActivity(),"magapp_order.last");
		task.execute(params);				

		return rootView;
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.base, menu);
		MenuItem dateRange_item = menu.findItem(R.id.date_range);
		dateRange_item.setVisible(true);
		getActivity().invalidateOptionsMenu();

		super.onCreateOptionsMenu(menu,inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.date_range) {
			selectDate();
		}
		return super.onOptionsItemSelected(item);
	}


	public void onPreExecute(){		
		ProgressBar progressBar =(ProgressBar) rootView.findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.VISIBLE);
	}

    @Override
	 public void doPostExecute(Object[] result, String result_api_point) {
		ProgressBar progressBar =(ProgressBar) rootView.findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE); 	
		SortRows(result);
	 }		

	 
	 public void RequestFailed(String error) {
		((BaseActivity)getActivity()).ShowMessage(error);
		ProgressBar progressBar =(ProgressBar) rootView.findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE); 			
		Intent Login = new Intent(getActivity(), LoginActivity.class);
		this.startActivity(Login);
		getActivity().finish();		 
	 }		


	public void Addrow(HashMap order) {

		String increment_id=order.get("increment_id").toString();
		String created_at=order.get("created_at").toString();
		String status=order.get("status").toString();
		String grandTotal=order.get("grand_total").toString();
		String billingName=order.get("billing_name").toString();

		/* Status */
		status = WordUtils.capitalize(status);
		billingName = WordUtils.capitalize(billingName);
		/* Status */

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
		NumberFormat currency_format = NumberFormat.getCurrencyInstance(Locale.US);
		String TotalAmount=currency_format.format(Double.valueOf(grandTotal).doubleValue());
		/*Total*/

		/*Add values to the List*/
		String Order_Line_1="Order #"+increment_id+" | "+created_at_date_string;
		String Order_Line_2="By: "+billingName;
		String Order_Line_3="Status: "+status+" | Grand Total: "+TotalAmount;

		HashMap<String, String> list_map = new HashMap<String, String>();

		list_map.put("invoice_number",Order_Line_1);
		list_map.put("description",Order_Line_2);
		list_map.put("increment_id",increment_id);
		list_map.put("line3",Order_Line_3);
		OrderList.add(list_map);

	}

	@Override
	public void onResume() {
		super.onResume();
		return;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String selected = ((TextView) v.findViewById(R.id.increment_id)).getText().toString();
		ShowOrderInfo(selected);
	}

	public boolean ShowOrderInfo(String OrderId) {
		if (OrderId.isEmpty())
			return false;
		Intent OrderInfo = new Intent(getActivity(), OrderInfoActivity.class);
	 	OrderInfo.putExtra("order_increment_id", OrderId);
		getActivity().startActivity(OrderInfo);
		// getActivity().finish();
		return true;
	}

	public void SortRows(Object[] orders) {

		List data = new ArrayList();
		for (Object o : orders) {
			HashMap map = (HashMap) o;
			data.add(map);
		}
		Collections.sort(data, new Comparator<HashMap>() {
			public int compare(HashMap obj1, HashMap obj2) {
				Integer obj1_val = Integer.parseInt(obj1.get("order_id").toString());
				Integer obj2_val = Integer.parseInt(obj2.get("order_id").toString());
				return obj2_val.compareTo(obj1_val);
			}
		});
		for (Object a : data) {
			HashMap order = (HashMap) a;

			Addrow(order);
		}
		if (orders.length<1) {
			HashMap<String, String> list_map = new HashMap<String, String>();
			list_map.put("invoice_number","There are no orders.");
			list_map.put("description","");
			list_map.put("increment_id","");
			OrderList.add(list_map);
		}
		adapter.notifyDataSetChanged();
	}


	public void selectDate() {
		Calendar mcurrentTime = Calendar.getInstance();
		//EditText DateInput = (EditText) rootView.findViewById(R.id.editText1);
		if (year == 0) {
			year = mcurrentTime.get(Calendar.YEAR);
			month = mcurrentTime.get(Calendar.MONTH);
			day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
		}

		DatePickerDialog mTimePicker;

		mTimePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						/*EditText DateInput = (EditText) rootView.findViewById(R.id.editText1);
						DateInput.setText(String.format("%02d", monthOfYear + 1)+ "/"+ String.format("%02d", dayOfMonth)+ "/"+ String.valueOf(year));
*/
						Vector params = new Vector();	
						String selected_date=String.valueOf(year) + "-"+ String.format("%02d", monthOfYear + 1)+"-"+String.format("%02d", dayOfMonth);
						DateFormat date_local = new SimpleDateFormat("yyyy-MM-dd");
						Date from_date = new Date();				 					
						try {
							from_date = date_local.parse(selected_date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
						date_local.setTimeZone(TimeZone.getTimeZone("UTC"));
						String date_utc_from = date_local.format(from_date);
						HashMap day_filter = new HashMap();
						day_filter.put("day", date_utc_from);
						params.add(day_filter);

						/*
						RequestArrayTask task;
						task = new RequestArrayTask(current_class, getActivity());
						task.execute(params);
							*/
						FragmentManager fragmentManager = getFragmentManager();
						Fragment screen = new Fragment();
						screen = new SalesListFragment();

						Bundle bundl = new Bundle();
						bundl.putString("date_utc_from", date_utc_from);
						screen.setArguments(bundl);

						fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack(null).commit();


						month = monthOfYear;
						day = dayOfMonth;
					}
				}, year, month, day);
		mTimePicker.getDatePicker().setMaxDate(new Date().getTime());
		mTimePicker.setTitle("Select Date");
		mTimePicker.show();
	}

}
