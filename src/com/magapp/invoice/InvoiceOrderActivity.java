package com.magapp.invoice;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;
import com.magapp.order.OrderInfoFragment;

/**
 * Created by Sashas on 10/11/2014.
 */
public class InvoiceOrderActivity extends Activity  {

    private int order_id;
    private String order_increment_id;
    private Bundle order_items=new Bundle();

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
        Fragment screen = new InvoiceOrderFragment();
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
}