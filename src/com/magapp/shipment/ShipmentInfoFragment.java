/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.shipment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.interfaces.ActivityInfoInterface;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.LoginActivity;
import com.magapp.main.R;
import com.magapp.order.ItemsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class ShipmentInfoFragment extends Fragment implements RequestInterface {


    public View rootView;
    String shipment_increment_id,api_point;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.order_info_fragment, null);

        shipment_increment_id=getArguments().getString("increment_id");
        api_point=getArguments().getString("api_point");

        Refresh();
        setHasOptionsMenu(true);
        return rootView;
    }

    public void Refresh() {
        Vector params = new Vector();
        RequestTask task;
        HashMap map_filter = new HashMap();
        map_filter.put("shipmentIncrementId", shipment_increment_id);
        params.add(map_filter);

        task = new RequestTask(this, getActivity(), api_point);
        task.execute(params);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.orderinfo_menu, menu);
        MenuItem add_track_item = menu.findItem(R.id.add_track);
        add_track_item.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onPreExecute() {
        ((ActivityLoadInterface) getActivity()).showProgressBar();
    }

    ;

    @Override
    public void doPostExecute(Object result) {
        ((ActivityLoadInterface) getActivity()).hideProgressBar();
        if (result instanceof HashMap) {
            HashMap map = (HashMap) result;
            FillData(map);
        } else {
            Refresh();
        }
    }

    public void RequestFailed(String error) {
        ((ActivityLoadInterface) getActivity()).ShowMessage(error);
        ((ActivityLoadInterface) getActivity()).hideProgressBar();
        Intent Login = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(Login);
        getActivity().finish();
    }

    public void FillData(HashMap shipment) {

        Bundle params;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();


        ((ActivityInfoInterface) getActivity()).setOrderIncrementId(shipment.get("order_increment_id").toString());
            /* Items */
        params = new Bundle();
        ArrayList<HashMap> items_array = new ArrayList<HashMap>();
        Object[] items = (Object[]) shipment.get("items");
        for (Object item : items) {
            HashMap item_data = (HashMap) item;
            items_array.add(item_data);
        }
        params.putSerializable("items", items_array);
        Fragment items_card =  new ItemsFragment();
        items_card.setArguments(params);
        mFragmentTransaction.add(R.id.items_card, items_card);
			/* Items */
			/* Trackings */
            params = new Bundle();
            ArrayList<HashMap> trackings_array = new ArrayList<HashMap>();
            Object[] tracking_items = (Object[]) shipment.get("tracks");
            for (Object track_item : tracking_items) {
                HashMap track_item_data = (HashMap) track_item;
                trackings_array.add(track_item_data);
            }
            params.putSerializable("items", trackings_array);
            params.putString("shipment_increment_id", shipment_increment_id);
            Fragment trackings_card =  new TrackingsFragment();
            trackings_card.setArguments(params);
            mFragmentTransaction.add(R.id.totals_card, trackings_card);
			/* Trackings */
        mFragmentTransaction.commit();

        /* Set Comments*/
            Object[] shipment_comments = (Object[]) shipment.get("comments");
            /*@todo redo params*/
            ((ActivityInfoInterface) getActivity()).setComments(shipment_comments);
        /* Set Comments*/

		/*Options Menu*/
        getActivity().invalidateOptionsMenu();
		/*Options Menu*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Vector<HashMap<String, String>> params = new Vector<HashMap<String, String>>();
        RequestTask task;
        HashMap<String, String> map_filter = new HashMap<String, String>();

        switch (item.getItemId()) {

            case R.id.add_track:
              /*  map_filter.put("invoiceIncrementId", invoice_increment_id);
                params.add(map_filter);
                task = new RequestTask(this, getActivity(), "sales_order_invoice.cancel");
                task.execute(params);*/
                ((ActivityLoadInterface) getActivity()).ShowMessage("Tracking number for Shipment #" + shipment_increment_id + " has been added.");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }



}
