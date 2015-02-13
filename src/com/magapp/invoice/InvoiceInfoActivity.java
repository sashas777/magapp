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
import android.widget.*;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;

public class InvoiceInfoActivity extends Activity implements OnNavigationListener {

    private int order_id;
    private String invoice_increment_id;
    private String order_increment_id;
    private TableLayout prodlist;

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
        order_id = vars.getInt("order_id");
        invoice_increment_id = vars.getString("increment_id");
        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new InvoiceInfoFragment();
        fragmentManager.beginTransaction().replace(R.id.container, screen).addToBackStack(null).commit();

    }

    public String GetInvoiceIncrementId() {
        return invoice_increment_id;
    }

    public void setOrderIncrementId(String order_increment_id_val) {
        order_increment_id = order_increment_id_val;
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
                screen = new InvoiceInfoFragment();
                break;

            case 1:
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
