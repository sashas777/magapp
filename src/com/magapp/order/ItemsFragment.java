package com.magapp.order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.magapp.main.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ItemsFragment extends ListFragment {

	public View rootView;
	private ArrayList<HashMap<String, String>> OrderItemsList;
	private OrderItemsListAdapter adapter;
	// public ImageLoader imageLoader; 
	   
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_items_info, null);

		OrderItemsList = new ArrayList<HashMap<String, String>>();

		/* Items list */
		adapter=new OrderItemsListAdapter(getActivity(), OrderItemsList);
		setListAdapter(adapter);

		/* Items list */

		ArrayList items = getArguments().getParcelableArrayList("items");
		//ListView items_listview=(ListView) rootView.findViewById(R.id.items_list);
		 
		TableLayout ItemsTable = (TableLayout) rootView.findViewById(R.id.items_list);
		for (Object item : items) {
		HashMap item_data = (HashMap) item;
	 
		View tr = inflater.inflate(R.layout.order_item_view, null,false);

		/* New View view  Item list*/
			String Order_Line_1="Order #"+" | ";
			String Order_Line_2="By: ";
			String Order_Line_3="Status: "+" | Grand Total: ";

			HashMap<String, String> list_map = new HashMap<String, String>();

			list_map.put("invoice_number",Order_Line_1);
			list_map.put("description",Order_Line_2);
			//list_map.put("increment_id",increment_id);
			list_map.put("line3",Order_Line_3);
			OrderItemsList.add(list_map);

		/* New View view  Item list*/
	/*	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext()).build();
		 ImageLoader.getInstance().init(config);
		ImageView item_image=(ImageView)tr.findViewById(R.id.list_image);
		//imageLoader=new ImageLoader(getActivity().getApplicationContext());
		ImageLoader.getInstance().displayImage(item_data.get("image").toString(), item_image);
		//ImageLoader.getInstance().displayImage("http://placehold.it/240x240", item_image);
		 
		TextView ProductName = (TextView) tr.findViewById(R.id.ItemName);	
		TextView ItemInfo = (TextView) tr.findViewById(R.id.ItemInfo);	
		TextView Price = (TextView) tr.findViewById(R.id.Price);
		TextView Options = (TextView) tr.findViewById(R.id.Options);
		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
		NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.US);
		
		Integer qty_val=(int) Double.valueOf(item_data.get("qty_ordered").toString()).doubleValue();				
		ProductName.setText(item_data.get("name").toString());
		
		ItemInfo.setText("Sku: "+item_data.get("sku").toString()+"\n"+"Qty: "+qty_val.toString()+"\n"+"Price: "+format.format(Double.valueOf(item_data.get("price").toString()).doubleValue()));
		
		String PriceText=new String();		 		 
		percentFormat.setMaximumFractionDigits(3);
		//PriceText="Price: "+format.format(Double.valueOf(item_data.get("price").toString()).doubleValue())+"\n";

        if (item_data.get("tax_amount").toString()!=null && !item_data.get("tax_amount").toString().isEmpty())
		PriceText="Tax Amount: "+format.format(Double.valueOf(item_data.get("tax_amount").toString()).doubleValue())+"\n";
		//PriceText+="Tax Percent: "+percentFormat.format(Double.valueOf(item_data.get("tax_percent").toString()).doubleValue()/100)+"\n";
		if (Double.valueOf(item_data.get("discount_amount").toString()).doubleValue()!=0)
		PriceText+="Discount: "+format.format(Double.valueOf(item_data.get("discount_amount").toString()).doubleValue())+"\n";
		PriceText+="Total: "+format.format(Double.valueOf(item_data.get("row_total_incl_tax").toString()).doubleValue());
		//PriceText+="Item status: "+item_data.get("item_status").toString();
		Price.setText(PriceText);
		
	 
		Object[] item_options= (Object[]) item_data.get("selected_options");
		if (item_options!= null) {
			String item_option_text=new String();
			int el_itertator=1;
			for (Object item_option : item_options) {	
				if (item_options.length!=el_itertator)
					item_option_text+=item_option.toString()+"\n";
				else 
					item_option_text+=item_option.toString();
				el_itertator++;
			}
			Options.setText(item_option_text);
		}
			 
		ItemsTable.addView(tr);		*/
		//Log.e("Sashas", item_data.get("sku").toString());
	 
		}
	 
	 
		return rootView;
	}
	
	 
	  
}
