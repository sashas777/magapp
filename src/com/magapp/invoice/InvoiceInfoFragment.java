/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.invoice;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import com.magapp.common.ItemsFragment;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.interfaces.ActivityInfoInterface;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.LoginActivity;
import com.magapp.main.R;
import com.magapp.order.TotalsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class InvoiceInfoFragment extends Fragment implements RequestInterface  {

	
	public View rootView;

	private Boolean can_cancel=false,can_capture=false;
	String invoice_increment_id,api_point;

	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.order_info_fragment, null);

            if (savedInstanceState!=null) {
                invoice_increment_id=savedInstanceState.getString("increment_id");
                api_point=savedInstanceState.getString("api_point");
            }else {
                invoice_increment_id=getArguments().getString("increment_id");
                api_point=getArguments().getString("api_point");
            }

			Refresh();
			setHasOptionsMenu(true);
			return rootView;
	}	 
	
	public void Refresh() {
		Vector params = new Vector();
		RequestTask task;
		HashMap map_filter = new HashMap();
		map_filter.put("invoiceIncrementId", invoice_increment_id);
		params.add(map_filter);
		task = new RequestTask(this, getActivity(),api_point);
		task.execute(params);	
	}
	 @Override
	 public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.orderinfo_menu, menu);
		MenuItem cancel_item = menu.findItem(R.id.cancel);
		MenuItem capture_item = menu.findItem(R.id.capture);
		cancel_item.setVisible(can_cancel);
		capture_item.setVisible(can_capture);
        super.onCreateOptionsMenu(menu, inflater);		
	}
	 
		public void onPreExecute(){		
			((ActivityLoadInterface)getActivity()).showProgressBar();
		}

    @Override
		 public void doPostExecute(Object result) {		
			((ActivityLoadInterface)getActivity()).hideProgressBar();
			if(result instanceof HashMap) {			
				HashMap map = (HashMap) result;
				FillData(map);
			} else {
				Refresh();
			}
		 }

		 public void RequestFailed(String error) {
			((ActivityLoadInterface)getActivity()).ShowMessage(error);
			((ActivityLoadInterface)getActivity()).hideProgressBar();
			Intent Login = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(Login);
			getActivity().finish();
		 }

		public void FillData(HashMap invoice) {

			Bundle params = new Bundle();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();

			((ActivityInfoInterface)getActivity()).setOrderIncrementId(invoice.get("order_increment_id").toString());
			/* Items */
			params = new Bundle();
			ArrayList<HashMap> items_array = new ArrayList<HashMap>();
			Object[] items = (Object[]) invoice.get("items");
			for (Object item : items) {
				HashMap item_data = (HashMap) item;
				items_array.add(item_data);
			}
			params.putSerializable("items", items_array);
			Fragment items_card =  new ItemsFragment();
			items_card.setArguments(params);
			mFragmentTransaction.add(R.id.items_card, items_card);
			/* Items */
			/* Totals */
			params = new Bundle();
			Object[] totals = (Object[]) invoice.get("totals");
			params.putSerializable("totals", totals);
			Fragment totals_card = new Fragment();
			totals_card = new TotalsFragment();
			totals_card.setArguments(params);
			mFragmentTransaction.add(R.id.totals_card, totals_card);
			/* Totals */
			mFragmentTransaction.commit();

            /* Set Comments*/
            Object[] invoice_comments = (Object[]) invoice.get("comments");
            ((ActivityInfoInterface)getActivity()).setComments(invoice_comments);
            /* Set Comments*/

			/*Options Menu*/
			can_cancel = (invoice.get("can_cancel").toString().equals("1"));
			can_capture = (invoice.get("can_capture").toString().equals("1"));
			getActivity().invalidateOptionsMenu();			
			/*Options Menu*/

		}	
		
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Vector<HashMap<String, String>> params = new Vector<HashMap<String, String>>();
        RequestTask task;
        HashMap<String, String> map_filter = new HashMap<String, String>();

        switch (item.getItemId()) {

            case R.id.cancel:
                map_filter.put("invoiceIncrementId", invoice_increment_id);
                params.add(map_filter);
                task = new RequestTask(this, getActivity(),"sales_order_invoice.cancel");
                task.execute(params);
                ShowMessage("The Invoice #"+ invoice_increment_id +" has been cancelled.");
                return true;

            case R.id.capture:
                map_filter.put("invoiceIncrementId", invoice_increment_id);
                params.add(map_filter);
                task = new RequestTask(this, getActivity(),"sales_order_invoice.capture");
                task.execute(params);
                ((ActivityLoadInterface) getActivity()).ShowMessage("The Invoice #" + invoice_increment_id + " has been captured.");
                return true;

				
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void ShowMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }	
	 
}
