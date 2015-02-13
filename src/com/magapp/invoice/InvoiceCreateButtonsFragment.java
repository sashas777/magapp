package com.magapp.invoice;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.main.R;

import java.util.HashMap;
import java.util.Vector;


public class InvoiceCreateButtonsFragment extends Fragment  implements View.OnClickListener, RequestInterface {
    public View rootView;
    private String order_increment_id,api_point;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        rootView  = inflater.inflate(R.layout.invoice_create_buttons, null);

        /* @todo put params from activity->frament->here*/
        order_increment_id="";
        api_point="sales_order_invoice.create";
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
        String comment = ((InvoiceCreateActivity)getActivity()).getComment();
        /*Items*/
        HashMap itemsQty=((InvoiceCreateActivity)getActivity()).GetInvoiceIdQty();
        /*Items*/

        Vector params = new Vector();
        RequestTask task;
        HashMap map_order = new HashMap();

        order_increment_id=((InvoiceCreateActivity)getActivity()).GetOrderIncrementId();

        map_order.put("orderIncrementId", order_increment_id);

        params.add(map_order);
        params.add(itemsQty);
        params.add(comment);
        params.add((email) ? 1 : 0);
        params.add((add_comment) ? 1 : 0);

        task = new RequestTask(this, getActivity(),api_point);
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
        ((InvoiceCreateActivity)getActivity()).showProgressBar();
    };

    @Override
    public void doPostExecute(Object result) {
        ((InvoiceCreateActivity)getActivity()).hideProgressBar();
        ShowMessage("Invoice #"+result.toString()+" has been created");
        Intent OrderInfo = new Intent(getActivity(), InvoiceInfoActivity.class);
        OrderInfo.putExtra("increment_id", result.toString());
        NavUtils.navigateUpTo(getActivity(), OrderInfo);
    }


    public void RequestFailed(String error) {
        ((InvoiceCreateActivity)getActivity()).hideProgressBar();
        ShowMessage(error);
    }

    public void ShowMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }


}