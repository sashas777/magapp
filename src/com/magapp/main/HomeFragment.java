/*
 * Copyright (c) 2016.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.main;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.YLabels;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.magapp.analytics.AnalyticsApplication;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

public class HomeFragment extends Fragment  implements RequestInterface{

	private View rootView;
	private SimpleDateFormat OrdersDateFormat = new SimpleDateFormat("hha");
	private Object[] AmountsData, OrdersData;
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.home, null);

		RequestTask task;
		 
		Vector params = new Vector();		 	 
		task = new RequestTask(this, getActivity(),"magapp_dashboard.charts");

		/*new chart*/
		LineChart chart = (LineChart) rootView.findViewById(R.id.chart);
		chart.setStartAtZero(true);
		chart.setDrawYValues(false);
		chart.setDrawBorder(false);
		chart.setDrawLegend(false);
		chart.setScaleEnabled(false);
		chart.setDragEnabled(false);
		chart.setPinchZoom(false);
		chart.setTouchEnabled(false);
		chart.setDrawGridBackground(false);
		chart.setDescription("Orders");
		chart.setBackgroundColor(Color.WHITE);
		YLabels yl = chart.getYLabels();
		yl.setFormatter(new IntegerValueFormatter());

		LineChart AmountsChart = (LineChart) rootView.findViewById(R.id.chart2);
		AmountsChart.setStartAtZero(true);
		AmountsChart.setDrawYValues(false);
		AmountsChart.setDrawBorder(false);
		AmountsChart.setDrawLegend(false);
		AmountsChart.setScaleEnabled(false);
		AmountsChart.setDragEnabled(false);
		AmountsChart.setTouchEnabled(false);
		AmountsChart.setPinchZoom(false);
		AmountsChart.setDrawGridBackground(false);
		AmountsChart.setDescription("Amounts");
		AmountsChart.setBackgroundColor(Color.WHITE);
		/*New chart*/

		task.execute(params);
        setHasOptionsMenu(true);
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
		return rootView;
	}

	public void onPreExecute(){		
		ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1); 
		progressBar.setVisibility(View.VISIBLE);
	}

    @Override
	 public void doPostExecute(Object result, String result_api_point) {
		ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);  
		progressBar.setVisibility(View.INVISIBLE); 	 		
		SetChartData(result);

	 }
	 
	 public void RequestFailed(String error) {
		((BaseActivity)getActivity()).ShowMessage(error);
		ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);  
		progressBar.setVisibility(View.INVISIBLE); 			
		Intent Login = new Intent(getActivity(), LoginActivity.class);
		this.startActivity(Login);
		getActivity().finish();		 
	 }



	public void AddOrdersSpinner() {
	 	Spinner OrdersPlotOptions = (Spinner) rootView
				.findViewById(R.id.OrdersPlotOptions);
		String[] plot_item_data = new String[] { "Last 24 Hours", "Last 7 Days", "Current Month", "YTD", "2YTD" };
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_row, plot_item_data);
		dataAdapter.setDropDownViewResource(R.layout.custom_spinner_row);
		OrdersPlotOptions.setAdapter(dataAdapter);

		OrdersPlotOptions.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {

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
							OrdersDateFormat = new SimpleDateFormat("MM-yyyy");
							break;
						case 4:
							OrdersDateFormat = new SimpleDateFormat("MM-yyyy");
							break;
					}
					PrepareChartData((HashMap) OrdersData[position],(HashMap)  AmountsData[position]);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void PrepareChartData(HashMap orders_info_obj, HashMap amounts_info_obj) {

		TextView Revenue = (TextView) rootView.findViewById(R.id.OrdersRevenueValue);
		TextView Tax = (TextView) rootView.findViewById(R.id.OrdersTaxValue);
		TextView Shipping = (TextView) rootView.findViewById(R.id.OrdersShippingValue);
		TextView Qty = (TextView) rootView.findViewById(R.id.OrdersQtyValue);

		LineChart chart = (LineChart) rootView.findViewById(R.id.chart);
		LineChart AmountsChart = (LineChart) rootView.findViewById(R.id.chart2);

		ArrayList<String> xVals = new ArrayList<String>();
		ArrayList<Entry> vals1 = new ArrayList<Entry>();

		Object[] x_obj = (Object[]) orders_info_obj.get("x");
		Object[] y_obj = (Object[]) orders_info_obj.get("y");
		Object[] totals_obj = (Object[]) orders_info_obj.get("totals");

		/* Amounts */
		Object[] amounts_x_obj = (Object[]) amounts_info_obj.get("x");
		Object[] amounts_y_obj = (Object[]) amounts_info_obj.get("y");
		ArrayList<String> xAmountsVals = new ArrayList<String>();
		ArrayList<Entry> AmountsVals1 = new ArrayList<Entry>();
		/* Amounts */

		OrdersDateFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

		for (int i = 0; i < x_obj.length; i++) {
			java.util.Date date = new java.util.Date(((Number) x_obj[i]).longValue() * 1000);
			xVals.add(OrdersDateFormat.format(date) + "   ");
			vals1.add(new Entry(new Double(y_obj[i].toString()).intValue(), i));
		}

		/* Amounts */
		for (int i = 0; i < amounts_x_obj.length; i++) {
			java.util.Date date = new java.util.Date(((Number) amounts_x_obj[i]).longValue() * 1000);
			xAmountsVals.add(OrdersDateFormat.format(date) + "   ");
			AmountsVals1.add(new Entry( Float.valueOf(amounts_y_obj[i].toString()), i));

		}

		LineDataSet set2 = new LineDataSet(AmountsVals1, "Amounts");
		set2.setDrawCubic(true);
		set2.setCubicIntensity(0.2f);
		set2.setDrawFilled(true);
		set2.setDrawCircles(false);
		set2.setLineWidth(2f);
		set2.setCircleSize(5f);
		set2.setHighLightColor(Color.rgb(244, 117, 117));
		set2.setColor(Color.rgb(234, 118, 1));
		/* Amounts */

		LineDataSet set1 = new LineDataSet(vals1, "Orders");
		set1.setDrawCubic(true);
		set1.setCubicIntensity(0.2f);
		set1.setDrawFilled(true);
		set1.setDrawCircles(false);
		set1.setLineWidth(2f);
		set1.setCircleSize(5f);
		set1.setHighLightColor(Color.rgb(244, 117, 117));
		set1.setColor(Color.rgb(234, 118, 1));

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set1);

		/* Amounts */
		ArrayList<LineDataSet> AmountsdataSets = new ArrayList<LineDataSet>();
		AmountsdataSets.add(set2);
		LineData Amountsdata = new LineData(xAmountsVals, set2);
		AmountsChart.setUnit(" $");
		AmountsChart.setData(Amountsdata);
		AmountsChart.fitScreen();
		AmountsChart.setScaleMinima(0, 0);
		/* Amounts */

		LineData data = new LineData(xVals, dataSets);

		// set data
		chart.setData(data);
		chart.fitScreen();
		chart.setScaleMinima(0, 0);

		Revenue.setText(totals_obj[0].toString());
		Tax.setText(totals_obj[1].toString());
		Shipping.setText(totals_obj[2].toString());
		Qty.setText(totals_obj[3].toString());

		chart.animateXY(2000, 2000);
		AmountsChart.animateXY(2000, 2000);
		chart.invalidate();
		AmountsChart.invalidate();
	}

	public void SetChartData(Object data) {
		HashMap data_map = (HashMap) data;

		OrdersData = (Object[]) data_map.get("orders");
		AddOrdersSpinner();
		AmountsData = (Object[]) data_map.get("amounts");
	}
	public class IntegerValueFormatter implements ValueFormatter {
		@Override
		public String getFormattedValue(float value) {
			Integer val=(int) value;

			if (val==0 && value>0) {
				value=value*10;
				val=(int) value;
			}

			return val.toString();
		}
	}

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_period);
        if (item!=null)
            item.setVisible(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e("Sashas","HomeFragment:onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.tfhours:
                OrdersDateFormat = new SimpleDateFormat("hha");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("24H")
                        .build());
                return true;

            case R.id.week:
                OrdersDateFormat = new SimpleDateFormat("MM-dd");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("7 Days")
                        .build());
                return true;

            case R.id.month:
                OrdersDateFormat = new SimpleDateFormat("MM-dd");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("Month")
                        .build());
                return true;

            case R.id.ytd:
                OrdersDateFormat = new SimpleDateFormat("MM-yyyy");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("YTD")
                        .build());
                return true;

            case R.id.twoytd:
                OrdersDateFormat = new SimpleDateFormat("MM-yyyy");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("2YTD")
                        .build());
                PrepareChartData((HashMap) OrdersData[4],(HashMap)  AmountsData[4]);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
