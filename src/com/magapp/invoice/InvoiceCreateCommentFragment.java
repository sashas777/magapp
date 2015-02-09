package com.magapp.invoice;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.magapp.main.R;

import java.util.HashMap;


public class InvoiceCreateCommentFragment extends Fragment  {
    public View rootView;
    private String order_increment_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView  = inflater.inflate(R.layout.invoice_create_comment_form, null);
        order_increment_id=((InvoiceOrderActivity)getActivity()).GetOrderIncrementId();


        return rootView;
    }


}