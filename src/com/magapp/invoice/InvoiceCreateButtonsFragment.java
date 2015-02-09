package com.magapp.invoice;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.main.LoginActivity;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;
import com.magapp.order.ItemsFragment;
import com.magapp.order.TotalsFragment;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class InvoiceCreateButtonsFragment extends Fragment  implements View.OnClickListener {
    public View rootView;
    private String order_increment_id;
    private LayoutInflater inf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        inf=inflater;
        rootView  = inflater.inflate(R.layout.invoice_create_buttons, null);
        order_increment_id=((InvoiceOrderActivity)getActivity()).GetOrderIncrementId();
        Button submitBtn = (Button) rootView.findViewById(R.id.submit);
        submitBtn.setOnClickListener(this);
        return rootView;
    }

    public void SubmitInvoice() {
        /* Add Comment*/
        boolean add_comment=((CheckBox) rootView.findViewById(R.id.comments)).isChecked();
        /*Email*/
        boolean email=((CheckBox) rootView.findViewById(R.id.email)).isChecked();

        /*Get comment Value*/
        View  comment_fragment =  inf.inflate(R.layout.invoice_create_comment_form, null);
        String comment = ((EditText)comment_fragment.findViewById(R.id.comment_value)).getText().toString();
        /*@todo comment not adding*/
        Log.e("Sashas", "add comment "+add_comment+" email "+email+" comment "+comment);
        /*Items*/
/*        ArrayList<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();
        listOfMaps=(ArrayList<Map<String, String>>)vars.getSerializable("order_items");
/*
        ArrayList<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();
        LinearLayout list = (LinearLayout)rootView.findViewById(R.id.items_list);
        for(int i=0; i<((ViewGroup)list).getChildCount(); ++i) {
            View nextChild = ((ViewGroup)list).getChildAt(i);
            String order_item_qty=((TextView) nextChild.findViewById(R.id.order_item_qty)).getText().toString();
            String order_item_id=((TextView)nextChild.findViewById(R.id.order_item_id)).getText().toString();
            HashMap<String, String> hashMap= new HashMap<String, String>();
            hashMap.put("order_item_qty",order_item_qty);
            hashMap.put("order_item_id",order_item_id);
            listOfMaps.add(hashMap);
        }
        InvoiceOrder.putExtra("order_items", listOfMaps);
*/
 /*Items*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                SubmitInvoice();
                break;

        }
    }
}