/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.magapp.analytics.AnalyticsApplication;
import com.magapp.main.R;

public class AddAccountFragment extends Fragment implements OnClickListener {


    View rootView;

    private String accountType = "com.magapp.main";
    private String desired_preferense_file = "magapp";
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_account, null);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("AddAccountFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        String accountName = "";
        if (getArguments() != null) {
            accountName = getArguments().getString("account", "");
        }
        EditText formAccName = (EditText) rootView.findViewById(R.id.AccountName);
        EditText formUrl = (EditText) rootView.findViewById(R.id.url);
        EditText formUsername = (EditText) rootView.findViewById(R.id.username);
        EditText formPassword = (EditText) rootView.findViewById(R.id.password);
        Button SaveAccButton = (Button) rootView.findViewById(R.id.save_acc);

        if (!accountName.isEmpty()) {
            AccountManager manager = AccountManager.get(getActivity());
            Account[] accounts = manager.getAccountsByType(accountType);
            for (int i = 0; i < accounts.length; i++) {
                Account account = accounts[i];
                if (accountName.equals(account.name)) {
                    formAccName.setText(account.name);
                    formAccName.setEnabled(false);
                    formUrl.setText(manager.getUserData(account, "store_url"));
                    formUsername.setText(manager.getUserData(account, "username"));
                    formPassword.setText(manager.getPassword(account));
                    break;
                }
            }
            SaveAccButton.setText("Save Changes");
        } else {
            formAccName.setText(accountName);
            formAccName.setEnabled(true);
            formUrl.setText(accountName);
            formUsername.setText(accountName);
            formPassword.setText(accountName);
            SaveAccButton.setText("Add Account");
        }

        SaveAccButton.setOnClickListener(this);
        Button BackButton = (Button) rootView.findViewById(R.id.create_account_back_screen);
        BackButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        mTracker.setScreenName("AddAccountFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Resume")
                .setAction("AddAccountFragment")
                .build());

        super.onResume();
        getActivity().invalidateOptionsMenu();
        return;
    }

    @Override
    public void onClick(View v) {
        AccountManager manager = AccountManager.get(getActivity());
        Account[] accounts = manager.getAccountsByType(accountType);

        switch (v.getId()) {
            case R.id.save_acc:
                AddAccount();
                break;
            case R.id.create_account_back_screen: {
                if (accounts.length == 0) {
                    mTracker.setScreenName("LoginFragment");
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Click")
                            .setAction("LoginFragment")
                            .build());

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Back")
                            .setAction("AddAccountFragment::popBackStack")
                            .build());
                    getFragmentManager().popBackStack();
                }
            }
            break;
            default:
                break;
        }

    }


    public void AddAccount() {
        boolean hasErrors = false;

        TextView AccountName = (TextView) rootView.findViewById(R.id.AccountName);
        TextView ApiUsername = (TextView) rootView.findViewById(R.id.username);
        TextView ApiPass = (TextView) rootView.findViewById(R.id.password);
        EditText StoreUrl = (EditText) rootView.findViewById(R.id.url);
        String username = ApiUsername.getText().toString();
        String password = ApiPass.getText().toString();
        String store_url = StoreUrl.getText().toString();
        String account_name = AccountName.getText().toString();

        if (account_name.length() < 1) {
            hasErrors = true;
            AccountName.setBackgroundColor(Color.parseColor("#F2DEDE"));
            ShowMessage("Please specify account name");

        } else if (store_url.length() < 1 || store_url.equals("http://")) {
            hasErrors = true;
            StoreUrl.setBackgroundColor(Color.parseColor("#F2DEDE"));
            ShowMessage("Store Url is not correct");
        } else if (username.length() < 1) {
            hasErrors = true;
            ApiUsername.setBackgroundColor(Color.parseColor("#F2DEDE"));
            ShowMessage("Please specify API username");
        } else if (password.length() < 1) {
            hasErrors = true;
            ApiPass.setBackgroundColor(Color.parseColor("#F2DEDE"));
            ShowMessage("Please specify API password");
        }

        if (hasErrors) {
            return;
        }
           /*Accounts*/
        AccountManager manager = AccountManager.get(getActivity());
        Account[] accounts = manager.getAccountsByType(accountType);
        String savedAccount = "";
        if (getArguments() != null) {
            savedAccount = getArguments().getString("account", "");
        }
        if (!savedAccount.isEmpty()) {
            for (int i = 0; i < accounts.length; i++) {
                Account account = accounts[i];
                if (savedAccount.equals(account.name)) {
                    manager.setUserData(account, "store_url", store_url);
                    manager.setUserData(account, "username", username);
                    manager.setPassword(account, password);
                    break;
                }
            }
            ShowMessage("Account has been updated.");
        } else {
            final Bundle extraData = new Bundle();
            extraData.putString("username", username);
            extraData.putString("store_url", store_url);
            final Account account = new Account(account_name, accountType);
            manager.addAccountExplicitly(account, password, extraData);
            ShowMessage("Account has been added.");
        }

        SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("selected_account_name", account_name);
        editor.commit();


		  /*GA*/
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Account")
                .setAction("Added")
                .build());

        mTracker.setScreenName("AccountsFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		  /*GA*/

        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new AccountsFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.container, screen)
                .addToBackStack(null)
                .commit();

    }

    public void ShowMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
