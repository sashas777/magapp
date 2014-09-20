package com.magapp.invoice;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.magapp.main.R;

public class InvoiceListFragment extends ListFragment    {  
	
	public View rootView;
	 

	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.invoice_list, null);
			 String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				        "Linux", "OS/2", "aa","sdd","ccc" };
				    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
				    setListAdapter(adapter);
			//getActiviy().getActionBar().setDisplayHomeAsUpEnabled(true);
			return rootView;
	}
	 
	 
	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	    // do something with the data
	  }	 
}

