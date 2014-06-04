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

 
	private View rootView;
	private SimpleDateFormat OrdersDateFormat = new SimpleDateFormat("hh:mma");
	private SimpleDateFormat AmountsDateFormat = new SimpleDateFormat("dd-MM");
	
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		 rootView =  inflater.inflate(R.layout.home, null);
		 	 
		 
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
	     
		 Number[] amounts_num = {5, 8, 9, 2, 5,2,3};
	     Number[] amounts_dates = {
    	    		 1367290270,  // April 30
    	    		1367380223, // May 1
    	    		1367466623, // May 2
    	    		1367553023, // May 3
    	    		1367639423,  // May 4	  
    	    		1367725823, // May 5
    	    		1367898623 // May 7    	              
  	        };	
	     
		 AddOrdersPlot(orders_num,order_dates);
		 AddAmountsPlot(amounts_num,amounts_dates);
		 AddOrdersSpinner(); 		 
		 AddAmountsSpinner();
	     return rootView;	   
	  }
	 
	 public void AddAmountsSpinner(){
		 Spinner AmountsPlotOptions = (Spinner) rootView.findViewById(R.id.AmountsPlotOptions);
		 String[] plot_item_data = new String[] {"Last 24 Hours", "Last 7 Days", "YTD", "2YTD" };
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinner_row, plot_item_data);
				dataAdapter.setDropDownViewResource(R.layout.custom_spinner_row );
				AmountsPlotOptions.setAdapter(dataAdapter);
		
			AmountsPlotOptions.setOnItemSelectedListener(new OnItemSelectedListener() {

	        public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
	           
	        	if (position==0) {
 
	        AmountsDateFormat = new SimpleDateFormat("dd-MM");
     		   Number[] amounts_num = {5, 8, 9, 2, 5,2,3};
	    	        Number[] amounts_dates = {
		     	    		 1367290270,  // April 30
		     	    		1367380223, // May 1
		     	    		1367466623, // May 2
		     	    		1367553023, // May 3
		     	    		1367639423,  // May 4	  
		     	    		1367725823, // May 5
		     	    		1367898623 // May 7    	              
	       	        };	
	    	        AddAmountsPlot(amounts_num,amounts_dates);
	        	}
	        	if (position==1) {
	        		AmountsDateFormat = new SimpleDateFormat("hha");
		     	       Number[] amounts_num=new Number[24];
		     	      for (int i=0; i <24; i++) {
		     	    	 amounts_num[i]=  i+1;
			     	     }
		     	      
		    	        Number[] amounts_dates=new Number[24];
		    	     
		    	        amounts_dates[0]=(Number)(Math.round(System.currentTimeMillis()/3600000)*3600) ;
		     	     for (int i=1; i <24; i++) {
		     	    	amounts_dates[i]= (Number) (((Integer)amounts_dates[i-1])-3600);
		     	     }
		    	        
		     	    AddAmountsPlot(amounts_num,amounts_dates);
	        
	        	}
	        	
	        }
	        public void onNothingSelected(AdapterView<?> arg0) { }
	    });
			
	 } 
	 
	 public void AddOrdersSpinner(){
		 Spinner OrdersPlotOptions = (Spinner)  rootView.findViewById(R.id.OrdersPlotOptions);
		 String[] plot_item_data = new String[] {"Last 24 Hours", "Last 7 Days", "YTD", "2YTD" };
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinner_row, plot_item_data);
				dataAdapter.setDropDownViewResource(R.layout.custom_spinner_row );
				OrdersPlotOptions.setAdapter(dataAdapter);
		
	    OrdersPlotOptions.setOnItemSelectedListener(new OnItemSelectedListener() {

	        public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
	           
	        	if (position==0) {
	        		OrdersDateFormat = new SimpleDateFormat("hha");
	     	       Number[] orders_num=new Number[24];
	     	      for (int i=0; i <24; i++) {
	     	    	 orders_num[i]=  i+1;
		     	     }
	     	      
	    	        Number[] order_dates=new Number[24];
	    	     
	    	        order_dates[0]=(Number)(Math.round(System.currentTimeMillis()/3600000)*3600) ;
	     	     for (int i=1; i <24; i++) {
	     	    	 order_dates[i]= (Number) (((Integer)order_dates[i-1])-3600);
	     	     }
	    	        
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
		 
		 XYPlot OrdersPlot = (XYPlot) rootView.findViewById(R.id.OrdersPlot);	  
		 OrdersPlot.clear();
	   
	        XYSeries series2 = new SimpleXYSeries( Arrays.asList(order_dates),Arrays.asList(orders_num),""); 
	        
	        OrdersPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null,null);	        	     
	        OrdersPlot.setPlotMargins(0, 0, 0, 0);	        	  
	      
	        SizeMetrics sm = new SizeMetrics(0, SizeLayoutType.FILL, 0, SizeLayoutType.FILL);
	        OrdersPlot.getGraphWidget().setSize(sm);
	        OrdersPlot.getGraphWidget().position(
	        		0, XLayoutStyle.RELATIVE_TO_LEFT,
	                0, YLayoutStyle.RELATIVE_TO_TOP,
	                AnchorPosition.LEFT_TOP);
	        
	        //  mySimpleXYPlot.setTicksPerRangeLabel(1);	    
	        //  mySimpleXYPlot.setDomainStep(XYStepMode.SUBDIVIDE, points);
	        //  mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL,1);
	        //  mySimpleXYPlot.setRangeBoundaries(0, BoundaryMode.FIXED,  6, BoundaryMode.GROW);	      
	        // get rid of decimal points in our range labels:
	        OrdersPlot.setRangeValueFormat(new DecimalFormat("0"));	        	        	  
	       
	        Paint lineFill = new Paint();	         
	        lineFill.setColor(Color.parseColor("#F4D4B2"));
	 	        	       
	        LineAndPointFormatter formatter  = new LineAndPointFormatter(  
	        		Color.parseColor("#DB4814"),
	        		Color.parseColor("#DB4814"),
	        		Color.parseColor("#F4D4B2"), 
	        		(PointLabelFormatter) null);
	        formatter.setFillPaint(lineFill);
	       
	        OrdersPlot.addSeries(series2, formatter);
	   
	        OrdersPlot.setDomainValueFormat(new Format() {	 	        	            
	        	private static final long serialVersionUID = 1L; 
	            @Override
	            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) { 	    	            
	               java.util.Date date=new java.util.Date(((Number) obj).longValue()*1000);	         
	                return OrdersDateFormat.format(date, toAppendTo, pos);
	            }
	 
	            @Override
	            public Object parseObject(String source, ParsePosition pos) {
	                return null;	 
	            }
	        });
	       
	        //redraw
	        OrdersPlot.redraw();		 
	 }
 
	 public void AddAmountsPlot( Number[] amounts_num, Number[] amounts_dates){
		 
		 XYPlot AmountsPlot = (XYPlot) rootView.findViewById(R.id.AmountsPlot);	  
		 AmountsPlot.clear();
	   
	        XYSeries series2 = new SimpleXYSeries( Arrays.asList(amounts_dates),Arrays.asList(amounts_num),""); 
	        
	        AmountsPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null,null);	        	     
	        AmountsPlot.setPlotMargins(0, 0, 0, 0);	        	  
	      
	        SizeMetrics sm = new SizeMetrics(0, SizeLayoutType.FILL, 0, SizeLayoutType.FILL);
	        AmountsPlot.getGraphWidget().setSize(sm);
	        AmountsPlot.getGraphWidget().position(
	        		0, XLayoutStyle.RELATIVE_TO_LEFT,
	                0, YLayoutStyle.RELATIVE_TO_TOP,
	                AnchorPosition.LEFT_TOP);
	        
	        //  mySimpleXYPlot.setTicksPerRangeLabel(1);	    
	        //  mySimpleXYPlot.setDomainStep(XYStepMode.SUBDIVIDE, points);
	        //  mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL,1);
	        //  mySimpleXYPlot.setRangeBoundaries(0, BoundaryMode.FIXED,  6, BoundaryMode.GROW);	      
	        // get rid of decimal points in our range labels:
	        AmountsPlot.setRangeValueFormat(new DecimalFormat("0"));	        	        	  
	       
	        Paint lineFill = new Paint();	         
	        lineFill.setColor(Color.parseColor("#F4D4B2"));
	 	        	       
	        LineAndPointFormatter formatter  = new LineAndPointFormatter(  
	        		Color.parseColor("#DB4814"),
	        		Color.parseColor("#DB4814"),
	        		Color.parseColor("#F4D4B2"), 
	        		(PointLabelFormatter) null);
	        formatter.setFillPaint(lineFill);	       
	        AmountsPlot.addSeries(series2, formatter);	  
	        
	        AmountsPlot.setDomainValueFormat(new Format() {	 	        	            
	        	private static final long serialVersionUID = 1L; 
	            @Override
	            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) { 	    	            
	               java.util.Date date=new java.util.Date(((Number) obj).longValue()*1000);	         
	                return AmountsDateFormat.format(date, toAppendTo, pos);
	            }
	 
	            @Override
	            public Object parseObject(String source, ParsePosition pos) {
	                return null;	 
	            }
	        });
	       
	        //redraw
	        AmountsPlot.redraw();		 
	 }	
   
}
