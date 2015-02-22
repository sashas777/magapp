/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.order;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.magapp.common.InvoiceListFragment;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderInfoActivity extends Activity implements OnNavigationListener, ActivityLoadInterface {

	private String order_increment_id,status;
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

    public void Refresh(){
        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new OrderInfoFragment();
        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack("order_info_activity").commit();
    }

    /*@todo Get rid of it */
	public void  setOrderId(Integer order_id_val){
		order_id=order_id_val;
	}

    public void setComments(Object[] comments_obj){
        comments = new ArrayList<HashMap>();
        for (Object comment_item : comments_obj) {
            HashMap item_data = (HashMap) comment_item;
            comments.add(item_data);
        }
    }

    public ArrayList  getComments( ){ return comments;}

    public void setStatus(String status_val){
        status=status_val;
    }
    public String  getStatus( ){ return status;}

	public Integer GetOrderId(){
		return order_id;
	}

	public String GetOrderIncrementId(){
		return order_increment_id;
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
			screen = new OrderInfoFragment();		
			break;
		case 1:
			screen = new InvoiceListFragment();
            params.putInt("order_id", order_id);
            params.putString("entity_name", "Invoice");
            params.putString("api_point","sales_order_invoice.list");
            screen.setArguments(params);
			break;
        case 2:
            screen = new InvoiceListFragment();
            params.putInt("order_id", order_id);
            params.putString("entity_name", "Shipment");
            params.putString("api_point","sales_order_shipment.list");
            screen.setArguments(params);
            break;
        case 3:
            screen = new InvoiceListFragment();
            params.putInt("order_id", order_id);
            params.putString("entity_name", "Credit Memo");
            params.putString("api_point","order_creditmemo.list");
            screen.setArguments(params);
            break;
		case 4:
            screen = new CommentsFragment();
            params.putString("status", status);
            params.putString("increment_id", order_increment_id);
            params.putString("api_point","sales_order.addComment");
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
			/* Need to specify back in each fragment */
			default:
				return super.onOptionsItemSelected(item);
		}

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