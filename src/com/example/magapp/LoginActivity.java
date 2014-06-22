package com.example.magapp;

import java.net.URI;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import Login.AccountsFragment;
import Login.AddAccountFragment;
import Login.LoginFragment;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity  {

	/*
	private XMLRPCClient client;
	private URI uri;
	 
	private String  session;
	private String url;
	private String api_username=null;
	private String api_password=null;*/
	private String accountType = "com.example.magapp";
	private String desired_preferense_file="magapp";
	//private Menu options_menu;
	//private TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);		 
		
		FragmentManager fragmentManager = getFragmentManager();  	  
	  	Fragment screen=new LoginFragment();
	  	fragmentManager.beginTransaction()
	    .replace(R.id.container,screen)           
	    .addToBackStack(null)
	    .commit();   
		/*
		SharedPreferences settings = getSharedPreferences(desired_preferense_file, 0);		 
	    String store_url = settings.getString("website_url","http://");	    
	    EditText url_element = (EditText) findViewById(R.id.url);
	    if (store_url!="http://") 
	    	url_element.setText(store_url);	 
	    	    	   
		/*parameters*/
	  /*  session=null;
	    
	    /*Accounts*/
	 /*   AccountManager manager = AccountManager.get(this);	
		  Account[]  accounts = manager.getAccountsByType(accountType);
		  String saved_username=null; 
		  String saved_password =null;
		  
		   if (accounts.length>0) {	 
			  Account account=accounts[0];
			  api_username=account.name;	
			  api_password=manager.getPassword(account);
			  EditText new_username = (EditText) findViewById(R.id.username);
			  EditText new_password = (EditText) findViewById(R.id.password);
			  new_username.setText(api_username);
			  new_password.setText(api_password);
			  CheckBox save = (CheckBox)findViewById(R.id.save_pass);
			  save.setText("Replace saved information");
			  save.toggle();
		  } */
		 
	}
 
 
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {	  
		  
		 	AccountManager manager = AccountManager.get(this);	
		 	Account[]  accounts = manager.getAccountsByType(accountType);
		 
	    	MenuInflater inflater = getMenuInflater();	    
	    	inflater.inflate(R.menu.login_menu, menu);
	    	MenuItem item_down = menu.findItem(R.id.accounts);
	    	 if ( accounts.length==0) 		    	 
		        item_down.setVisible(false);
	    	 else
	    		 item_down.setVisible(true);
	    	 
	         return true;
	    }
	 
	   @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    { 	
		   	  
	    	  FragmentManager fragmentManager = getFragmentManager();  	  
	      	  Fragment screen=new Fragment();
 		 	
	      	  switch (item.getItemId()) {
			case R.id.accounts:  
					screen=new AccountsFragment(); 				 
				break;

			case R.id.add_account:
					screen=new AddAccountFragment();
				break;
				
			default:
				break;
			}
		       
	      fragmentManager.beginTransaction()
	          .replace(R.id.container,screen)           
	          .addToBackStack(null)
	          .commit(); 
	      
	    	return false;
	    }
 
	/*
	  public void LoginonClick1(View v) {
		  boolean hasErrors = false; 
		  TextView ApiUsername = (TextView) this.findViewById(R.id.username);  
		  TextView ApiPass = (TextView) this.findViewById(R.id.password);
		  EditText url_element = (EditText) findViewById(R.id.url);
	      String username = ApiUsername.getText().toString();  
	      String password = ApiPass.getText().toString();  
	      String store_url=url_element.getText().toString();
	      CheckBox save = (CheckBox)findViewById(R.id.save_pass);
	      
	      if (username.length() < 1) {  
	            hasErrors = true;  
	            ApiUsername.setBackgroundColor(Color.parseColor("#F2DEDE"));  
	            ShowMessage("Please specify API username");
	        }  
	        if (password.length() < 1) {  
	            hasErrors = true;  
	            ApiPass.setBackgroundColor(Color.parseColor("#F2DEDE"));
	            ShowMessage("Please specify API password");
	        }  	      
	        if (store_url=="http://" || store_url.length() < 1) {  
	            hasErrors = true;  
	            url_element.setBackgroundColor(Color.parseColor("#F2DEDE"));  
	            ShowMessage("Url is not correct");
	        }  	        
		  if (hasErrors) {  
	            return;  
	        }  
		   /*Accounts*//*
		  AccountManager manager = AccountManager.get(this);	
	 
		  if (save.isChecked()) {			 	
			  Account[]  accounts = manager.getAccountsByType(accountType);
			  for (int index = 0; index < accounts.length; index++) {				
				manager.removeAccount(accounts[index], null, null);
			  }
			  
			  final Account account = new Account(username, accountType);
		      manager.addAccountExplicitly(account, password, null);
		      api_username=username;
			  api_password=password;			  
			  ShowMessage("Your account saved and secured");
		  }
	      /*Accounts*/ /* 
	        
		  SharedPreferences settings = getSharedPreferences(desired_preferense_file, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString("website_url", store_url);
	      editor.commit();	      
	   
	       url=store_url+"/index.php/api/xmlrpc/";	       
		   uri = URI.create(url);
	       client = new XMLRPCClient(uri);
	       	      	       
	       LoginTask task=new LoginTask();
	       task.execute(api_username,api_password);
	  }
*/
	  public void ShowMessage(String text) {
		  Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	  }
	  
	  /*
	  public void ShowSales() {
		 Intent Base = new Intent(this,BaseFragment.class);
		 Base.putExtra("api_session",session);
		 Base.putExtra("api_url",url);		 
		 this.startActivity(Base);
		 finish();
	  }
	  */
	  
 
}
 