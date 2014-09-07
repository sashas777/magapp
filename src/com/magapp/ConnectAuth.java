package com.magapp;

import java.net.URI;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.magapp.NewOrderService.LoginTask;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public class ConnectAuth extends Activity {

	private String accountType = "com.example.magapp";
	private String desired_preferense_file = "magapp";
	private XMLRPCClient client;
	private URI uri;
	private String session = null;
	private String url;
	private String api_username = null;
	private String api_password = null;

	public void login() {
		SharedPreferences settings = this.getSharedPreferences(
				desired_preferense_file, 0);
		String selected_account_name = settings.getString(
				"selected_account_name", null);
		String used_session = settings.getString("session", null);
		AccountManager manager = AccountManager.get(this);
		Account[] accounts = manager.getAccountsByType(accountType);
		/* Login with account specified */
		for (int i = 0; i < accounts.length; i++) {
			Account account = accounts[i];
			if (selected_account_name.equals(account.name)) {
				api_username = manager.getUserData(account, "username");
				api_password = manager.getPassword(account);
				url = manager.getUserData(account, "store_url");
				break;
			}
		}

		if (!api_username.equals(null)) {
			url = url.concat("/index.php/api/xmlrpc/");
			uri = URI.create(url);
			client = new XMLRPCClient(uri);

			LoginTask task = new LoginTask();
			task.execute(api_username, api_password);
		}

	}

	class LoginTask extends AsyncTask<String, Void, String> {

		protected void onPreExecute() {
			super.onPreExecute();
		};

		protected String doInBackground(String... credentials) {

			try {
				session = (String) client.call("login", credentials[0],
						credentials[1]);
				return session;
			} catch (XMLRPCException e) {
				Log.e("Sashas", e.getMessage());
				session = e.getMessage().toString();
			} catch (Exception e) {
				Log.e("Sashas", e.getMessage());
				session = e.getMessage().toString();
			}
			return session;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.contains(" ")) {
				// ShowMessage(result);
			} else {
				// ShowMessage("You are logged in");
				// OrdersRequest();
			}
		}
	}

}
