package com.magapp.order;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magapp.R;

public class CustomerAccountFragment  extends Fragment {  
	
	public View rootView;
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.fragment_account_info, null);
			String customer_name = getArguments().getString("customer_name");
			String customer_email=getArguments().getString("customer_email");
			String customer_group=getArguments().getString("customer_group");
					 

			TextView CustomerGroup = (TextView) rootView.findViewById(R.id.customer_group);
			TextView CustomerName = (TextView) rootView.findViewById(R.id.customer_name);
			TextView Email = (TextView) rootView.findViewById(R.id.customer_email);		
			
			CustomerName.setText(customer_name);
			Email.setText(customer_email);	
			CustomerGroup.setText(customer_group);	 
	 		
			return rootView;
	}
	 
}
