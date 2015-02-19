/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.shipment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TrackingsFragment extends  Fragment  implements View.OnClickListener, RequestInterface {

	public View rootView;
	private LinearLayout general_clicked_wrapper;
    private String shipment_increment_id;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.linear_comments, null);



		/* Items list */
		LinearLayout list = (LinearLayout)rootView.findViewById(R.id.container);
		/* Items list */

		ArrayList items = getArguments().getParcelableArrayList("items");
        shipment_increment_id=getArguments().getString("shipment_increment_id");

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
            if (carrier_code.equals("dhl"))
                tracking_number="<a href='http://track.dhl-usa.com/TrackByNbr.asp?ShipmentNumber="+tracking_number+"'>"+tracking_number+"</a>";
            if (carrier_code.equals("usps"))
                tracking_number="<a href='https://tools.usps.com/go/TrackConfirmAction_input?qtc_tLabels1="+tracking_number+"'>"+tracking_number+"</a>";
            if (carrier_code.equals("ups"))
                tracking_number="<a href='http://wwwapps.ups.com/WebTracking/track?track=yes&trackNums="+tracking_number+"'>"+tracking_number+"</a>";
            if (carrier_code.equals("fedex"))
                tracking_number="<a href='http://www.fedex.com/Tracking?action=track&tracknumbers="+tracking_number+"'>"+tracking_number+"</a>";

            String  tracking_id=item_data.get("track_id").toString();
			/* Number */

			View vi = inflater.inflate(R.layout.tracking_item_view, null);

            ((TextView) vi.findViewById(R.id.tracking_title)).setText(title);

             TextView tacking_element=((TextView) vi.findViewById(R.id.tracking_number));
             tacking_element.setText(Html.fromHtml(tracking_number));
             tacking_element.setMovementMethod(LinkMovementMethod.getInstance());

            ((TextView) vi.findViewById(R.id.tracking_id)).setText(tracking_id);
            Button removeBtn = (Button) vi.findViewById(R.id.remove);
            removeBtn.setOnClickListener(this);
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
                View wrapper_text=((View) view.getParent());
                general_clicked_wrapper=(LinearLayout) wrapper_text.getParent().getParent().getParent();
                String track_id=((TextView) wrapper_text.findViewById(R.id.tracking_id)).getText().toString();

                Vector params = new Vector();
                RequestTask task;
                HashMap map_tracking= new HashMap();
                map_tracking.put("shipmentIncrementId", shipment_increment_id);
                params.add(map_tracking);
                params.add(track_id);
                task = new RequestTask(this, getActivity(),"sales_order_shipment.removeTrack");
                task.execute(params);

                ((ActivityLoadInterface)getActivity()).ShowMessage("Tracking number has been removed");
                break;
        }
    }

    public void onPreExecute(){
        ((ActivityLoadInterface)getActivity()).showProgressBar();
    };

    @Override
    public void doPostExecute(Object result) {
        ((ActivityLoadInterface)getActivity()).hideProgressBar();
        LinearLayout list = (LinearLayout)rootView.findViewById(R.id.container);
        list.removeView(general_clicked_wrapper);
        ((ActivityLoadInterface)getActivity()).ShowMessage("Tracking number has been removed");
    }


    public void RequestFailed(String error) {
        ((ActivityLoadInterface)getActivity()).hideProgressBar();
        ((ActivityLoadInterface)getActivity()).ShowMessage(error);
    }
}
