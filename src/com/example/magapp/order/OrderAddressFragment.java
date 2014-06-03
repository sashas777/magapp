package com.example.magapp.order;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.magapp.R;

public class OrderAddressFragment  extends Fragment {  
 	 
	public View rootView;
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {

		rootView  = inflater.inflate(R.layout.order_address, null);
		String order_address = getArguments().getString("order_address");
		String adress_title=getArguments().getString("address_title");
		TextView address_text = (TextView) rootView.findViewById(R.id.address_text);
		TextView card_title = (TextView) rootView.findViewById(R.id.card_title);
		address_text.setText(order_address);		
		card_title.setText(adress_title);
		
		return rootView;
	}
	 
	 
}
