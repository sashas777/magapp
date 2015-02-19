/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.creditmemo;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.interfaces.ActivityInfoInterface;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.LoginActivity;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;
import com.magapp.order.CommentsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class CreditMemoInfoActivity extends Activity implements OnNavigationListener, ActivityLoadInterface, ActivityInfoInterface, RequestInterface {

    private String creditmemo_increment_id;
    private String order_increment_id, api_point;
    private ArrayList<HashMap> comments;
    private HashMap creditmemo_info;
    private Boolean can_invoice=false;

    String[] actions = new String[]{"Credit Memo", "Comments"};
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
        creditmemo_increment_id = vars.getString("increment_id");

        api_point="magapp_sales_order_creditmemo.info";
        Refresh();
    }

    public void Refresh() {
        Vector<HashMap<String, String>> params = new Vector<HashMap<String, String>>();
        RequestTask task;
        HashMap<String, String> map_filter = new HashMap<String, String>();
        map_filter.put("increment_id", creditmemo_increment_id);
        params.add(map_filter);
        task = new RequestTask(this, this,api_point);
        task.execute(params);
    }

    public void FillData(HashMap creditmemo) {

        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new CreditMemoInfoFragment();
        Bundle params=new Bundle();
        params.putString("increment_id", creditmemo_increment_id);
        params.putString("api_point",api_point);
        creditmemo_info=creditmemo;
        params.putSerializable("item", creditmemo);

        Object[] creditmemo_comments = (Object[]) creditmemo.get("comments");
        setComments(creditmemo_comments);

        can_invoice=(creditmemo.get("can_invoice").toString().equals("1"));
        invalidateOptionsMenu();

        screen.setArguments(params);
        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("creditmemo_info_activity").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.orderinfo_menu, menu);
        MenuItem cancel = menu.findItem(R.id.cancel);
        cancel.setVisible(can_invoice);
        return super.onCreateOptionsMenu(menu);
    }

    /*For back action to order view*/
    public void setOrderIncrementId(String order_increment_id_val) {
        order_increment_id = order_increment_id_val;
    }

    /*For comments?*/
    public void setComments(Object[] comments_obj){
        comments = new ArrayList<HashMap>();
        for (Object comment_item : comments_obj) {
            HashMap item_data = (HashMap) comment_item;
            comments.add(item_data);
        }
    }

    public ArrayList  getComments(){ return comments;}

    /* Additional for actionbar */
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (menu_id == -1) {
            menu_id = 0;
            return false;
        }
        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new Fragment();
        Bundle params=new Bundle();

        switch (itemPosition) {
            case 0:
                screen = new CreditMemoInfoFragment();
                params.putString("increment_id", creditmemo_increment_id);
                params.putString("api_point","magapp_sales_order_shipment.info");
                params.putSerializable("item", creditmemo_info);
                screen.setArguments(params);
                break;

            case 1:
                screen = new CommentsFragment();
                params.putString("status",  "");
                params.putString("increment_id", creditmemo_increment_id);
                params.putString("api_point","order_creditmemo.addComment");
                params.putSerializable("comments", comments);
                screen.setArguments(params);
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("creditmemo_info_activity").commit();

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle params;
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent OrderInfo = new Intent(this, OrderInfoActivity.class);
                OrderInfo.putExtra("order_increment_id", order_increment_id);
                NavUtils.navigateUpTo(this, OrderInfo);
                return true;

            case R.id.cancel:
                Vector<HashMap<String, String>> task_params = new Vector<HashMap<String, String>>();
                HashMap<String, String> map_filter = new HashMap<String, String>();
                map_filter.put("creditmemoIncrementId", creditmemo_increment_id);
                task_params.add(map_filter);
                RequestTask task = new RequestTask(this, this,"order_creditmemo.cancel");
                task.execute(task_params);
                ShowMessage("The Order #"+ order_increment_id +" has been released from holding status.");
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


    public void onPreExecute() {
        showProgressBar();
    }

    @Override
    public void doPostExecute(Object result) {
        hideProgressBar();
        if(result instanceof HashMap) {
            HashMap map = (HashMap) result;
            FillData(map);
        }else {
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
