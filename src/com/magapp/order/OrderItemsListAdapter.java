package com.magapp.order;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.magapp.main.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderItemsListAdapter extends BaseAdapter  {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	int selectedIndex = -1;

	public OrderItemsListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);         
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {     
        return data.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public void setSelectedIndex(int index){
        selectedIndex = index;
    }
    
    public void remove(int index){
    	data.remove(index);
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {        	
            vi =  inflater.inflate(R.layout.order_info_item_view, null);
        }     
        
        TextView product_name = (TextView)vi.findViewById(R.id.ProductName);
        TextView product_sku = (TextView)vi.findViewById(R.id.ProductSku);
        TextView product_qty = (TextView)vi.findViewById(R.id.ProductQty);
        TextView product_options =(TextView)vi.findViewById(R.id.ProductOptions);
        TextView product_price = (TextView)vi.findViewById(R.id.ProductPrice);
        TextView product_totals = (TextView)vi.findViewById(R.id.ProductTotals);
        TextView order_item_id = (TextView)vi.findViewById(R.id.order_item_id);
        TextView order_item_qty = (TextView)vi.findViewById(R.id.order_item_qty);
        ImageView item_image=(ImageView)vi.findViewById(R.id.ProductImage);

        HashMap<String, String> order_item = new HashMap<String, String>();
        order_item = data.get(position);

        /*Images */
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(vi.getContext()).build();
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().displayImage(order_item.get("image_url"), item_image);
        /*Images */

        // Setting all values in listview
        product_sku.setText(order_item.get("product_sku"));
        product_name.setText(order_item.get("product_name"));
        product_qty.setText(order_item.get("product_qty"));
        product_options.setText(order_item.get("product_options"));
        product_price.setText(order_item.get("product_price"));
        product_totals.setText(order_item.get("product_totals"));

        if (order_item.get("product_options").isEmpty())
            product_options.setHeight(0);



        return vi;
    }
     
    
}
