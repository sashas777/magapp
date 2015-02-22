/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.shipment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class AddTrackingFragment extends  Fragment  implements View.OnClickListener, RequestInterface, AdapterView.OnItemSelectedListener  {

	public View rootView;
    private String shipment_increment_id;
    private Spinner carriersListView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.tracking_item_add, null);

        shipment_increment_id=getArguments().getString("increment_id");

        carriersListView = (Spinner) rootView.findViewById(R.id.carrier);
        List<String> carriersList = new ArrayList<String>();
        carriersList.add("DHL");
        carriersList.add("Federal Express");
        carriersList.add("United Parcel Service");
        carriersList.add("United States Postal Service");
        carriersList.add("Custom Value");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, carriersList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carriersListView.setAdapter(dataAdapter);

        Button addTrackingBtn = (Button) rootView.findViewById(R.id.add_tracking);
        addTrackingBtn.setOnClickListener(this);
        Button scanTrackingBtn = (Button) rootView.findViewById(R.id.scan_tracking);
        scanTrackingBtn.setOnClickListener(this);

        carriersListView.setOnItemSelectedListener(this);

        CapitalEditText();

        if (getArguments().getString("tracking_number")!=null) {
            String tracking_number = getArguments().getString("tracking_number");
            TextView tracking_number_value = ((TextView) rootView.findViewById(R.id.tracking_number));
            tracking_number_value.setText(tracking_number);

            if (getArguments().getString("carrier")!=null)
                carriersListView.setSelection(Integer.parseInt(getArguments().getString("carrier")));

            if (getArguments().getString("notify_customer")!=null)
                ((CheckBox) rootView.findViewById(R.id.notify_customer)).setChecked(true);
            else
                ((CheckBox) rootView.findViewById(R.id.notify_customer)).setChecked(false);
        }

        return rootView;
	}

    public void addTracking(){
        String carrier_code="custom";
        String tracking_number=((TextView) rootView.findViewById(R.id.tracking_number)).getText().toString();
        String carrier_title=((TextView) rootView.findViewById(R.id.carrier_title)).getText().toString();
        boolean notify_customer=((CheckBox) rootView.findViewById(R.id.notify_customer)).isChecked();

        if (tracking_number.isEmpty() || tracking_number.isEmpty()   || carrier_title.isEmpty()) {
            ((ActivityLoadInterface)getActivity()).ShowMessage("Please fill all fields");
        }

        switch (carriersListView.getSelectedItemPosition()) {
            case 0:
                carrier_code="dhl";
                break;
            case 1:
                carrier_code="fedex";
                break;
            case 2:
                carrier_code="ups";
                break;
             case 3:
                carrier_code="usps";
                break;
            default:
                carrier_code="custom";
                break;
        }


       Vector params = new Vector();
        RequestTask task;
        HashMap map_tracking= new HashMap();
        map_tracking.put("shipmentIncrementId", shipment_increment_id);
        params.add(map_tracking);
        params.add(carrier_code);
        params.add(carrier_title);
        params.add(tracking_number);
        params.add(notify_customer);

        task = new RequestTask(this, getActivity(),"magapp_sales_order_shipment.addtrack");
        task.execute(params);
    }

    public void scanTracking() {


        Intent ScanTracking = new Intent(getActivity(), ScanTrackingActivity.class);
        ScanTracking.putExtra("increment_id", shipment_increment_id);
        ScanTracking.putExtra("carrier", Integer.toString(carriersListView.getSelectedItemPosition()));
        ScanTracking.putExtra("carrier_title", ((TextView) rootView.findViewById(R.id.carrier_title)).getText().toString());
        ScanTracking.putExtra("notify_customer", ((CheckBox) rootView.findViewById(R.id.notify_customer)).isChecked());

        getActivity().startActivity(ScanTracking);
        getActivity().finish();

    }

    @Override
    public void onResume() {
        super.onResume();
        String tracking_number=getArguments().getString("tracking_number");
        TextView tracking_number_value=((TextView) rootView.findViewById(R.id.tracking_number));
        tracking_number_value.setText(tracking_number);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        TextView carrier_title=((TextView) rootView.findViewById(R.id.carrier_title));

        switch (pos) {
            default:
                if (getArguments().getString("carrier_title")!=null)
                    carrier_title.setText(getArguments().getString("carrier_title"));
                else
                    carrier_title.setText(parent.getItemAtPosition(pos).toString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add_tracking:
                addTracking();
                break;
            case R.id.scan_tracking:
                scanTracking();
                break;
        }
    }

    public void onPreExecute(){
        ((ActivityLoadInterface)getActivity()).showProgressBar();
    };

    @Override
    public void doPostExecute(Object result) {
        ((ActivityLoadInterface)getActivity()).hideProgressBar();
        ((ActivityLoadInterface)getActivity()).ShowMessage("The tracking number has been added");
        Intent ShipmentInfo = new Intent(getActivity(), ShipmentInfoActivity.class);
        ShipmentInfo.putExtra("increment_id", shipment_increment_id);
        NavUtils.navigateUpTo(getActivity(), ShipmentInfo);
    }


    public void RequestFailed(String error) {
        ((ActivityLoadInterface)getActivity()).hideProgressBar();
        ((ActivityLoadInterface)getActivity()).ShowMessage(error);
    }

    public void CapitalEditText(){
        ((TextView) rootView.findViewById(R.id.tracking_number)).addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
            }

            @Override
            public void afterTextChanged(Editable et) {
                String s = et.toString();
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    ((TextView) rootView.findViewById(R.id.tracking_number)).setText(s);
                }
            }
        });
    }
}
