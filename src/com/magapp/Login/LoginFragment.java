/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import com.magapp.connect.GetSession;
import com.magapp.connect.MagAuth;
import com.magapp.main.BaseActivity;
import com.magapp.main.R;
import org.xmlrpc.android.XMLRPCClient;

public class LoginFragment  extends Fragment    implements  OnClickListener, GetSession  {
	
	 
	View rootView;	 
	private String accountType = "com.magapp.main";
	private String desired_preferense_file="magapp";	 
	private XMLRPCClient client;
	private String session=null;
	private String url;
	private ProgressBar progressBar;
	private MagAuth auth;

	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.fragment_login, null);		 
			Button LoginButton = (Button) rootView.findViewById(R.id.LoginWithAccount);
			Button AddAccountButton = (Button) rootView.findViewById(R.id.AddAccount);
			LoginButton.setOnClickListener(this);
			AddAccountButton.setOnClickListener(this);

			auth=new MagAuth(this,getActivity(),0);

			return rootView;
	}
	 
	 public void SessionReturned(String ses, Boolean status){

         if (progressBar!=null)
		    progressBar.setVisibility(View.INVISIBLE);
		 if (status) {
			 session=ses;
			 SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);
			 String store_url = settings.getString("store_url", null);
			 url=store_url;

			 /*Log.e("Sashas", "returned - " + session);*/
            /* @todo something happening when click back  and then on the icon.*/
			/*  auth.makeToast("You are logged in"); */
			 ShowSales();
		 }else {
			 auth.makeToast(ses);
		 }
	 }
	 
	 public void ShowProgressBar() {
		  progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		  progressBar.setVisibility(View.VISIBLE);		 
	 }
	 
 
	 
	 @Override
	 public void onResume() {
			super.onResume(); 
			getActivity().invalidateOptionsMenu();
		    return; 
		}

	 public void ShowSales() {	 	
		 Intent Base = new Intent(getActivity(),BaseActivity.class);

		 this.startActivity(Base);
		 getActivity().finish();
	  }
 
	@Override
	public void onClick(View v) {
		 FragmentManager fragmentManager = getFragmentManager();  	  
     	 Fragment screen=new Fragment();
     	 AccountManager manager = AccountManager.get(getActivity());	
     	 Account[]  accounts = manager.getAccountsByType(accountType);
     	 
		 switch (v.getId()) {
		case R.id.LoginWithAccount:{
			if ( accounts.length==0) {
				screen=new AddAccountFragment();
			}else{
				LoginAction();
				return;
			}
		}						 
			break;
		case R.id.AddAccount:
			screen=new AddAccountFragment();
			break;
		default:
			break;
		}
		 fragmentManager.beginTransaction()
         .replace(R.id.container,screen)           
         .addToBackStack(null)
         .commit();      
			 
	}
		 
	public void LoginAction(){ 
	      auth=new MagAuth(this,getActivity(),0);
	}

}
