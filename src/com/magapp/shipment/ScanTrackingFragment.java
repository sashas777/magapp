/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.shipment;

import android.app.Fragment;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.*;
import android.widget.Spinner;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.magapp.main.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import java.util.ArrayList;
import java.util.List;

public class ScanTrackingFragment extends  Fragment  implements ZXingScannerView.ResultHandler {

	public View rootView;
    private String shipment_increment_id;
    private Spinner carriersListView;
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        shipment_increment_id=getArguments().getString("increment_id");
        mScannerView = new ZXingScannerView(getActivity());
        if(savedInstanceState != null) {
            mFlash = savedInstanceState.getBoolean(FLASH_STATE, false);
            mAutoFocus = savedInstanceState.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = savedInstanceState.getIntegerArrayList(SELECTED_FORMATS);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
        }

        setupFormats();

        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);

        return mScannerView;
	}

    @Override
    public void onCreate(Bundle state) {

        super.onCreate(state);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if(mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}

       // mScannerView.stopCamera();

        Bundle params = new Bundle();
        params.putString("shipment_increment_id", shipment_increment_id);
        params.putString("tracking_number", rawResult.getText());

        Fragment add_tracking_fragment = new AddTrackingFragment();
        add_tracking_fragment.setArguments(params);

        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.container, add_tracking_fragment)
                 .addToBackStack("add_tracking_fragment")
                .commit();
        mScannerView.startCamera();
        /*Log.e("Sashas", "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString()); */
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
