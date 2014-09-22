package com.magapp.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.main.LoginActivity;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;

public class OrderInfoFragment extends Fragment implements RequestInterface  {

	
	public View rootView;
	private Menu menu_settings;
	 

	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.order_info_fragment, null);
			int order_id=((OrderInfoActivity)getActivity()).GetOrderId();
			Vector params = new Vector();	 
			RequestTask task;
			HashMap map_filter = new HashMap(); 
			map_filter.put("order_id", order_id);		
			params.add(map_filter);
			task = new RequestTask(this, getActivity());
			task.execute(params);					
			setHasOptionsMenu(true);
			return rootView;
	}	 
	 @Override
	 public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		  menu.clear();
		  getActivity().getMenuInflater().inflate(R.menu.base, menu);		
		  menu_settings=menu;
		  super.onCreateOptionsMenu(menu, inflater); 
		}
	 
 
	 
		public void onPreExecute(){		
			ProgressBar progressBar =(ProgressBar)  getActivity().findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.VISIBLE);
		};  

		 @Override
		 public void doPostExecute(Object result) {		
			ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.INVISIBLE); 	 		
			HashMap map = (HashMap) result;
			FillData(map);
		 }		
		
		 public  String GetApiRoute() {
			 return "magapp_sales_order.info";
		 }
		 
		 public void RequestFailed(String error) {
			((OrderInfoActivity)getActivity()).ShowMessage(error);
			ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.INVISIBLE); 			
			Intent Login = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(Login);
			getActivity().finish();		 
		 }	
		
		public void FillData(HashMap order) {

			Bundle params = new Bundle();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction mFragmentTransaction = fragmentManager
					.beginTransaction();

			/* Main Info */
			params = new Bundle();
			String cardt_title = order.get("order_title").toString();
			params.putString("card_title", cardt_title);
			params.putString("ip", order.get("remote_ip").toString());
			params.putString("store_name", order.get("store_name").toString());

			String status = order.get("status").toString();
			status = status.substring(0, 1).toUpperCase(Locale.ENGLISH)
					+ status.substring(1);
			params.putString("order_status", status);

			String created_at = cardt_title.substring(
					cardt_title.lastIndexOf("|") + 1, cardt_title.length());
			params.putString("created_at", created_at);
			Fragment main_info_card = new Fragment();
			main_info_card = new MainInfoFragment();
			main_info_card.setArguments(params);
			mFragmentTransaction.add(R.id.main_info_card, main_info_card);
			/* Main Info */
			/* Account Info */
			params = new Bundle();
			if (order.get("customer_is_guest").toString() == "1")
				params.putString("customer_name", "Guest");
			else
				params.putString("customer_name", order.get("customer_firstname")
						.toString()
						+ " "
						+ order.get("customer_lastname").toString());
			params.putString("customer_group", order.get("customer_group_title")
					.toString());
			params.putString("customer_email", order.get("customer_email")
					.toString());
			Fragment account_card = new Fragment();
			account_card = new CustomerAccountFragment();
			account_card.setArguments(params);
			mFragmentTransaction.add(R.id.account_info, account_card);
			/* Account Info */
			/* Billing Address */
			params = new Bundle();
			Fragment billing_card = new Fragment();
			billing_card = new OrderAddressFragment();
			params.putString("order_address", order.get("billing_address_html")
					.toString());
			params.putString("address_title", "Billing Address");
			billing_card.setArguments(params);
			mFragmentTransaction.add(R.id.bill_address, billing_card);
			/* Billing Address */

			/* Shipping Address */
			if (Integer.parseInt(order.get("is_virtual").toString()) == 0) {

				Fragment shipping_card = new Fragment();
				shipping_card = new OrderAddressFragment();
				params = new Bundle();
				params.putString("order_address", order
						.get("shipping_address_html").toString());
				params.putString("address_title", "Shipping Address");
				shipping_card.setArguments(params);
				mFragmentTransaction.add(R.id.ship_address, shipping_card);

			}
			/* Shipping Address */
			/* Payment Info */
			params = new Bundle();
			params.putString("payment_info", order.get("payment_method_text")
					.toString());
			params.putString("currency_info", order.get("payment_currency_text")
					.toString());
			Fragment payment_card = new Fragment();
			payment_card = new PaymentInfoFragment();
			payment_card.setArguments(params);
			mFragmentTransaction.add(R.id.payment_card, payment_card);
			/* Payment Info */

			/* Items */
			params = new Bundle();
			ArrayList<HashMap> items_array = new ArrayList<HashMap>();
			Object[] items = (Object[]) order.get("items");
			for (Object item : items) {
				HashMap item_data = (HashMap) item;
				items_array.add(item_data);
			}
			params.putSerializable("items", items_array);
			Fragment items_card = new Fragment();
			items_card = new ItemsFragment();
			items_card.setArguments(params);
			mFragmentTransaction.add(R.id.items_card, items_card);
			/* Items */
			/* Totals */
			params = new Bundle();
			Object[] totals = (Object[]) order.get("totals");
			params.putSerializable("totals", totals);
			Fragment totals_card = new Fragment();
			totals_card = new TotalsFragment();
			totals_card.setArguments(params);
			mFragmentTransaction.add(R.id.totals_card, totals_card);
			/* Totals */
			mFragmentTransaction.commit();
			
			/*Menu Item*/
			MenuItem invoice_item = menu_settings.findItem(R.id.invoice);
			 
		//	if (order.get("can_invoice").toString()=="1"){
				invoice_item.setVisible(true);
				
		//	}else {
		//		invoice_item.setVisible(false);
	//		}
			getActivity().invalidateOptionsMenu();
			/*Menu Item*/
		}	
		
 
	 
}
