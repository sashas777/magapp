/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.main;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.magapp.analytics.AnalyticsApplication;
import com.magapp.chart.CurrencyValueFormatter;
import com.magapp.chart.DateValueFormatter;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

public class HomeFragment extends Fragment implements RequestInterface {

    private View rootView;
    private SimpleDateFormat OrdersDateFormat = new SimpleDateFormat("hha");
    private Object[] AmountsData, OrdersData;
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    private MenuItem chartPeriod;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home, null);

        RequestTask task;

        Vector params = new Vector();
        task = new RequestTask(this, getActivity(), "magapp_dashboard.charts");
        Log.e("Sashas", "HomeFragment:onCreateView");
        task.execute(params);
        setHasOptionsMenu(true);
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        return rootView;
    }

    public void onPreExecute() {
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.amountsChart).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.ordersChart).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.OrdersStats).setVisibility(View.INVISIBLE);
        if (chartPeriod != null)
            chartPeriod.setVisible(false);
        createChart("amounts");
        createChart("orders");
    }

    @Override
    public void doPostExecute(Object result, String result_api_point) {
        if (isAdded()) {
            ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.INVISIBLE);
            SetChartData(result);
            if (chartPeriod != null)
                chartPeriod.setVisible(false);
        }
        rootView.findViewById(R.id.amountsChart).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.ordersChart).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.OrdersStats).setVisibility(View.VISIBLE);

    }

    public void RequestFailed(String error) {
        ((BaseActivity) getActivity()).ShowMessage(error);
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
        Intent Login = new Intent(getActivity(), LoginActivity.class);
        this.startActivity(Login);
        getActivity().finish();
    }

    public void createChart(String code) {

        LineChart chart;

        if (code.equals("amounts")) {
            chart = (LineChart) rootView.findViewById(R.id.amountsChart);
        } else {
            chart = (LineChart) rootView.findViewById(R.id.ordersChart);
        }

    }


    public void PrepareChartData(HashMap ordersInfoObj, HashMap amountsInfoObj) {

        TextView Revenue = (TextView) rootView.findViewById(R.id.OrdersRevenueValue);
        TextView Tax = (TextView) rootView.findViewById(R.id.OrdersTaxValue);
        TextView Shipping = (TextView) rootView.findViewById(R.id.OrdersShippingValue);
        TextView Qty = (TextView) rootView.findViewById(R.id.OrdersQtyValue);
        Object[] totalsObj = (Object[]) ordersInfoObj.get("totals");
        updateChart("amounts", amountsInfoObj);
        updateChart("orders", ordersInfoObj);

        Revenue.setText(totalsObj[0].toString());
        Tax.setText(totalsObj[1].toString());
        Shipping.setText(totalsObj[2].toString());
        Qty.setText(totalsObj[3].toString());
    }

    public void updateChart(String code, HashMap chartInfo) {
        LineChart chart;
        if (code.equals("amounts")) {
            chart = (LineChart) rootView.findViewById(R.id.amountsChart);
        } else {
            chart = (LineChart) rootView.findViewById(R.id.ordersChart);
        }

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        Object[] xObj = (Object[]) chartInfo.get("x");
        Object[] yObj = (Object[]) chartInfo.get("y");

        OrdersDateFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        for (int i = 0; i < xObj.length; i++) {
            java.util.Date date = new java.util.Date(((Number) xObj[i]).longValue() * 1000);
            xVals.add(OrdersDateFormat.format(date));
            if (code.equals("amounts")) {
                yVals.add(new Entry(i, Float.valueOf(yObj[i].toString())));
            } else {
                yVals.add(new Entry(i, new Double(yObj[i].toString()).intValue()));
            }
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DateValueFormatter(xVals));

        LineDataSet chartDataSet;
        if (code.equals("amounts")) {
            chartDataSet = new LineDataSet(yVals, "Amounts");
        } else {
            chartDataSet = new LineDataSet(yVals, "Orders");
        }
        chartDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        chartDataSet.setCubicIntensity(0.2f);
        chartDataSet.setDrawFilled(true);
        chartDataSet.setDrawCircles(false);
        chartDataSet.setLineWidth(2f);
        chartDataSet.setHighLightColor(getResources().getColor(R.color.orange));
        chartDataSet.setColor(getResources().getColor(R.color.orange));
        chartDataSet.setFillColor(getResources().getColor(R.color.orange));
        chartDataSet.setFillAlpha(255);
        chartDataSet.setDrawValues(false);

        LineData chartData = new LineData(chartDataSet);
        chart.setData(chartData);
        chart.fitScreen();
        chart.setScaleMinima(0, 0);
        chart.animateXY(2000, 2000);
        chart.resetViewPortOffsets();
        chart.setExtraLeftOffset(5);
        chart.setExtraRightOffset(5);

        /*Default Char Properties*/

        YAxis YAxisLeft = chart.getAxisLeft();
        YAxisLeft.setAxisMinimum(0f);
        YAxisLeft.setDrawGridLines(false);
        if (code.equals("amounts")) {
            YAxisLeft.setValueFormatter(new CurrencyValueFormatter());
        }
        YAxisLeft.setGranularity(1f);

        YAxis YAxisRight = chart.getAxisRight();
        //  YAxisRight.setEnabled(false);
        YAxisRight.setAxisMinimum(0f);
        YAxisRight.setDrawGridLines(false);
        if (code.equals("amounts")) {
            YAxisRight.setValueFormatter(new CurrencyValueFormatter());
        }
        YAxisRight.setGranularity(1f);

        chart.setDrawBorders(false);
        chart.setScaleEnabled(false);
        chart.setDragEnabled(false);
        chart.setTouchEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        Description chartDescription = new Description();
        chartDescription.setEnabled(false);
        chart.setDescription(chartDescription);
        chart.setBackgroundColor(Color.WHITE);
        /*Default Chart Properties*/

        chart.invalidate();
    }

    public void SetChartData(Object data) {
        HashMap data_map = (HashMap) data;
        OrdersData = (Object[]) data_map.get("orders");
        AmountsData = (Object[]) data_map.get("amounts");
        OrdersDateFormat = new SimpleDateFormat("hha");
        PrepareChartData((HashMap) OrdersData[0], (HashMap) AmountsData[0]);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_period);
        if (item != null)
            chartPeriod = item;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e("Sashas", "HomeFragment:onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.tfhours:
                OrdersDateFormat = new SimpleDateFormat("hha");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("24H")
                        .build());
                PrepareChartData((HashMap) OrdersData[0], (HashMap) AmountsData[0]);
                return true;

            case R.id.week:
                OrdersDateFormat = new SimpleDateFormat("MM-dd");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("7 Days")
                        .build());
                PrepareChartData((HashMap) OrdersData[1], (HashMap) AmountsData[1]);
                return true;

            case R.id.month:
                OrdersDateFormat = new SimpleDateFormat("MM-dd");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("Month")
                        .build());
                PrepareChartData((HashMap) OrdersData[2], (HashMap) AmountsData[2]);
                return true;

            case R.id.ytd:
                OrdersDateFormat = new SimpleDateFormat("MM-yyyy");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("YTD")
                        .build());
                PrepareChartData((HashMap) OrdersData[3], (HashMap) AmountsData[3]);
                return true;

            case R.id.twoytd:
                OrdersDateFormat = new SimpleDateFormat("MM-yyyy");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Chart")
                        .setAction("2YTD")
                        .build());
                PrepareChartData((HashMap) OrdersData[4], (HashMap) AmountsData[4]);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
