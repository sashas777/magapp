/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.creditmemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.magapp.interfaces.ActivityInfoInterface;
import com.magapp.main.R;
import com.magapp.order.ItemsFragment;
import com.magapp.order.TotalsFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class CreditMemoInfoFragment extends Fragment {


    public View rootView;
    String shipment_increment_id,api_point;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.order_info_fragment, null);

        shipment_increment_id=getArguments().getString("increment_id");
        api_point=getArguments().getString("api_point");
        HashMap item= (HashMap) getArguments().getSerializable("item");

        FillData(item);

        return rootView;
    }

    public void FillData(HashMap creditmemo) {

        Bundle params;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();


        ((ActivityInfoInterface) getActivity()).setOrderIncrementId(creditmemo.get("order_increment_id").toString());
            /* Items */
        params = new Bundle();
        ArrayList<HashMap> items_array = new ArrayList<HashMap>();
        Object[] items = (Object[]) creditmemo.get("items");
        for (Object item : items) {
            HashMap item_data = (HashMap) item;
            items_array.add(item_data);
        }
        params.putSerializable("items", items_array);
        Fragment items_card =  new ItemsFragment();
        items_card.setArguments(params);
        mFragmentTransaction.add(R.id.items_card, items_card);
		/* Totals */
        params = new Bundle();
        Object[] totals = (Object[]) creditmemo.get("totals");
        params.putSerializable("totals", totals);
        Fragment totals_card = new TotalsFragment();
        totals_card.setArguments(params);
        mFragmentTransaction.add(R.id.totals_card, totals_card);
		/* Totals */
        mFragmentTransaction.commit();
    }





}
