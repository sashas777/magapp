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

import com.magapp.main.R;

public class AddAccountFragment extends Fragment implements OnClickListener {


    View rootView;

    private String accountType = "com.magapp.main";
    private String desired_preferense_file = "magapp";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_account, null);

        String accountName = "";
        if (getArguments() != null) {
            accountName = getArguments().getString("account", "");
        }
        EditText formAccName = rootView.findViewById(R.id.AccountName);
        EditText formUrl = rootView.findViewById(R.id.url);
        EditText formUsername = rootView.findViewById(R.id.username);
        EditText formPassword = rootView.findViewById(R.id.password);
        Button SaveAccButton = rootView.findViewById(R.id.save_acc);

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
        Button BackButton = rootView.findViewById(R.id.create_account_back_screen);
        BackButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
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
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
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

        TextView AccountName = rootView.findViewById(R.id.AccountName);
        TextView ApiUsername = rootView.findViewById(R.id.username);
        TextView ApiPass = rootView.findViewById(R.id.password);
        EditText StoreUrl = rootView.findViewById(R.id.url);
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

        FragmentManager fragmentManager = getFragmentManager();
        Fragment screen = new LoginFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.container, screen)
                .addToBackStack(null)
                .commit();

    }

    public void ShowMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
