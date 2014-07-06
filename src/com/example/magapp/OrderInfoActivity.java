package com.example.magapp;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.magapp.order.CustomerAccountFragment;
import com.example.magapp.order.ItemsFragment;
import com.example.magapp.order.MainInfoFragment;
import com.example.magapp.order.OrderAddressFragment;
import com.example.magapp.order.PaymentInfoFragment;
import com.example.magapp.order.TotalsFragment;
 
 
 

public class OrderInfoActivity extends Activity implements OnNavigationListener {  

	public String api_session;
	public String api_url;
	private XMLRPCClient client;
	private URI uri;
	private TableLayout prodlist;
	private int order_id;
	String[] actions = new String[] {"Order", "Invoice", "Shipment" };
	private CharSequence mTitle;
	public Integer menu_id =-1;
	private ProgressBar progressBar;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderinfo);            
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);		
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter mSpinnerAdapter = new ArrayAdapter<String>(getBaseContext(), 
              R.layout.custom_spinner_row_white,actions);              
        
        getActionBar().setListNavigationCallbacks(mSpinnerAdapter , this);   
         
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
         
      Bundle vars= getIntent().getExtras();
		api_session = vars.getString("api_session");
		api_url = vars.getString("api_url");
		order_id= vars.getInt("order_id");
	 
		uri = URI.create(api_url);
		client = new XMLRPCClient(uri);
		OrderinfoTask task = new OrderinfoTask();
		task.execute(order_id);	  
	}
	
	public void FillData(HashMap order){
		 
		 Bundle params=new Bundle();
		 FragmentManager fragmentManager = getFragmentManager();
		 FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
		  		 		
		 /*Main Info*/
		 params=new Bundle();
		 String cardt_title=order.get("order_title").toString();
		 params.putString("card_title", cardt_title);	
		 params.putString("ip", order.get("remote_ip").toString());	
		 params.putString("store_name", order.get("store_name").toString());	
		 
		 String status=order.get("status").toString();
		 status=status.substring(0,1).toUpperCase(Locale.ENGLISH) + status.substring(1);		 
		 params.putString("order_status", status);
		 
		 String created_at=cardt_title.substring(cardt_title.lastIndexOf("|")+1,cardt_title.length());
		 params.putString("created_at", created_at);	
		 Fragment main_info_card=new Fragment();
		 main_info_card=new MainInfoFragment();		
		 main_info_card.setArguments(params);				 
	   	 mFragmentTransaction.add(R.id.main_info_card,main_info_card);       
		 /*Main Info*/ 		 
		 /*Account Info*/
		 params=new Bundle();
		 if (order.get("customer_is_guest").toString()=="1")
			 params.putString("customer_name", "Guest");	 
		 else
			 params.putString("customer_name",order.get("customer_firstname").toString()+" "+order.get("customer_lastname").toString());		 
		 params.putString("customer_group", order.get("customer_group_title").toString());	
		 params.putString("customer_email",  order.get("customer_email").toString());
		 Fragment account_card=new Fragment();
		 account_card=new CustomerAccountFragment();		
		 account_card.setArguments(params);				 
	   	 mFragmentTransaction.add(R.id.account_info,account_card);      
	   	 /*Account Info*/		 	   	 			 
		 /*Billing Address*/ 
		 params=new Bundle();
	     Fragment billing_card=new Fragment();
	     billing_card=new OrderAddressFragment();			   	 			  
	   	 params.putString("order_address", order.get("billing_address_html").toString());	
	   	 params.putString("address_title", "Billing Address");			   	 
	   	 billing_card.setArguments(params);				 
	   	 mFragmentTransaction.add(R.id.bill_address,billing_card);       			     		   	 		 	  
		 /*Billing Address*/	   	
	     
		 /*Shipping Address*/ 
		 if (Integer.parseInt(order.get("is_virtual").toString())==0) {
			
			     Fragment shipping_card=new Fragment();
			     shipping_card=new OrderAddressFragment();		
			     params=new Bundle();
			   	 params.putString("order_address", order.get("shipping_address_html").toString());	
			  	 params.putString("address_title", "Shipping Address");			   	 
			   	shipping_card.setArguments(params);				 
			   	mFragmentTransaction.add(R.id.ship_address,shipping_card);          			     	
			    
		 } 				 
		 /*Shipping Address*/
		 /*Payment Info*/
		 params=new Bundle();			      	     		   	 			  				 
	   	 params.putString("payment_info", order.get("payment_method_text").toString());	
	   	 params.putString("currency_info",  order.get("payment_currency_text").toString());	 
	   	 Fragment payment_card=new Fragment();
	     payment_card=new PaymentInfoFragment();	
	     payment_card.setArguments(params);				 
	   	 mFragmentTransaction.add(R.id.payment_card,payment_card);   	   	 	   	 	
		 /*Payment Info*/
	   	 
		 /*Items*/
		 params=new Bundle();			
		 ArrayList<HashMap> items_array = new ArrayList<HashMap>();
		 Object[] items = (Object[]) order.get("items");
			for (Object item : items) {
				HashMap item_data = (HashMap) item;
				items_array.add(item_data);	
			}
			 params.putSerializable("items",items_array);	  
	   	 Fragment items_card=new Fragment();
	   	 items_card=new ItemsFragment();	
	   	 items_card.setArguments(params);				 
	   	 mFragmentTransaction.add(R.id.items_card,items_card);  	   	  		
		 /*Items*/
		/*Totals*/
		params=new Bundle();
		Object[] totals = (Object[]) order.get("totals"); 
		params.putSerializable("totals",totals); 	 
	   	Fragment totals_card=new Fragment();
	   	totals_card=new TotalsFragment();	
	   	totals_card.setArguments(params);	
	   	 mFragmentTransaction.add(R.id.totals_card,totals_card);
		/*Totals*/
		 mFragmentTransaction.commit();		
	}
	
	 

	class OrderinfoTask extends AsyncTask<Integer, Void, Object> {

		protected Object doInBackground(Integer... order_id) {
			progressBar.setVisibility(View.VISIBLE);
			int order_id_value = order_id[0];		 
			HashMap map_date = new HashMap();
			 
			map_date.put("eq", order_id_value);
			HashMap map_filter = new HashMap();
			map_filter.put("order_id", map_date);

			Object orders;
			try {
				orders = (Object) client.callEx("call", new Object[] {
						api_session, "magapp_sales_order.info",
						new Object[] { map_filter } });
				return orders;
			} catch (XMLRPCException e) {
				Log.e("Sashas", e.getMessage());
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result instanceof XMLRPCException ) {				 			 
				ShowMessage(result.toString());		
				HandleError(result.toString());
			} else {
				// for(Object o : result) {
				HashMap map = (HashMap) result;
				FillData(map);
				//Log.e("Sashas_Log", map.get("subtotal").toString());
				progressBar.setVisibility(View.INVISIBLE);
				// }

			}
		}
	}
	
	/*Additional for actionbar*/
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (menu_id==-1) {
			menu_id=0;
			return false;
		}
		  switch (itemPosition) {
          case 0:
        	  ShowMessage("Coming Soon");
              break;
          case 1:
        	  ShowMessage("Coming Soon");        	   
        	   break;     
          case 2:
        	  ShowMessage("Coming Soon");
              break;              
		  }   
              
	    
	    return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   switch (item.getItemId()) {
	      case android.R.id.home:
	         NavUtils.navigateUpTo(this,
	               new Intent(this, SalesActivity.class));
	         return true;
	   }
	   return super.onOptionsItemSelected(item);
	}
	

	  public void ShowMessage(String text) {
		  Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	  } 
	  
	  public void HandleError(String error) {
		  String regex_script = "\\[code (.*?)\\]";
			 Pattern p = Pattern.compile(regex_script); 
			 
			 Matcher m = p.matcher(error);
			 String error_code="0";
			 if (m.find())
			 {				 
				  error_code=m.group(1).toString();				 
			 }
			 
			 if (error_code.equals("5")){
				 Intent Login = new Intent(this,LoginActivity.class);				  
				 this.startActivity(Login);
				 this.finish();
			 }
			  
	  }
}
