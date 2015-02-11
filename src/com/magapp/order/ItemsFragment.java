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
	   
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_items_info, null);

		/* Items list */
		LinearLayout list = (LinearLayout)rootView.findViewById(R.id.items_list);
		/* Items list */

		ArrayList items = getArguments().getParcelableArrayList("items");
		int items_coint=items.size();
		int item_iterator=1;
		for (Object item : items) {
		HashMap item_data = (HashMap) item;

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
			((TextView) vi.findViewById(R.id.ProductSku)).setText("SKU: "+item_data.get("sku").toString());
			/*Images */
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(vi.getContext()).build();
				ImageLoader.getInstance().init(config);
				ImageLoader.getInstance().displayImage(item_data.get("image").toString(), ((ImageView) vi.findViewById(R.id.ProductImage)));
			/*Images */

			((TextView) vi.findViewById(R.id.ProductPrice)).setText("Price: "+format.format(Double.valueOf(item_data.get("price").toString()).doubleValue()));
			((TextView) vi.findViewById(R.id.ProductTotals)).setText(Totals);
			((TextView) vi.findViewById(R.id.ProductOptions)).setText(item_option_text);
			/*Hidden Values*/
			((TextView) vi.findViewById(R.id.order_item_id)).setText(item_data.get("item_id").toString());

			Double qty_for_invoice=Double.valueOf(item_data.get("qty_ordered").toString()).doubleValue();
			if (item_data.get("qty_invoiced")!=null && !item_data.get("qty_invoiced").toString().isEmpty())
				qty_for_invoice=qty_for_invoice-Double.valueOf(item_data.get("qty_invoiced").toString()).doubleValue();

			String qty_for_invoice_string=qty_for_invoice.toString();
			((TextView) vi.findViewById(R.id.order_item_qty)).setText(qty_for_invoice_string);
			/*Hidden Values*/

			if (item_option_text.isEmpty())
				((TextView) vi.findViewById(R.id.ProductOptions)).setHeight(0);

			if (items_coint<2 || items_coint==item_iterator)
				((View) vi.findViewById(R.id.Separator)).getLayoutParams().height=0;

			list.addView(vi);
			item_iterator++;
		}

		return rootView;
	}

}
