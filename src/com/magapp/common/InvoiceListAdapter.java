/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.common;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.magapp.main.R;

import java.util.ArrayList;
import java.util.HashMap;

public class InvoiceListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    int selectedIndex = -1;

    public InvoiceListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public void remove(int index) {
        data.remove(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.invoice_list_item, null);
        }

        TextView invoice_num = (TextView) vi.findViewById(R.id.invoice_number);
        TextView description = (TextView) vi.findViewById(R.id.InvoiceDescription);
        TextView increment_id = (TextView) vi.findViewById(R.id.increment_id);
        TextView Line3 = (TextView) vi.findViewById(R.id.ItemLine3);

        HashMap<String, String> invoice = new HashMap<String, String>();
        invoice = data.get(position);

        // Setting all values in listview
        increment_id.setText(invoice.get("increment_id"));
        invoice_num.setText(invoice.get("invoice_number"));
        description.setText(invoice.get("description"));
        Line3.setText(invoice.get("line3"));

        return vi;
    }


}
