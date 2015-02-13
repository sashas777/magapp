
/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.shipment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.magapp.invoice.InvoiceCreateCommentFragment;
import com.magapp.invoice.InvoiceCreateFragment;
import com.magapp.main.ActivityLoadInterface;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;

import java.util.HashMap;


public class ShipmentCreateActivity extends Activity implements InvoiceCreateCommentFragment.CommentListener, ActivityLoadInterface {

    private int order_id;
    private String order_increment_id;
    private Bundle order_items=new Bundle();
    private String comment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderinfo);

        Bundle vars = getIntent().getExtras();
        order_id = vars.getInt("order_id");
        order_increment_id = vars.getString("order_increment_id");

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new InvoiceCreateFragment();
        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("invoice_create_activity").commit();
    }

    public String GetOrderIncrementId(){
        return order_increment_id;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent OrderInfo = new Intent(this, OrderInfoActivity.class);
                OrderInfo.putExtra("order_increment_id", order_increment_id);
                NavUtils.navigateUpTo(this, OrderInfo);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCommentEditTextChanged(String string) {
        comment=string;
    }

    public String getComment(){
        return comment;
    }

    public HashMap GetInvoiceIdQty(){
        HashMap<String, String> hashMap= new HashMap<String, String>();
        LinearLayout list = (LinearLayout)findViewById(R.id.items_list);
        for(int i=0; i<((ViewGroup)list).getChildCount(); ++i) {
            View nextChild = ((ViewGroup)list).getChildAt(i);
            String order_item_qty=((TextView) nextChild.findViewById(R.id.order_item_qty)).getText().toString();
            String order_item_id=((TextView)nextChild.findViewById(R.id.order_item_id)).getText().toString();
            hashMap.put(order_item_id,order_item_qty);
        }
        return hashMap;
    }

    public void ShowMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar(){
        LinearLayout Progress =(LinearLayout) findViewById(R.id.linlaHeaderProgress);
        Progress.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        LinearLayout Progress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        Progress.setVisibility(View.GONE);
    }
}