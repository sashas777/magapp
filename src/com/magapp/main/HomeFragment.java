package com.magapp.main;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.Plot;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;

public class HomeFragment extends Fragment  implements RequestInterface{

	private View rootView;
	private SimpleDateFormat OrdersDateFormat = new SimpleDateFormat("hha");
	private SimpleDateFormat AmountsDateFormat = new SimpleDateFormat("hha");
	private Object[] AmountsData, OrdersData;
	//public String api_session;
	//public String api_url;
	//private XMLRPCClient client;
	//private URI uri;
	//private ProgressBar progressBar;
	//private ProgressBar progressBar2;
	  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.home, null);

	/*	progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);*/
		/* Get credentials */
 
		/*
		api_session=MagAuth.getSession();
		api_url=MagAuth.getApiUrl();
		
		
		uri = URI.create(api_url);
		client = new XMLRPCClient(uri);
		ChartsTask task_orders = new ChartsTask();
		task_orders.execute(new String[] { "orders" });
		ChartsTask task_amounts = new ChartsTask();
		task_amounts.execute(new String[] { "amounts" });
*/
		RequestTask task;
		RequestTask task2;
		Vector params = new Vector();
		HashMap req_type = new HashMap();
		req_type.put("type", "orders");
		params.add(req_type);
		
		task = new RequestTask(this,"orders");
		task.execute(params);
		
		req_type.clear();
		params.clear();
		req_type.put("type", "amounts");
		params.add(req_type);
		
		task2 = new RequestTask(this,"amounts");
		task2.execute(params);		
		
		// AddOrdersPlot(new Number[]{},new Number[]{});
		// AddAmountsPlot(new Number[]{},new Number[]{});
		 
		return rootView;
	}

	public void onPreExecute(String type){		

		ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1); 
		ProgressBar progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
		
		if (type.equals("orders")) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar2.setVisibility(View.VISIBLE);
	 	}		
		 
	};  

	 @Override
	 public void doPostExecute(Object result, String type) {
		 
		ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1); 
		ProgressBar progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
			
		if (type.equals("orders")) {
			progressBar.setVisibility(View.INVISIBLE);
		} else {
			progressBar2.setVisibility(View.INVISIBLE);
	 	}		 
		
		SetChartData(result);

	 }		
	
	 public  String GetApiRoute() {
		 return "magapp_dashboard.charts";
	 }
	 
	public void AddAmountsSpinner() {
		Spinner AmountsPlotOptions = (Spinner) rootView
				.findViewById(R.id.AmountsPlotOptions);
		String[] plot_item_data = new String[] { "Last 24 Hours",
				"Last 7 Days", "Current Month", "YTD", "2YTD" };
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.custom_spinner_row, plot_item_data);
		dataAdapter.setDropDownViewResource(R.layout.custom_spinner_row);
		AmountsPlotOptions.setAdapter(dataAdapter);

		AmountsPlotOptions
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View view,
							int position, long id) {
						if (AmountsData != null) {
							switch (position) {
							case 0:
								AmountsDateFormat = new SimpleDateFormat("hha");
								break;
							case 1:
								AmountsDateFormat = new SimpleDateFormat(
										"MM-dd");
								break;
							case 2:
								AmountsDateFormat = new SimpleDateFormat(
										"MM-dd");
								break;
							case 3:
								AmountsDateFormat = new SimpleDateFormat(
										"MM-yyyy");
								break;
							case 4:
								AmountsDateFormat = new SimpleDateFormat(
										"MM-yyyy");
								break;
							}

							PrepareChartData((HashMap) AmountsData[position],
									"amounts");
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

	}

	public void AddOrdersSpinner() {
		Spinner OrdersPlotOptions = (Spinner) rootView
				.findViewById(R.id.OrdersPlotOptions);
		String[] plot_item_data = new String[] { "Last 24 Hours",
				"Last 7 Days", "Current Month", "YTD", "2YTD" };
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.custom_spinner_row, plot_item_data);
		dataAdapter.setDropDownViewResource(R.layout.custom_spinner_row);
		OrdersPlotOptions.setAdapter(dataAdapter);

		OrdersPlotOptions
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View view,
							int position, long id) {

						if (OrdersData != null) {
							switch (position) {
							case 0:
								OrdersDateFormat = new SimpleDateFormat("hha");
								break;
							case 1:
								OrdersDateFormat = new SimpleDateFormat("MM-dd");
								break;
							case 2:
								OrdersDateFormat = new SimpleDateFormat("MM-dd");
								break;
							case 3:
								OrdersDateFormat = new SimpleDateFormat(
										"MM-yyyy");
								break;
							case 4:
								OrdersDateFormat = new SimpleDateFormat(
										"MM-yyyy");
								break;
							}

							PrepareChartData((HashMap) OrdersData[position],
									"orders");
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

	}

	public void PrepareChartData(HashMap chart_info_obj, String type) {

		Object[] x_obj = (Object[]) chart_info_obj.get("x");
		Object[] y_obj = (Object[]) chart_info_obj.get("y");
		Object[] totals_obj = (Object[]) chart_info_obj.get("totals");

		Number[] x_row = new Number[x_obj.length];
		Number[] y_row = new Number[y_obj.length];
		for (int i = 0; i < x_obj.length; i++) {
			x_row[i] = (Number) x_obj[i];
			y_row[i] = (Number) y_obj[i];
		}

		TextView Revenue = (TextView) rootView
				.findViewById(R.id.OrdersRevenueValue);
		TextView Tax = (TextView) rootView.findViewById(R.id.OrdersTaxValue);
		TextView Shipping = (TextView) rootView
				.findViewById(R.id.OrdersShippingValue);
		TextView Qty = (TextView) rootView.findViewById(R.id.OrdersQtyValue);

		if (type == "orders") {
			AddOrdersPlot(y_row, x_row);
		} else if (type == "amounts") {
			AddAmountsPlot(y_row, x_row);
			Revenue = (TextView) rootView
					.findViewById(R.id.AmountsRevenueValue);
			Tax = (TextView) rootView.findViewById(R.id.AmountsTaxValue);
			Shipping = (TextView) rootView
					.findViewById(R.id.AmountsShippingValue);
			Qty = (TextView) rootView.findViewById(R.id.AmountsQtyValue);
		}

		Revenue.setText(totals_obj[0].toString());
		Tax.setText(totals_obj[1].toString());
		Shipping.setText(totals_obj[2].toString());
		Qty.setText(totals_obj[3].toString());

	}

	public void AddOrdersPlot(Number[] orders_num, Number[] order_dates) {

		XYPlot OrdersPlot = (XYPlot) rootView.findViewById(R.id.OrdersPlot);
		OrdersPlot.clear();
		XYSeries series2 = new SimpleXYSeries(Arrays.asList(order_dates),
				Arrays.asList(orders_num), "");

		OrdersPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
		OrdersPlot.setPlotMargins(0, 0, 0, 0);

		SizeMetrics sm = new SizeMetrics(0, SizeLayoutType.FILL, 0,
				SizeLayoutType.FILL);
		OrdersPlot.getGraphWidget().setSize(sm);
		OrdersPlot.getGraphWidget().position(0, XLayoutStyle.RELATIVE_TO_LEFT,
				0, YLayoutStyle.RELATIVE_TO_TOP, AnchorPosition.LEFT_TOP);

		// OrdersPlot.setTicksPerRangeLabel(1);
		// OrdersPlot.setDomainStep(XYStepMode.SUBDIVIDE, orders_num.length);
		OrdersPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);
		// mySimpleXYPlot.setRangeBoundaries(0, BoundaryMode.FIXED, 6,
		// BoundaryMode.GROW);
		// get rid of decimal points in our range labels:
		OrdersPlot.setRangeValueFormat(new DecimalFormat("0"));

		Paint lineFill = new Paint();
		lineFill.setColor(Color.parseColor("#F4D4B2"));

		LineAndPointFormatter formatter = new LineAndPointFormatter(
				Color.parseColor("#DB4814"), Color.parseColor("#DB4814"),
				Color.parseColor("#F4D4B2"), (PointLabelFormatter) null);
		formatter.setFillPaint(lineFill);

		OrdersPlot.addSeries(series2, formatter);

		OrdersPlot.setDomainValueFormat(new Format() {
			private static final long serialVersionUID = 1L;

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo,
					FieldPosition pos) {
				java.util.Date date = new java.util.Date(((Number) obj)
						.longValue() * 1000);
				return OrdersDateFormat.format(date, toAppendTo, pos);
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		});

		// redraw
		OrdersPlot.redraw();
	}

	public void AddAmountsPlot(Number[] amounts_num, Number[] amounts_dates) {

		XYPlot AmountsPlot = (XYPlot) rootView.findViewById(R.id.AmountsPlot);
		AmountsPlot.clear();

		XYSeries series2 = new SimpleXYSeries(Arrays.asList(amounts_dates),
				Arrays.asList(amounts_num), "");

		AmountsPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
		AmountsPlot.setPlotMargins(0, 0, 0, 0);

		SizeMetrics sm = new SizeMetrics(0, SizeLayoutType.FILL, 0,
				SizeLayoutType.FILL);
		AmountsPlot.getGraphWidget().setSize(sm);
		AmountsPlot.getGraphWidget().position(0, XLayoutStyle.RELATIVE_TO_LEFT,
				0, YLayoutStyle.RELATIVE_TO_TOP, AnchorPosition.LEFT_TOP);

		// mySimpleXYPlot.setTicksPerRangeLabel(1);
		// mySimpleXYPlot.setDomainStep(XYStepMode.SUBDIVIDE, points);
		// AmountsPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL,1);
		// mySimpleXYPlot.setRangeBoundaries(0, BoundaryMode.FIXED, 6,
		// BoundaryMode.GROW);
		// get rid of decimal points in our range labels:
		AmountsPlot.setRangeValueFormat(new DecimalFormat("0"));

		Paint lineFill = new Paint();
		lineFill.setColor(Color.parseColor("#F4D4B2"));

		LineAndPointFormatter formatter = new LineAndPointFormatter(
				Color.parseColor("#DB4814"), Color.parseColor("#DB4814"),
				Color.parseColor("#F4D4B2"), (PointLabelFormatter) null);
		formatter.setFillPaint(lineFill);
		AmountsPlot.addSeries(series2, formatter);

		AmountsPlot.setDomainValueFormat(new Format() {
			private static final long serialVersionUID = 1L;

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo,
					FieldPosition pos) {
				java.util.Date date = new java.util.Date(((Number) obj)
						.longValue() * 1000);
				return AmountsDateFormat.format(date, toAppendTo, pos);
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		});

		// redraw
		AmountsPlot.redraw();
	}

	public void SetChartData(Object data) {
		HashMap data_map = (HashMap) data;

		if (data_map.get("orders") != null) {
			OrdersData = (Object[]) data_map.get("orders");
			AddOrdersSpinner();			 
		} else {
			AmountsData = (Object[]) data_map.get("amounts");
			AddAmountsSpinner();			 
		}
	}
/*
	class ChartsTask extends AsyncTask<String, Void, Object> {
		protected void onPreExecute() {

			super.onPreExecute();

		};

		protected Object doInBackground(String... request_type) {

			Vector params = new Vector();
			HashMap req_type = new HashMap();
			req_type.put("type", request_type[0]);
			if (request_type[0].equals("orders")) {
				progressBar.setVisibility(View.VISIBLE);
			} else {
				progressBar2.setVisibility(View.VISIBLE);
			}

			params.add(req_type);
			Object charts_info;
			try {
				charts_info = (Object) client.callEx("call", new Object[] {
						api_session, "magapp_dashboard.charts", params });
				return charts_info;
			} catch (XMLRPCException e) {
				Log.e("Sashas", e.getMessage());
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {

			if (result instanceof XMLRPCException) {
				ShowMessage(result.toString());
				HandleError(result.toString());
			} else {
				SetChartData(result);
			}

		}

	}
*/
	public void ShowMessage(String text) {
		Toast.makeText(this.getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	public void HandleError(String error) {
		String regex_script = "\\[code (.*?)\\]";
		Pattern p = Pattern.compile(regex_script);

		Matcher m = p.matcher(error);
		String error_code = "0";
		if (m.find()) {
			error_code = m.group(1).toString();
		}
		/*Code 5 ? session expired*/
		if (error_code.equals("5")) {
			Intent Login = new Intent(getActivity(), LoginActivity.class);
			this.startActivity(Login);
			getActivity().finish();
		}

	}
}
