package com.example.magapp;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidplot.Plot;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

public class HomeFragment extends Fragment {

	private XYPlot mySimpleXYPlot;
	private View rootView;
	
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		 rootView =  inflater.inflate(R.layout.home, null);
	       Number[] orders_num = {5, 8, 9, 2, 5};
	       Number[] order_dates = {
	        		1359698502,  // Feb
	        		1362117702, // Mar
	        		1364792502, // April
	                1367384502, // May
	                1370062902,  // Jun	              
	        		};
	        
		 AddOrdersPlot(orders_num,order_dates);
		 AddOrdersSpinner();
 	
	    return rootView;
	    
	  }
	 
	 public void AddOrdersSpinner(){
		 Spinner OrdersPlotOptions = (Spinner) rootView.findViewById(R.id.PlotOptions);
		 String[] plot_item_data = new String[] {"Last 24 Hours", "Last 7 Days", "YTD", "2YTD" };
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinner_row, plot_item_data);
				dataAdapter.setDropDownViewResource(R.layout.custom_spinner_row );
				OrdersPlotOptions.setAdapter(dataAdapter);
		
	    OrdersPlotOptions.setOnItemSelectedListener(new OnItemSelectedListener() {

	        public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
	            //int item = OrdersPlotOptions.getSelectedItemPosition();
	        //	Log.e("Sashas", Integer.toString(position));	
	        	if (position==0) {
	     	       Number[] orders_num = {5, 8, 9, 2, 5};
	     	      Number[] order_dates = {
	    	        		1359698502,  // Feb
	    	        		1362117702, // Mar
	    	        		1364792502, // April
	    	                1367384502, // May
	    	                1370062902,  // Jun	              
	    	        		};
	    	        
	    		 AddOrdersPlot(orders_num,order_dates);
	        	}
	        	if (position==1) {
	        		   Number[] orders_num = {1, 2, 3, 4, 6};
	       	        Number[] order_dates = {
	       	        		1370062902,  // Jul
	       	        		1375333302, // Aug
	       	        		1378011702, // Sep
	       	        		1380603702, // Oct
	       	        		1383282102,  // Dec	       	              
	       	        };
	       	     AddOrdersPlot(orders_num,order_dates);
	        
	        	}
	        	
	        }
	        public void onNothingSelected(AdapterView<?> arg0) { }
	    });
			
	 }
	 public void AddOrdersPlot( Number[] orders_num, Number[] order_dates){
		 
		 mySimpleXYPlot = (XYPlot) rootView.findViewById(R.id.mySimpleXYPlot);
	  
		 mySimpleXYPlot.clear();
	     // create our series from our array of nums:
	        XYSeries series2 = new SimpleXYSeries(
	                Arrays.asList(order_dates),
	                Arrays.asList(orders_num)," Orders");
	        
	        mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);	    
	        mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.WHITE);
	        mySimpleXYPlot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.WHITE);	   
	        mySimpleXYPlot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
	        mySimpleXYPlot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
	        mySimpleXYPlot.getGraphWidget().setGridPadding(0, 10, 0, 0);
	        mySimpleXYPlot.getBackgroundPaint().setColor(Color.WHITE);
	        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.ROUNDED, (float) 5, (float) 5);
	        
	        
	        mySimpleXYPlot.setPlotMargins(0, 0, 0, 0);
	        mySimpleXYPlot.setPlotPadding(0, 10, 0, 0);
	        mySimpleXYPlot.setGridPadding(0, 10, 5, 0);
	        
	      
	        SizeMetrics sm = new SizeMetrics(0, SizeLayoutType.FILL, 0, SizeLayoutType.FILL);
	        mySimpleXYPlot.getGraphWidget().setSize(sm);
	        mySimpleXYPlot.getGraphWidget().position(
	        		0, XLayoutStyle.RELATIVE_TO_LEFT,
	                0, YLayoutStyle.RELATIVE_TO_TOP,
	                AnchorPosition.LEFT_TOP);
	        
	        mySimpleXYPlot.setTicksPerRangeLabel(1);
	        mySimpleXYPlot.setTicksPerDomainLabel(1);	        
	        mySimpleXYPlot.setDomainStep(XYStepMode.SUBDIVIDE, order_dates.length);
	        
	        mySimpleXYPlot.setRangeBoundaries(0, BoundaryMode.FIXED,  6, BoundaryMode.GROW);
	   //  mySimpleXYPlot.setDomainBoundaries(1391234502 , BoundaryMode.FIXED, 1401585353, BoundaryMode.FIXED);
	        // get rid of decimal points in our range labels:
	        mySimpleXYPlot.setRangeValueFormat(new DecimalFormat("0"));
	        	        	  
	        //setup our line fill paint to be a slightly transparent gradient:
	        Paint lineFill = new Paint();	         
	        lineFill.setColor(Color.parseColor("#F4D4B2"));
	 	        	       
	        LineAndPointFormatter formatter  = new LineAndPointFormatter(  
	        		Color.parseColor("#DB4814"),
	        		Color.parseColor("#DB4814"),
	        		Color.parseColor("#F4D4B2"), 
	        		new PointLabelFormatter());
	        formatter.setFillPaint(lineFill);
	       
	        mySimpleXYPlot.addSeries(series2, formatter);
	   
	        mySimpleXYPlot.setDomainValueFormat(new Format() {	 	       
	            // create a simple date format that draws on the year portion of our timestamp.
	            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
	            // for a full description of SimpleDateFormat.
	            
	 
	            @Override
	            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
	            	SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMMM");
	                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
	                // we multiply our timestamp by 1000:
	                long timestamp = ((Number) obj).longValue() * 1000;
//	            /    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	                Date date = new Date(timestamp);
	               // Log.e("Sashas",((Number) obj).toString());
	              //  Log.e("Sashas",dateFormat.format(date));
	                return dateFormat.format(date, toAppendTo, pos);
	            }
	 
	            @Override
	            public Object parseObject(String source, ParsePosition pos) {
	                return null;	 
	            }
	        });
	        //redraw
	        mySimpleXYPlot.redraw();
		 
	 }
 
	
   
}
