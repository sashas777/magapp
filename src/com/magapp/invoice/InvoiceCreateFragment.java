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

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magapp.common.ItemsFragment;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.LoginActivity;
import com.magapp.main.R;
import com.magapp.order.TotalsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Sashas on 10/12/2014.
 */
public class InvoiceCreateFragment extends Fragment implements RequestInterface {

    public View rootView;
    private String order_increment_id, api_point;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_info_fragment, null);

        Vector params = new Vector();
        RequestTask task;
        HashMap map_filter = new HashMap();

        order_increment_id = getArguments().getString("order_increment_id");
        api_point = getArguments().getString("api_point");

        map_filter.put("order_increment_id", order_increment_id);
        params.add(map_filter);
        task = new RequestTask(this, getActivity(), "magapp_sales_order.info");
        task.execute(params);
        return rootView;
    }

    public void onPreExecute() {
        ((ActivityLoadInterface) getActivity()).showProgressBar();
    }

    @Override
    public void doPostExecute(Object result, String result_api_point) {
        ((ActivityLoadInterface) getActivity()).hideProgressBar();
        HashMap map = (HashMap) result;
        FillData(map);
    }


    public void RequestFailed(String error) {
        ((ActivityLoadInterface) getActivity()).ShowMessage(error);
        ((ActivityLoadInterface) getActivity()).hideProgressBar();
        Intent Login = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(Login);
        getActivity().finish();
    }

    public void FillData(HashMap order) {
        Bundle params = new Bundle();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();

        /* Items */
        ArrayList<HashMap> items_array = new ArrayList<HashMap>();
        Object[] items = (Object[]) order.get("items");
        for (Object item : items) {
            HashMap item_data = (HashMap) item;
            items_array.add(item_data);
        }
        params.putSerializable("items", items_array);
        params.putString("api_point", api_point);

        Fragment items_card = new ItemsFragment();
        items_card.setArguments(params);
        mFragmentTransaction.add(R.id.main_info_card, items_card);
        /* Items */
			/* Totals */
        params = new Bundle();
        Object[] totals = (Object[]) order.get("totals");
        params.putSerializable("totals", totals);
        Fragment totals_card = new Fragment();
        totals_card = new TotalsFragment();
        totals_card.setArguments(params);
        mFragmentTransaction.add(R.id.account_info, totals_card);
		/* Totals */
        /*Comment*/
        Fragment comment_form = new InvoiceCreateCommentFragment();
        params = new Bundle();
        params.putString("order_increment_id", order_increment_id);
        comment_form.setArguments(params);
        mFragmentTransaction.add(R.id.bill_address, comment_form);
        /*Comment*/
        /*Buttons*/
        Fragment invoice_buttons = new InvoiceCreateButtonsFragment();
        params = new Bundle();
        params.putString("order_increment_id", order_increment_id);
        params.putString("api_point", api_point);
        invoice_buttons.setArguments(params);
        mFragmentTransaction.add(R.id.ship_address, invoice_buttons);
        /*Buttons*/


        mFragmentTransaction.commit();


    }
}