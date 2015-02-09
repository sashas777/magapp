package com.magapp.invoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;

/**
 * Created by Sashas on 10/11/2014.
 */
public class InvoiceOrderActivity extends Activity {

    private int order_id;
    private String increment_id;
    private Bundle order_items=new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_order_view);

        Bundle vars = getIntent().getExtras();
        order_id = vars.getInt("order_id");
        increment_id = vars.getString("increment_id");
        order_items= vars.getBundle("order_items");

        Log.e("Sashas", order_items.toString() );


        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent OrderInfo = new Intent(this, OrderInfoActivity.class);
                OrderInfo.putExtra("order_id", order_id);
                NavUtils.navigateUpTo(this, OrderInfo);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}