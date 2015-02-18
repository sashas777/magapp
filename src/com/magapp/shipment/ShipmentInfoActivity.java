/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.shipment;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.invoice.InvoiceInfoFragment;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;
import com.magapp.order.CommentsFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ShipmentInfoActivity extends Activity implements OnNavigationListener, ActivityLoadInterface {

    private String shipment_increment_id;
    private String order_increment_id;
    private ArrayList<HashMap> comments;

    String[] actions = new String[]{"Shipment", "Comments"};
    private CharSequence mTitle;
    public Integer menu_id = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderinfo);

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter mSpinnerAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_spinner_row_white, actions);
        getActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle vars = getIntent().getExtras();
        shipment_increment_id = vars.getString("increment_id");
        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new InvoiceInfoFragment();

        Bundle params=new Bundle();
        params.putString("shipment_increment_id", shipment_increment_id);
        params.putString("api_point","magapp_sales_order_shipment.info");
        screen.setArguments(params);

        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("shipment_info_activity").commit();

    }

    public String GetInvoiceIncrementId() {
        return shipment_increment_id;
    }

    public void setOrderIncrementId(String order_increment_id_val) {
        shipment_increment_id = order_increment_id_val;
    }

    public void setComments(Object[] comments_obj){
        comments = new ArrayList<HashMap>();
        for (Object comment_item : comments_obj) {
            HashMap item_data = (HashMap) comment_item;
            comments.add(item_data);
        }
    }

    public ArrayList  getComments( ){ return comments;}

    /* Additional for actionbar */
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (menu_id == -1) {
            menu_id = 0;
            return false;
        }
        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new Fragment();
        switch (itemPosition) {
            case 0:
                screen = new ShipmentInfoFragment();
                break;

            case 1:
                screen = new CommentsFragment();
                Bundle params = new Bundle();
                params.putString("status",  "");
                params.putString("increment_id", shipment_increment_id);
                params.putString("api_point","sales_order_shipment.addComment");
                params.putSerializable("comments", comments);
                screen.setArguments(params);
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("shipment_info_activity").commit();

        return false;
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

    public void ShowMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar(){
        ProgressBar progressBar =(ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
    }	
	
}
