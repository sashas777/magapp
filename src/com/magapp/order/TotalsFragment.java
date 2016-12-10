/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.order;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.magapp.main.R;

import java.util.HashMap;

public class TotalsFragment extends Fragment {

    public View rootView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_totals_info, null);
        TableLayout total_list = (TableLayout) rootView.findViewById(R.id.totals_table);


        Bundle vars = getArguments();
        Object[] totals = (Object[]) vars.get("totals");
        for (Object totel_obj : totals) {
            HashMap total_data = (HashMap) totel_obj;
            //Log.e("Sashas",key_data.get("title").toString());
            TableRow total_row = new TableRow(getActivity());
            total_row.setBackgroundColor(Color.parseColor("#FFFFFF"));

            TextView total_title = new TextView(getActivity());
            total_title.setText(total_data.get("title").toString() + ":");
            total_title.setPadding(10, 10, 10, 10);
            total_title.setGravity(Gravity.RIGHT);
            //total_title.setBackgroundResource(R.layout.line);

            TextView total_value = new TextView(getActivity());
            total_value.setText(total_data.get("value").toString());
            total_value.setPadding(10, 10, 10, 10);
            total_value.setGravity(Gravity.RIGHT);
            //total_value.setBackgroundResource(R.layout.line);

            total_row.addView(total_title, new TableRow.LayoutParams());
            total_row.addView(total_value, new TableRow.LayoutParams());
            total_row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            total_row.setClickable(false);
            total_list.addView(total_row);
            total_list.setColumnStretchable(0, true);
            total_list.setColumnStretchable(2, true);
        }


        return rootView;
    }

}
