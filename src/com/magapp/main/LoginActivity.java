package com.magapp.main;

import Login.AccountsFragment;
import Login.AddAccountFragment;
import Login.LoginFragment;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class LoginActivity extends Activity {

 
	private String accountType = "com.magapp.main";
	private String desired_preferense_file = "magapp";

 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_login);
 
		FragmentManager fragmentManager = getFragmentManager();
		Fragment screen = new LoginFragment();
		fragmentManager.beginTransaction().replace(R.id.container, screen)
				.addToBackStack(null).commit(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		AccountManager manager = AccountManager.get(this);
		Account[] accounts = manager.getAccountsByType(accountType);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_menu, menu);
		MenuItem item_down = menu.findItem(R.id.accounts);
		if (accounts.length == 0)
			item_down.setVisible(false);
		else
			item_down.setVisible(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		FragmentManager fragmentManager = getFragmentManager();
		Fragment screen = new Fragment();

		switch (item.getItemId()) {
		case R.id.accounts:
			screen = new AccountsFragment();
			break;

		case R.id.add_account:
			screen = new AddAccountFragment();
			break;

		default:
			break;
		}

		fragmentManager.beginTransaction().replace(R.id.container, screen)
				.addToBackStack(null).commit();

		return false;
	}

	public void ShowMessage(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}
