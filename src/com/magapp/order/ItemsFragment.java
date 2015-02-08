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
import android.widget.*;

import com.magapp.main.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ItemsFragment extends  Fragment {

	public View rootView;
	private ArrayList<HashMap<String, String>> OrderItemsList;
	private OrderItemsListAdapter adapter;
	// public ImageLoader imageLoader; 
	   
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_items_info, null);

		OrderItemsList = new ArrayList<HashMap<String, String>>();

		/* Items list */
		LinearLayout list = (LinearLayout)rootView.findViewById(R.id.items_list);
		/* Items list */

		ArrayList items = getArguments().getParcelableArrayList("items");
		//ListView items_listview=(ListView) rootView.findViewById(R.id.items_list);
		 
	//	TableLayout ItemsTable = (TableLayout) rootView.findViewById(R.id.items_list);
		for (Object item : items) {
		HashMap item_data = (HashMap) item;
	 
		View tr = inflater.inflate(R.layout.order_item_view, null,false);

		/* New View view  Item list*/
			/*Qty*/
			Integer qty_val=(int) Double.valueOf(item_data.get("qty_ordered").toString()).doubleValue();
			/*Qty*/
			/*Options*/
			String item_option_text="";
			Object[] item_options= (Object[]) item_data.get("selected_options");
			if (item_options!= null) {
				item_option_text=new String();
				int el_itertator=1;
				for (Object item_option : item_options) {
					if (item_options.length!=el_itertator)
						item_option_text+=item_option.toString()+"\n";
					else
						item_option_text+=item_option.toString();
					el_itertator++;
				}
			}
			/*Options*/
			/*Price & PercentageFormats */
			NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
			/*Price & PercentageFormats */
			/* Totals */
			String Totals="";
			if (item_data.get("tax_amount").toString()!=null && !item_data.get("tax_amount").toString().isEmpty() && Double.valueOf(item_data.get("tax_amount").toString()).doubleValue()!=0)
				Totals="Tax Amount: "+format.format(Double.valueOf(item_data.get("tax_amount").toString()).doubleValue())+"\n";

			if (Double.valueOf(item_data.get("discount_amount").toString()).doubleValue()!=0)
				Totals+="Discount: "+format.format(Double.valueOf(item_data.get("discount_amount").toString()).doubleValue())+"\n";
			Totals+="Row Total: "+format.format(Double.valueOf(item_data.get("row_total_incl_tax").toString()).doubleValue());
			/* Totals */



			View vi = inflater.inflate(R.layout.order_info_item_view, null);
			((TextView) vi.findViewById(R.id.ProductName)).setText(item_data.get("name").toString());
			((TextView) vi.findViewById(R.id.ProductQty)).setText("Qty: "+qty_val.toString());
			((TextView) vi.findViewById(R.id.ProductSku)).setText(item_data.get("name").toString());
			/*Images */
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(vi.getContext()).build();
				ImageLoader.getInstance().init(config);
				ImageLoader.getInstance().displayImage(item_data.get("image").toString(), ((ImageView) vi.findViewById(R.id.ProductImage)));
			/*Images */

			((TextView) vi.findViewById(R.id.ProductPrice)).setText("Price: "+format.format(Double.valueOf(item_data.get("price").toString()).doubleValue()));
			((TextView) vi.findViewById(R.id.ProductTotals)).setText(Totals);
			((TextView) vi.findViewById(R.id.ProductOptions)).setText(item_option_text);
			if (item_option_text.isEmpty())
				((TextView) vi.findViewById(R.id.ProductOptions)).setHeight(0);

			list.addView(vi);
			View line = inflater.inflate(R.layout.line, null);
			list.addView(line);


			//OrderItemsList.add(list_map);

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
		
		ItemInfo.setText("Sku: "+item_data.get("sku").toString()+"\n"+
		"Qty: "+qty_val.toString()+"\n"+"Price: "+format.format(Double.valueOf(item_data.get("price").toString()).doubleValue()));
		
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

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		//setListViewHeightBasedOnChildren(getListView());
		super.onViewCreated(view, savedInstanceState);
	}

	/**
    * Update height to list view by calculate height of children
    */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	 
	  
}
