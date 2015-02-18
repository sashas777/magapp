/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.shipment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class TrackingsFragment extends  Fragment  implements View.OnClickListener {

	public View rootView;
	   
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.linear_comments, null);

        Button removeBtn = (Button) rootView.findViewById(R.id.remove);
        removeBtn.setOnClickListener(this);

		/* Items list */
		LinearLayout list = (LinearLayout)rootView.findViewById(R.id.container);
		/* Items list */

		ArrayList items = getArguments().getParcelableArrayList("items");

		for (Object item : items) {
		    HashMap item_data = (HashMap) item;
			/*Carrier Code*/
			String  carrier_code=item_data.get("carrier_code").toString();
			/*Carrier Code*/
			/* Title */
            String  title=item_data.get("title").toString();
			/* Title */
			/* Number */
            String  tracking_number=item_data.get("number").toString();
            String  tracking_id=item_data.get("track_id").toString();
			/* Number */

			View vi = inflater.inflate(R.layout.tracking_item_view, null);

            ((TextView) vi.findViewById(R.id.tracking_title)).setText(title);
            ((TextView) vi.findViewById(R.id.tracking_number)).setText(tracking_number);
            ((TextView) vi.findViewById(R.id.tracking_id)).setText(tracking_id);

			/*Date*/
            DateFormat created_at_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            created_at_format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date created_at_date = new Date();
            try {
                created_at_date =created_at_format.parse(item_data.get("created_at").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String created_at_date_string= new SimpleDateFormat("LLL dd, yyy HH:mm:ss") .format(created_at_date);
			/*Date*/

            ((TextView) vi.findViewById(R.id.card_title)).setText("Shipped at "+created_at_date_string);

			list.addView(vi);
		}

		return rootView;
	}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remove:
                ((ActivityLoadInterface)getActivity()).ShowMessage("The Tracking Number has been removed");
                break;
        }
    }

}
