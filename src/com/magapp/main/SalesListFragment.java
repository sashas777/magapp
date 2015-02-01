package com.magapp.main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.*;
import org.xmlrpc.android.XMLRPCClient;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.magapp.connect.RequestArrayInterface;
import com.magapp.connect.RequestArrayTask;

public class SalesListFragment extends Fragment implements OnClickListener,RequestArrayInterface {


	public TableLayout prodlist;
	public View rootView;
	private int year;
	private int month;
	private int day;
	private SalesListFragment current_class;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.sales, null);

		EditText DateInput = (EditText) rootView.findViewById(R.id.editText1);
		DateInput.setText("Select Date");
		AddTitles();
			
		ImageButton imageButton1 = (ImageButton) rootView.findViewById(R.id.imageButton1);
		imageButton1.setOnClickListener(this);
		 
		RequestArrayTask task;	 
		Vector params = new Vector();		 	 			  		
		task = new RequestArrayTask(this, getActivity());
		task.execute(params);				
		current_class=this;
		
		return rootView;
	}



	@Override
	public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

	}
	
	public void onPreExecute(){		
		ProgressBar progressBar =(ProgressBar) rootView.findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.VISIBLE);
	};  

	 @Override
	 public void doPostExecute(Object[] result) {		
		ProgressBar progressBar =(ProgressBar) rootView.findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE); 	
		SortRows(result);
	 }		
	
	 public  String GetApiRoute() {
		 return "magapp_order.last";
	 }
	 
	 public void RequestFailed(String error) {
		((BaseActivity)getActivity()).ShowMessage(error);
		ProgressBar progressBar =(ProgressBar) rootView.findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE); 			
		Intent Login = new Intent(getActivity(), LoginActivity.class);
		this.startActivity(Login);
		getActivity().finish();		 
	 }		
	
	public void AddTitles() {
		/* Add Field titles */
		prodlist = (TableLayout) rootView.findViewById(R.id.tbl_orders);
		prodlist.setStretchAllColumns(true);
		prodlist.setColumnShrinkable(1, true);

		TableRow row = new TableRow(getActivity());
		row.setBackgroundColor(Color.parseColor("#EEEEEE"));

		TextView order_id = new TextView(getActivity());
		order_id.setText("Order #");
		order_id.setPadding(10, 10, 10, 10);
		order_id.setGravity(Gravity.CENTER_HORIZONTAL);

		TextView order_name = new TextView(getActivity());
		order_name.setText("Name");
		order_name.setPadding(10, 10, 10, 10);
		order_name.setGravity(Gravity.CENTER_HORIZONTAL);

		TextView order_date = new TextView(getActivity());
		order_date.setText("Date");
		order_date.setPadding(10, 10, 10, 10);
		order_date.setGravity(Gravity.CENTER_HORIZONTAL);

		row.addView(order_id, new TableRow.LayoutParams());
		row.addView(order_name, new TableRow.LayoutParams());
		row.addView(order_date, new TableRow.LayoutParams());

		prodlist.addView(row, new TableLayout.LayoutParams());
		/* Add Field titles */
	}

	public void Addrow(HashMap order) {
		prodlist = (TableLayout) rootView.findViewById(R.id.tbl_orders);

		TableRow row = new TableRow(getActivity());
		row.setBackgroundColor(Color.parseColor("#FFFFFF"));

		TextView hLabel = new TextView(getActivity());
		hLabel.setText(order.get("increment_id").toString());
		hLabel.setPadding(10, 10, 10, 10);
		hLabel.setGravity(Gravity.CENTER_HORIZONTAL);
		hLabel.setBackgroundResource(R.layout.line);
		// hNextLabel.setGravity(Gravity.CENTER_HORIZONTAL) ;
		// row.setBackgroundResource(R.layout.line);

		TextView hLabel2 = new TextView(getActivity());
		hLabel2.setText(order.get("billing_name").toString());
		hLabel2.setPadding(10, 10, 10, 10);
		hLabel2.setGravity(Gravity.CENTER_HORIZONTAL);
		hLabel2.setBackgroundResource(R.layout.line);

		DateFormat created_at = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date created_at_date = new Date();
		try {
			created_at_date = created_at.parse(order.get("created_at").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView hLabel3 = new TextView(getActivity());
		hLabel3.setText(new SimpleDateFormat("MM-dd-yyy") .format(created_at_date));
		hLabel3.setPadding(10, 10, 10, 10);
		hLabel3.setGravity(Gravity.CENTER_HORIZONTAL);
		hLabel3.setBackgroundResource(R.layout.line);

		row.setId(Integer.parseInt(order.get("order_id").toString()));

		row.addView(hLabel, new TableRow.LayoutParams());
		row.addView(hLabel2, new TableRow.LayoutParams());
		row.addView(hLabel3, new TableRow.LayoutParams());
		row.setClickable(true);
		row.setOnClickListener(tablerowOnClickListener);

		prodlist.addView(row, new TableLayout.LayoutParams());

	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		prodlist = (TableLayout) rootView.findViewById(R.id.tbl_orders);
		Integer rows = prodlist.getChildCount();
		for (int j = 1; j < rows; j++) {
			TableRow t = (TableRow) prodlist.getChildAt(j);
			Integer a = t.getChildCount();
			for (Integer i = 0; i < a; i++) {
				TextView firstTextView = (TextView) t.getChildAt(i);
				firstTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
				firstTextView.setBackgroundResource(R.layout.line);
			}
		}
		return;
	}

	private OnClickListener tablerowOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			TableRow t = (TableRow) v;

			Integer a = t.getChildCount();
			for (Integer i = 0; i < a; i++) {
				TextView firstTextView = (TextView) t.getChildAt(i);
				firstTextView.setBackgroundColor(Color.parseColor("#DDDDDD"));
			}
			// TextView IncrementIdCell = (TextView) t.getChildAt(0);
			// String order_id = (String) IncrementIdCell.getText();

			ShowOrderInfo(t.getId());
		}
	};

	public void ShowOrderInfo(int OrderId) {

		Intent OrderInfo = new Intent(getActivity(), OrderInfoActivity.class);
	 	OrderInfo.putExtra("order_id", OrderId);
		getActivity().startActivity(OrderInfo);
		// getActivity().finish();
	}

	public void SortRows(Object[] orders) {
		prodlist.removeAllViews();
		AddTitles();
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
			// Log.e("Sashas",order.get("order_id").toString());
			Addrow(order);
		}
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageButton1:
			selectDate(v);
			break;
		/*
		 * case R.id.btnRemove: fTrans.remove(frag1); break;
		 */
		default:
			Toast.makeText(this.getActivity(), "Something clicked?",
					Toast.LENGTH_LONG).show();
			break;
		}

	}

	public void selectDate(View view) {
		Calendar mcurrentTime = Calendar.getInstance();
		EditText DateInput = (EditText) rootView.findViewById(R.id.editText1);
		if (year == 0) {
			year = mcurrentTime.get(Calendar.YEAR);
			month = mcurrentTime.get(Calendar.MONTH);
			day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
		}
		DatePickerDialog mTimePicker;

		mTimePicker = new DatePickerDialog(getActivity(),
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						EditText DateInput = (EditText) rootView.findViewById(R.id.editText1);
						DateInput.setText(String.format("%02d", monthOfYear + 1)+ "/"+ String.format("%02d", dayOfMonth)+ "/"+ String.valueOf(year));

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
						
						RequestArrayTask task;	 						 	 	 			  	
						task = new RequestArrayTask(current_class, getActivity());
						task.execute(params); 					
																
						month = monthOfYear;
						day = dayOfMonth;
					}
				}, year, month, day);
		mTimePicker.setTitle("Select Date");
		mTimePicker.show();
	}

}
