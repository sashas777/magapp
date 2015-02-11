package com.magapp.invoice;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.main.LoginActivity;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;
import com.magapp.order.ItemsFragment;
import com.magapp.order.TotalsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class InvoiceCreateButtonsFragment extends Fragment  implements View.OnClickListener, RequestInterface {
    public View rootView;
    private String order_increment_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        rootView  = inflater.inflate(R.layout.invoice_create_buttons, null);

        order_increment_id=((InvoiceOrderActivity)getActivity()).GetOrderIncrementId();
        Button submitBtn = (Button) rootView.findViewById(R.id.submit);
        submitBtn.setOnClickListener(this);


        return rootView;
    }

    public void SubmitInvoice() {
        /* Add Comment*/
        boolean add_comment=((CheckBox) rootView.findViewById(R.id.comments)).isChecked();
        /*Email*/
        boolean email=((CheckBox) rootView.findViewById(R.id.email)).isChecked();
        /*Get comment Value*/
        String comment = ((InvoiceOrderActivity)getActivity()).getComment();
        /*Items*/
        HashMap itemsQty=((InvoiceOrderActivity)getActivity()).GetInvoiceIdQty();
        /*Items*/

        Vector params = new Vector();
        RequestTask task;
        HashMap map_order = new HashMap();

        order_increment_id=((InvoiceOrderActivity)getActivity()).GetOrderIncrementId();

        map_order.put("orderIncrementId", order_increment_id);

        params.add(map_order);
        params.add(itemsQty);
        params.add(comment);
        params.add((email) ? 1 : 0);
        params.add((add_comment) ? 1 : 0);

        task = new RequestTask(this, getActivity());
        task.execute(params);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                SubmitInvoice();
                break;
        }
    }


    public void onPreExecute(){
        ((InvoiceOrderActivity)getActivity()).showProgressBar();
    };

    @Override
    public void doPostExecute(Object result) {
        ((InvoiceOrderActivity)getActivity()).hideProgressBar();
        ShowMessage("Invoice #"+result.toString()+" has been created");
        Intent OrderInfo = new Intent(getActivity(), OrderInfoActivity.class);
        OrderInfo.putExtra("order_increment_id", order_increment_id);
        NavUtils.navigateUpTo(getActivity(), OrderInfo);
    }

    public  String GetApiRoute() {
        return "sales_order_invoice.create";
    }

    public void RequestFailed(String error) {
        ((InvoiceOrderActivity)getActivity()).hideProgressBar();
        ShowMessage(error);
    }

    public void ShowMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }


}