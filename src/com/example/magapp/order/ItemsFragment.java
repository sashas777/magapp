package com.example.magapp.order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.magapp.R;
import com.example.magapp.imageLoader.ImageLoader;

public class ItemsFragment extends Fragment {

	public View rootView;
 
	public ImageLoader imageLoader; 
	   
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_items_info, null);
		ArrayList items = getArguments().getParcelableArrayList("items");
		//ListView items_listview=(ListView) rootView.findViewById(R.id.items_list);
		 
		TableLayout ItemsTable = (TableLayout) rootView.findViewById(R.id.items_list);
		for (Object item : items) {
		HashMap item_data = (HashMap) item;
	 
		View tr = inflater.inflate(R.layout.order_item_view, null,false);
	
		ImageView item_image=(ImageView)tr.findViewById(R.id.list_image);
		imageLoader=new ImageLoader(getActivity().getApplicationContext());
		imageLoader.DisplayImage(item_data.get("image").toString(), item_image);
 
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
			 
		ItemsTable.addView(tr);				 		 
		//Log.e("Sashas", item_data.get("sku").toString());
	 
		}
	 
	 
		return rootView;
	}
	
	 
	  
}
