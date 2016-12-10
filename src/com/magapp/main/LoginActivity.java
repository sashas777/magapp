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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.magapp.analytics.AnalyticsApplication;
import com.magapp.login.AccountsFragment;
import com.magapp.login.AddAccountFragment;
import com.magapp.login.LoginFragment;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends Activity {


    private String accountType = "com.magapp.main";
    private String desired_preferense_file = "magapp";
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Sashas", "LoginActivity:onCreate");
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]
        mTracker.setScreenName("LoginFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        FragmentManager fragmentManager = getFragmentManager();

        Fragment screen = new LoginFragment();
        fragmentManager.beginTransaction().replace(R.id.container, screen).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType(accountType);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base, menu);
        MenuItem item_down = menu.findItem(R.id.accounts);
        if (accounts.length == 0)
            item_down.setVisible(false);
        else
            item_down.setVisible(true);
        menu.findItem(R.id.settings).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen;
        Log.e("Sashas", "LoginActivity:onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.accounts:
                screen = new AccountsFragment();
                mTracker.setScreenName("AccountsFragment");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Options")
                        .setAction("AccountsFragment")
                        .build());
                break;

            case R.id.add_account:
                screen = new AddAccountFragment();
                mTracker.setScreenName("AddAccountFragment");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Options")
                        .setAction("AddAccountFragment")
                        .build());
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        fragmentManager.beginTransaction().replace(R.id.container, screen)
                .addToBackStack(null).commit();

        return false;
    }

    @Override
    protected void onResume() {
        Log.e("Sashas", "LoginActivity:onResume");
        mTracker.setScreenName("LoginActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Resume")
                .setAction("LoginActivity")
                .build());
        super.onResume();
    }


    public void ShowMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
