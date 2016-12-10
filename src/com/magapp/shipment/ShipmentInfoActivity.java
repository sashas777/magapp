/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.shipment;

import android.app.*;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.magapp.analytics.AnalyticsApplication;
import com.magapp.interfaces.ActivityInfoInterface;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.R;
import com.magapp.order.CommentsFragment;
import com.magapp.order.OrderInfoActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ShipmentInfoActivity extends Activity implements OnNavigationListener, ActivityLoadInterface, ActivityInfoInterface {

    private String shipment_increment_id;
    private String order_increment_id;
    private ArrayList<HashMap> comments;

    String[] actions = new String[]{"Shipment", "Comments"};
    private CharSequence mTitle;
    public Integer menu_id = -1;
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderinfo);
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter mSpinnerAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_spinner_row_white, actions);
        getActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle vars = getIntent().getExtras();
        shipment_increment_id = vars.getString("increment_id");
        String tracking_number = vars.getString("tracking_number");


        FragmentManager fragmentManager = getFragmentManager();
        Bundle params = new Bundle();
        Fragment screen;

        if (tracking_number != null) {
            params.putString("tracking_number", tracking_number);

            if (vars.getString("carrier") != null)
                params.putString("carrier", vars.getString("carrier"));

            if (vars.getString("carrier_title") != null)
                params.putString("carrier_title", vars.getString("carrier_title"));

            if (vars.getString("notify_customer") != null)
                params.putString("notify_customer", vars.getString("notify_customer"));

            screen = new AddTrackingFragment();
            mTracker.setScreenName("AddTrackingFragment");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } else {
            mTracker.setScreenName("ShipmentInfoFragment");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            screen = new ShipmentInfoFragment();
        }


        params.putString("increment_id", shipment_increment_id);
        params.putString("api_point", "magapp_sales_order_shipment.info");
        screen.setArguments(params);

        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("shipment_info_activity").commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.orderinfo_menu, menu);
        MenuItem add_track_item = menu.findItem(R.id.add_track);
        add_track_item.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    /*For back action to order view*/
    public void setOrderIncrementId(String order_increment_id_val) {
        order_increment_id = order_increment_id_val;
    }

    /*For comments?*/
    public void setComments(Object[] comments_obj) {
        comments = new ArrayList<HashMap>();
        for (Object comment_item : comments_obj) {
            HashMap item_data = (HashMap) comment_item;
            comments.add(item_data);
        }
    }

    public ArrayList getComments() {
        return comments;
    }

    /* Additional for actionbar */
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (menu_id == -1) {
            menu_id = 0;
            return false;
        }
        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new Fragment();
        Bundle params = new Bundle();

        switch (itemPosition) {
            case 0:
                mTracker.setScreenName("ShipmentInfoFragment");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                screen = new ShipmentInfoFragment();
                params.putString("increment_id", shipment_increment_id);
                params.putString("api_point", "magapp_sales_order_shipment.info");
                screen.setArguments(params);
                break;

            case 1:
                mTracker.setScreenName("CommentsFragment");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                screen = new CommentsFragment();
                params.putString("status", "");
                params.putString("increment_id", shipment_increment_id);
                params.putString("api_point", "sales_order_shipment.addComment");
                params.putSerializable("comments", comments);
                screen.setArguments(params);
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("shipment_info_activity").commit();

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle params;
        switch (item.getItemId()) {
            case android.R.id.home:
                mTracker.setScreenName("OrderInfoActivity");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                Intent OrderInfo = new Intent(this, OrderInfoActivity.class);
                OrderInfo.putExtra("order_increment_id", order_increment_id);
                NavUtils.navigateUpTo(this, OrderInfo);
                return true;

            case R.id.add_track:
                mTracker.setScreenName("AddTrackingFragment");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                params = new Bundle();
                params.putString("increment_id", shipment_increment_id);
                Fragment add_tracking_fragment = new AddTrackingFragment();
                add_tracking_fragment.setArguments(params);
                FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();

                mFragmentTransaction.replace(R.id.container, add_tracking_fragment).addToBackStack("shipment_info_activity").commit();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void ShowMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
    }

}
