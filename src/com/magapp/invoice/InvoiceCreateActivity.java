/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.invoice;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.magapp.analytics.AnalyticsApplication;
import com.magapp.interfaces.ActivityCreateInterface;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.R;
import com.magapp.order.OrderInfoActivity;

import java.util.HashMap;

/**
 * Created by Sashas on 10/11/2014.
 */
public class InvoiceCreateActivity extends Activity implements InvoiceCreateCommentFragment.CommentListener, ActivityLoadInterface, ActivityCreateInterface {

    private int order_id;
    private String order_increment_id;
    private Bundle order_items = new Bundle();
    private String comment;
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderinfo);
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]
        Bundle vars = getIntent().getExtras();
        order_id = vars.getInt("order_id");
        order_increment_id = vars.getString("order_increment_id");

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new InvoiceCreateFragment();

        Bundle params = new Bundle();
        params.putString("order_increment_id", order_increment_id);
        params.putString("api_point", "sales_order_invoice.create");
        screen.setArguments(params);

        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("invoice_create_activity").commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mTracker.setScreenName("OrderInfoActivity");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                Intent OrderInfo = new Intent(this, OrderInfoActivity.class);
                OrderInfo.putExtra("order_increment_id", order_increment_id);
                NavUtils.navigateUpTo(this, OrderInfo);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param string
     */
    @Override
    public void onCommentEditTextChanged(String string) {
        comment = string;
    }

    public String getComment() {
        return comment;
    }

    public HashMap GetOrderItemsIdQty() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        LinearLayout list = (LinearLayout) findViewById(R.id.items_list);
        for (int i = 0; i < list.getChildCount(); ++i) {
            View nextChild = list.getChildAt(i);
            String order_item_qty = ((TextView) nextChild.findViewById(R.id.order_item_qty)).getText().toString();
            String order_item_id = ((TextView) nextChild.findViewById(R.id.order_item_id)).getText().toString();
            hashMap.put(order_item_id, order_item_qty);
        }
        return hashMap;
    }

    @Override
    public void ShowSuccess(String increment_id) {
        ShowMessage("Invoice #" + increment_id + " has been created");
        mTracker.setScreenName("OrderInfoActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        Intent InvoiceInfo = new Intent(this, InvoiceInfoActivity.class);
        InvoiceInfo.putExtra("increment_id", increment_id);
        NavUtils.navigateUpTo(this, InvoiceInfo);
    }

    public void showProgressBar() {
        LinearLayout Progress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        Progress.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        LinearLayout Progress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        Progress.setVisibility(View.GONE);
    }

    public void ShowMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}