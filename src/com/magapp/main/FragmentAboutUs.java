/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.main;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.magapp.analytics.AnalyticsApplication;
import com.magapp.connect.MagAuth;

public class FragmentAboutUs extends Fragment implements View.OnClickListener {


    View rootView;

    private MagAuth auth;


    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_aboutus, null);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("FragmentAboutUs");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        rootView.findViewById(R.id.sendEmail).setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendEmail:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.extensions.sashas.org/contacts"));
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {

        mTracker.setScreenName("FragmentAboutUs");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Resume")
                .setAction("FragmentAboutUs")
                .build());
        super.onResume();
        getActivity().invalidateOptionsMenu();
        return;
    }


}
