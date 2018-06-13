/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2018 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.order;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.magapp.common.InvoiceListFragment;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.LoginActivity;
import com.magapp.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import de.timroes.axmlrpc.XMLRPCException;

public class OrderInfoActivity extends FragmentActivity implements OnNavigationListener, ActivityLoadInterface, RequestInterface {

    private String order_increment_id, status;
    private Integer order_id;
    private ArrayList<HashMap> comments;

    public Integer menu_id = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderinfo);

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> mSpinnerAdapter;
        mSpinnerAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.custom_spinner_row_white, new ArrayList<String>());

        getActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mSpinnerAdapter.add("Order");
        mSpinnerAdapter.add("Invoice");
        mSpinnerAdapter.add("Shipment");
        mSpinnerAdapter.add("Credit Memo");
        mSpinnerAdapter.add("Comments");
        mSpinnerAdapter.notifyDataSetChanged();

        Bundle vars = getIntent().getExtras();
        order_increment_id = vars.getString("order_increment_id");
        Refresh();


    }

    /* Execute all tasks when activity loads*/
    public void Refresh() {
        Vector<HashMap<String, String>> params = new Vector<HashMap<String, String>>();
        RequestTask task;
        HashMap<String, String> map_filter = new HashMap<String, String>();

        map_filter.put("order_increment_id", order_increment_id);
        params.add(map_filter);
        task = new RequestTask(this, this, "magapp_sales_order.info");
        task.execute(params);

    }


    /*@todo Get rid of it */
    public void setOrderId(Integer order_id_val) {
        order_id = order_id_val;
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

    public void setStatus(String status_val) {
        status = status_val;
    }

    public String getStatus() {
        return status;
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
                Refresh();
                break;
            case 1:
                screen = new InvoiceListFragment();
                params.putInt("order_id", order_id);
                params.putString("entity_name", "Invoice");
                params.putString("api_point", "sales_order_invoice.list");
                screen.setArguments(params);
                break;
            case 2:
                screen = new InvoiceListFragment();
                params.putInt("order_id", order_id);
                params.putString("entity_name", "Shipment");
                params.putString("api_point", "sales_order_shipment.list");
                screen.setArguments(params);
                break;
            case 3:
                screen = new InvoiceListFragment();
                params.putInt("order_id", order_id);
                params.putString("entity_name", "Credit Memo");
                params.putString("api_point", "order_creditmemo.list");
                screen.setArguments(params);
                break;
            case 4:
                screen = new CommentsFragment();
                params.putString("status", status);
                params.putString("increment_id", order_increment_id);
                params.putString("api_point", "sales_order.addComment");
                params.putSerializable("comments", comments);
                screen.setArguments(params);
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("order_info_activity").commit();

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Vector<HashMap<String, String>> params = new Vector<HashMap<String, String>>();
        RequestTask task;
        HashMap<String, String> map_filter = new HashMap<String, String>();

        switch (item.getItemId()) {

            case R.id.hold:
                map_filter.put("order_increment_id", order_increment_id);
                params.add(map_filter);
                task = new RequestTask(this, this, "sales_order.hold");
                task.execute(params);
                ShowMessage("The Order #" + order_increment_id + " has been put on hold.");
                return true;

            case R.id.unhold:
                map_filter.put("order_increment_id", order_increment_id);
                params.add(map_filter);
                task = new RequestTask(this, this, "sales_order.unhold");
                task.execute(params);
                ShowMessage("The Order #" + order_increment_id + " has been released from holding status.");
                return true;

            case R.id.cancel:
                map_filter.put("order_increment_id", order_increment_id);
                params.add(map_filter);
                task = new RequestTask(this, this, "sales_order.cancel");
                task.execute(params);
                ShowMessage("The Order #" + order_increment_id + " has been cancelled.");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void ShowMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar() {
        LinearLayout Progress = findViewById(R.id.linlaHeaderProgress);
        Progress.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        LinearLayout Progress = findViewById(R.id.linlaHeaderProgress);
        Progress.setVisibility(View.GONE);
    }

    public void onPreExecute() {
        showProgressBar();
    }

    @Override
    public void doPostExecute(Object result, String result_api_point) {
        hideProgressBar();
        Log.e("Sashas", "OrderInfoActivity::doPostExecute");
        if (result instanceof HashMap) {
            HashMap map = (HashMap) result;
            FragmentManager fragmentManager = getFragmentManager();
            Fragment screen = null;
            if (result_api_point.equals("magapp_sales_order.info")) {
                screen = new OrderInfoFragment();
                Bundle params = new Bundle();
                params.putSerializable("order", map);
                screen.setArguments(params);
            }

            fragmentManager.beginTransaction().replace(R.id.container, screen).commit();

        } else if (result instanceof XMLRPCException) {
            ShowMessage(((XMLRPCException) result).getMessage());
        } else {
            Refresh();
        }
    }


    public void RequestFailed(String error) {
        ShowMessage(error);
        hideProgressBar();
        Intent Login = new Intent(this, LoginActivity.class);
        startActivity(Login);
        finish();
    }


}
