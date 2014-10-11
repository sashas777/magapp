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
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.Toast;
import com.magapp.main.R;
import com.magapp.main.SalesActivity;
import com.magapp.order.OrderInfoFragment;

public class InvoiceInfoActivity extends Activity implements OnNavigationListener {

	private int order_id,invoice_incerement;
	 
	private TableLayout prodlist;
	 
	String[] actions = new String[] { "Invoice", "Comments" };
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
		order_id = vars.getInt("order_id");
        invoice_incerement= vars.getInt("increment_id");
		FragmentManager fragmentManager = getFragmentManager();
		Fragment screen = new OrderInfoFragment();	
		fragmentManager.beginTransaction().replace(R.id.container, screen)
		.addToBackStack(null).commit();		
	}
 
	public int GetOrderId(){
		return order_id;
	}

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
            ShowMessage("Coming Soon");
			break;
		}
		
		fragmentManager.beginTransaction().replace(R.id.container, screen)
		.addToBackStack(null).commit();		

		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, SalesActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void ShowMessage(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
 
}
