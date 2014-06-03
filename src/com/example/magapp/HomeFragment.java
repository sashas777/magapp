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
    //private	SimpleDateFormat OrdersDateFormat = new SimpleDateFormat("dd-MM");
	private SimpleDateFormat OrdersDateFormat = new SimpleDateFormat("hh:mma");
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		 rootView =  inflater.inflate(R.layout.home, null);
		 OrdersDateFormat = new SimpleDateFormat("hh:mma");
	       Number[] orders_num = {5, 8, 9, 2, 5,2,3};
	        Number[] order_dates = {
	        		1367913023,  //3 50
	        		1367920223, // 5 50 
	        		1367923823, // 6 50 
	        		1367931023, // 8 50			     	    	 
	        		1367934023,  //9 40  
	        		1367934023, // 10 40
	        		1367941823 // 11 50           
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
	           
	        	if (position==0) {
	        		OrdersDateFormat = new SimpleDateFormat("hh:mma");
	     	       Number[] orders_num = {5, 8, 9, 2, 5,2,3};
	    	        Number[] order_dates = {
	    	        		1367913023,  //3 50
	    	        		1367920223, // 5 50 
	    	        		1367923823, // 6 50 
	    	        		1367931023, // 8 50			     	    	 
	    	        		1367934023,  //9 40  
	    	        		1367934023, // 10 40
	    	        		1367941823 // 11 50           
	       	        };	     	       
	     	     
	    	        
	    		 AddOrdersPlot(orders_num,order_dates);
	        	}
	        	if (position==1) {
	        		OrdersDateFormat = new SimpleDateFormat("dd-MM");
	        		   Number[] orders_num = {5, 8, 9, 2, 5,2,3};
		    	        Number[] order_dates = {
			     	    		 1367290270,  // April 30
			     	    		1367380223, // May 1
			     	    		1367466623, // May 2
			     	    		1367553023, // May 3
			     	    		1367639423,  // May 4	  
			     	    		1367725823, // May 5
			     	    		1367898623 // May 7    	              
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
	        	private static final long serialVersionUID = 1L; 
	            @Override
	            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
 
	                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
	                // we multiply our timestamp by 1000:
	              //  long timestamp = new java.util.Date(((Number) obj).longValue()*1000);		
	               
	              //  java.util.Date date = new Date(timestamp);

	                OrdersDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));	 
	            	//long timestamp = ((Number) obj).longValue()*1000;
	            	
	               java.util.Date date=new java.util.Date(((Number) obj).longValue()*1000);	         
	                return OrdersDateFormat.format(date, toAppendTo, pos);
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
