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
import android.widget.Toast;

import com.magapp.connect.GetSession;
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

    private String maggapp_api_version = "1.2";
    private String magapp_url = "http://www.extensions.sashas.org/magento-android-manager.html";
    private String magapp_api_point = "magapp.version";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Sashas", this.getClass().getName() + "::onCreateView");
        rootView = inflater.inflate(R.layout.fragment_login, null);
        rootView.findViewById(R.id.LoginWithAccount).setOnClickListener(this);
        rootView.findViewById(R.id.AddAccount).setOnClickListener(this);
        rootView.findViewById(R.id.extensionUpdate).setOnClickListener(this);
        rootView.findViewById(R.id.checkAgain).setOnClickListener(this);
        ShowProgressBar();

        rootView.findViewById(R.id.LoginWithAccount).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.AddAccount).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.extensionUpdate).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.checkAgain).setVisibility(View.INVISIBLE);

        checkVersion();

        return rootView;
    }

    /* @todo remove */
    public void SessionReturned(String ses, Boolean status) {
        Log.e("Sashas", "LoginFragment::SessionReturned - TO BE REMOVED");
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
        if (status) {
            session = ses;
            SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);
            String store_url = settings.getString("store_url", null);
            url = store_url;
            checkVersion();
        } else {
            Toast.makeText(getActivity(), ses, Toast.LENGTH_SHORT).show();
        }
    }

    public void ShowProgressBar() {
        progressBar = rootView.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        Log.d("Sashas", this.getClass().getName() + "::onResume");
        super.onResume();
        getActivity().invalidateOptionsMenu();
        return;
    }

    public void ShowSales() {
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
                    Log.d("Sashas", this.getClass().getName() + ":LoginWithAccount:NoAccounts");
                    screen = new AddAccountFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, screen)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Log.d("Sashas", this.getClass().getName() + ":LoginWithAccount:Login");
                    checkVersion();
                    return;
                }
            }
            break;
            case R.id.AddAccount:
                screen = new AddAccountFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, screen)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.extensionUpdate:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(magapp_url));
                startActivity(intent);
                break;
            case R.id.checkAgain:
                checkVersion();
                break;

            default:
                break;
        }
    }

    public void LoginAction() {
        //auth = new MagAuth(this, getActivity(), 0);
        ShowSales();
    }

    public void checkVersion() {
        Log.d("Sashas", this.getClass().getName() + "::checkVersion");
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
            Log.d("Sashas", "LoginFragment:doPostExecute::version:" + versionMap.get("version").toString());
            if (versionMap.get("version").toString().compareTo(maggapp_api_version) > -1) {
                rootView.findViewById(R.id.LoginWithAccount).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.AddAccount).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.extensionUpdate).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.checkAgain).setVisibility(View.INVISIBLE);
                ShowSales();
            } else {
                RequestFailed(getResources().getString(R.string.magapp_update));
            }

        } else {
            Log.e("Sashas", "LoginFragment:doPostExecute::else");
        }
    }

    @Override
    public void onPreExecute() {
        ShowProgressBar();
    }

    @Override
    public void RequestFailed(String error) {
        Log.d("Sashas", this.getClass().getName() + "::RequestFailed");
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
        Log.d("Sashas", this.getClass().getName() + " " + error);
        Toast.makeText(getActivity().getBaseContext(), error, Toast.LENGTH_SHORT).show();

        rootView.findViewById(R.id.LoginWithAccount).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.AddAccount).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.extensionUpdate).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.checkAgain).setVisibility(View.INVISIBLE);
    }
}
