package com.magapp.invoice;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ProgressBar;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.main.LoginActivity;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;
import com.magapp.order.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

public class InvoiceInfoFragment extends Fragment implements RequestInterface  {

	
	public View rootView;
	private Menu menu_settings;
	private Boolean can_invoice=false;
    private int order_id;
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.order_info_fragment, null);
			int increment_id=((InvoiceInfoActivity)getActivity()).GetInvoiceIncrementId();
			Vector params = new Vector();
			RequestTask task;
			HashMap map_filter = new HashMap();
			map_filter.put("invoiceIncrementId", increment_id);
			params.add(map_filter);
			task = new RequestTask(this, getActivity());
			task.execute(params);
		//	setHasOptionsMenu(true);
			return rootView;
	}	 
	 @Override
	 public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
			 return "magapp_sales_order_invoice.info";
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
			FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();

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
			

		}	
		
 
	 
}
