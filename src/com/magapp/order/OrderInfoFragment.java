package com.magapp.order;

import java.util.*;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.widget.TextView;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.invoice.InvoiceOrderActivity;
import com.magapp.main.*;

public class OrderInfoFragment extends Fragment implements RequestInterface {


    public View rootView;
    private Menu menu_settings;
    private Boolean can_invoice=false, can_hold=false,can_unhold=false,can_creditmemo=false, can_ship=false, can_cancel=false;
    private Boolean can_comment=false;
    private String order_increment_id;
    private Integer order_id;
    private Bundle order_items = new Bundle();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.order_info_fragment, null);
        order_increment_id = ((OrderInfoActivity) getActivity()).GetOrderIncrementId();
        Vector params = new Vector();
        RequestTask task;
        HashMap map_filter = new HashMap();
        map_filter.put("order_increment_id", order_increment_id);
        params.add(map_filter);
        task = new RequestTask(this, getActivity());
        task.execute(params);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.orderinfo_menu, menu);
        MenuItem invoice_item = menu.findItem(R.id.invoice);
        MenuItem hold_item = menu.findItem(R.id.hold);
        MenuItem unhold_item = menu.findItem(R.id.unhold);
        MenuItem creditmemo_item = menu.findItem(R.id.creditmemo);
        MenuItem ship_item = menu.findItem(R.id.ship);
        MenuItem cancel_item = menu.findItem(R.id.cancel);

        invoice_item.setVisible(can_invoice);
        hold_item.setVisible(can_hold);
        unhold_item.setVisible(can_unhold);
        creditmemo_item.setVisible(can_creditmemo);
        ship_item.setVisible(can_ship);
        cancel_item.setVisible(can_cancel);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onPreExecute() {
        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
    }

    ;

    @Override
    public void doPostExecute(Object result) {
        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);

        HashMap map = (HashMap) result;
        FillData(map);
    }

    public String GetApiRoute() {
        return "magapp_sales_order.info";
    }

    public void RequestFailed(String error) {
        ((OrderInfoActivity) getActivity()).ShowMessage(error);
        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
        Intent Login = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(Login);
        getActivity().finish();
    }

    public void FillData(HashMap order) {

        Bundle params = new Bundle();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();

            /*Invoice Menu*/
        order_increment_id = order.get("increment_id").toString();
        order_id = Integer.parseInt(order.get("order_id").toString());
        ((OrderInfoActivity) getActivity()).setOrderId(order_id);
            /*Invoice Menu*/
            /* Main Info */
        params = new Bundle();
        String cardt_title = order.get("order_title").toString();
        params.putString("card_title", cardt_title);
        params.putString("ip", order.get("remote_ip").toString());
        params.putString("store_name", order.get("store_name").toString());

        String status = order.get("status").toString();
        status = status.substring(0, 1).toUpperCase(Locale.ENGLISH)
                + status.substring(1);
        params.putString("order_status", status);

        String created_at = cardt_title.substring(
                cardt_title.lastIndexOf("|") + 1, cardt_title.length());
        params.putString("created_at", created_at);
        Fragment main_info_card = new Fragment();
        main_info_card = new MainInfoFragment();
        main_info_card.setArguments(params);
        mFragmentTransaction.add(R.id.main_info_card, main_info_card);
			/* Main Info */
			/* Account Info */
        params = new Bundle();
        if (order.get("customer_is_guest").toString() == "1")
            params.putString("customer_name", "Guest");
        else
            params.putString("customer_name", order.get("customer_firstname")
                    .toString()
                    + " "
                    + order.get("customer_lastname").toString());
        params.putString("customer_group", order.get("customer_group_title")
                .toString());
        params.putString("customer_email", order.get("customer_email")
                .toString());
        Fragment account_card = new Fragment();
        account_card = new CustomerAccountFragment();
        account_card.setArguments(params);
        mFragmentTransaction.add(R.id.account_info, account_card);
			/* Account Info */
			/* Billing Address */
        params = new Bundle();
        Fragment billing_card = new Fragment();
        billing_card = new OrderAddressFragment();
        params.putString("order_address", order.get("billing_address_html")
                .toString());
        params.putString("address_title", "Billing Address");
        billing_card.setArguments(params);
        mFragmentTransaction.add(R.id.bill_address, billing_card);
			/* Billing Address */

			/* Shipping Address */
        if (Integer.parseInt(order.get("is_virtual").toString()) == 0) {

            Fragment shipping_card = new Fragment();
            shipping_card = new OrderAddressFragment();
            params = new Bundle();
            params.putString("order_address", order
                    .get("shipping_address_html").toString());
            params.putString("address_title", "Shipping Address");
            shipping_card.setArguments(params);
            mFragmentTransaction.add(R.id.ship_address, shipping_card);

        }
			/* Shipping Address */
			/* Payment Info */
        params = new Bundle();
        params.putString("payment_info", order.get("payment_method_text")
                .toString());
        params.putString("currency_info", order.get("payment_currency_text")
                .toString());
        Fragment payment_card = new Fragment();
        payment_card = new PaymentInfoFragment();
        payment_card.setArguments(params);
        mFragmentTransaction.add(R.id.payment_card, payment_card);
			/* Payment Info */

			/* Items */
        params = new Bundle();
        ArrayList<HashMap> items_array = new ArrayList<HashMap>();
        Object[] items = (Object[]) order.get("items");
        for (Object item : items) {
            HashMap item_data = (HashMap) item;
            items_array.add(item_data);
        }
        params.putSerializable("items", items_array);
            /* Invoice Menu */
        order_items.putSerializable("items", items_array);
            /*Invoice Menu*/
        Fragment items_card = new Fragment();
        items_card = new ItemsFragment();
        items_card.setArguments(params);
        mFragmentTransaction.add(R.id.items_card, items_card);
			/* Items */
			/* Totals */
        params = new Bundle();
        Object[] totals = (Object[]) order.get("totals");
        params.putSerializable("totals", totals);
        Fragment totals_card = new Fragment();
        totals_card = new TotalsFragment();
        totals_card.setArguments(params);
        mFragmentTransaction.add(R.id.totals_card, totals_card);
			/* Totals */
        mFragmentTransaction.commit();
			
			/*Menu Items*/
        can_invoice = (order.get("can_invoice").toString().equals("1"));
        can_hold = (order.get("can_hold").toString().equals("1"));
        can_unhold = (order.get("can_unhold").toString().equals("1"));
        can_ship = (order.get("can_ship").toString().equals("1"));
        can_creditmemo = (order.get("can_creditmemo").toString().equals("1"));
        can_comment = (order.get("can_comment").toString().equals("1"));
        can_cancel = (order.get("can_cancel").toString().equals("1"));
        getActivity().invalidateOptionsMenu();
			/*Menu Items*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), SalesListFragment.class));
                return true;

            case R.id.invoice:
                Intent InvoiceOrder = new Intent(getActivity(), InvoiceOrderActivity.class);
                InvoiceOrder.putExtra("order_id", order_id);
                InvoiceOrder.putExtra("order_items", order_items);
                InvoiceOrder.putExtra("order_increment_id", order_increment_id);

                getActivity().startActivity(InvoiceOrder);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
