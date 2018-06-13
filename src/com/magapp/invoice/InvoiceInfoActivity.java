/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2018 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.invoice;

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

import com.magapp.interfaces.ActivityInfoInterface;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.R;
import com.magapp.order.CommentsFragment;
import com.magapp.order.OrderInfoActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class InvoiceInfoActivity extends Activity implements OnNavigationListener, ActivityLoadInterface, ActivityInfoInterface {

    // private int order_id;
    private String invoice_increment_id;
    private String order_increment_id;
    private ArrayList<HashMap> comments;

    String[] actions = new String[]{"Invoice", "Comments"};
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
        //  order_id = vars.getInt("order_id");
        invoice_increment_id = vars.getString("increment_id");
        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new InvoiceInfoFragment();
        Bundle params = new Bundle();
        params.putString("increment_id", invoice_increment_id);
        params.putString("api_point", "magapp_sales_order_invoice.info");
        screen.setArguments(params);

        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("invoice_info_activity").commit();

    }


    public void setOrderIncrementId(String order_increment_id_val) {
        order_increment_id = order_increment_id_val;
    }

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

                screen = new InvoiceInfoFragment();
                params.putString("increment_id", invoice_increment_id);
                params.putString("api_point", "magapp_sales_order_invoice.info");
                screen.setArguments(params);
                break;

            case 1:
                screen = new CommentsFragment();

                params.putString("status", "");
                params.putString("increment_id", invoice_increment_id);
                params.putString("api_point", "sales_order_invoice.addComment");
                params.putSerializable("comments", comments);
                screen.setArguments(params);
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack(null).commit();

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

    public void showProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
    }

}
