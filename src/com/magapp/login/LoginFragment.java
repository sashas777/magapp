/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2018 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.magapp.analytics.AnalyticsApplication;
import com.magapp.connect.GetSession;
import com.magapp.connect.MagAuth;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.main.BaseActivity;
import com.magapp.main.R;

import java.util.HashMap;
import java.util.Vector;

import de.timroes.axmlrpc.XMLRPCClient;

public class LoginFragment extends Fragment implements OnClickListener, GetSession, RequestInterface {


    View rootView;
    private String accountType = "com.magapp.main";
    private String desired_preferense_file = "magapp";
    private XMLRPCClient client;
    private String session = null;
    private String url;
    private ProgressBar progressBar;
    private MagAuth auth;

    private String maggapp_api_version = "1.2.0";
    private String magapp_url = "http://www.extensions.sashas.org/magento-android-manager.html";
    private String magapp_api_point = "magapp.version";
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_login, null);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("LoginFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


        rootView.findViewById(R.id.LoginWithAccount).setOnClickListener(this);
        rootView.findViewById(R.id.AddAccount).setOnClickListener(this);
        rootView.findViewById(R.id.extensionUpdate).setOnClickListener(this);
        rootView.findViewById(R.id.checkAgain).setOnClickListener(this);
        ShowProgressBar();

        rootView.findViewById(R.id.LoginWithAccount).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.AddAccount).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.extensionUpdate).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.checkAgain).setVisibility(View.INVISIBLE);

        auth = new MagAuth(this, getActivity(), 0);

        return rootView;
    }

    public void SessionReturned(String ses, Boolean status) {

        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
        if (status) {
            session = ses;
            SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);
            String store_url = settings.getString("store_url", null);
            url = store_url;
            checkVersion();
        } else {
            auth.makeToast(ses);
        }
    }

    public void ShowProgressBar() {
        progressBar = rootView.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {

        mTracker.setScreenName("LoginFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Resume")
                .setAction("LoginFragment")
                .build());
        super.onResume();
        getActivity().invalidateOptionsMenu();
        return;
    }

    public void ShowSales() {
        mTracker.setScreenName("BaseActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Activity")
                .setAction("BaseActivity")
                .build());

        Intent Base = new Intent(getActivity(), BaseActivity.class);

        this.startActivity(Base);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new Fragment();
        AccountManager manager = AccountManager.get(getActivity());
        Account[] accounts = manager.getAccountsByType(accountType);

        switch (v.getId()) {
            case R.id.LoginWithAccount: {
                if (accounts.length == 0) {
                    screen = new AddAccountFragment();
                    mTracker.setScreenName("AddAccountFragment");
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Click")
                            .setAction("AddAccountFragment")
                            .build());
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, screen)
                            .addToBackStack(null)
                            .commit();
                } else {
                    LoginAction();
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Click")
                            .setAction("LoginAction")
                            .build());
                    return;
                }
            }
            break;
            case R.id.AddAccount:
                screen = new AddAccountFragment();
                mTracker.setScreenName("AddAccountFragment");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Click")
                        .setAction("AddAccountFragment")
                        .build());
                fragmentManager.beginTransaction()
                        .replace(R.id.container, screen)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.extensionUpdate:
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Click")
                        .setAction("ExtensionUpdate")
                        .build());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(magapp_url));
                startActivity(intent);
                break;
            case R.id.checkAgain:
                checkVersion();
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Click")
                        .setAction("CheckVersionAgain")
                        .build());
                break;

            default:
                break;
        }
    }

    public void LoginAction() {
        auth = new MagAuth(this, getActivity(), 0);
    }

    public void checkVersion() {
        Vector params = new Vector();
        RequestTask task;
        task = new RequestTask(this, getActivity(), magapp_api_point);
        task.execute(params);
    }

    @Override
    public void doPostExecute(Object result, String result_api_point) {

        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);

        if (result instanceof HashMap) {
            HashMap versionMap = (HashMap) result;
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Version")
                    .setAction(versionMap.get("version").toString())
                    .build());
            Log.e("Sashas", "LoginFragment::doPostExecute::version::" + versionMap.get("version").toString());
            if (versionMap.get("version").equals(maggapp_api_version)) {
                rootView.findViewById(R.id.LoginWithAccount).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.AddAccount).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.extensionUpdate).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.checkAgain).setVisibility(View.INVISIBLE);
                ShowSales();
            } else {
                RequestFailed(getResources().getString(R.string.magapp_update));
            }

        } else {
            Log.e("Sashas", "LoginFragment::doPostExecute::else");
        }
    }

    @Override
    public void onPreExecute() {
        ShowProgressBar();
    }

    @Override
    public void RequestFailed(String error) {
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
        auth.makeToast(getResources().getString(R.string.magapp_update));
        rootView.findViewById(R.id.LoginWithAccount).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.AddAccount).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.extensionUpdate).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.checkAgain).setVisibility(View.VISIBLE);
    }
}
