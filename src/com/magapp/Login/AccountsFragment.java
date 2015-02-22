/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.magapp.connect.GetSession;
import com.magapp.connect.MagAuth;
import com.magapp.main.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountsFragment  extends Fragment    implements  OnItemClickListener,GetSession {
	
	 ListView dataList;
	 View rootView;
	 String[] AccountNames;
	
	 private String accountType = "com.magapp.main";
	 private String desired_preferense_file="magapp";	 
	 private ArrayList<HashMap<String, String>> AccountList;	 
	 private AccountsAdapter adapter;
	 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.fragment_accounts, null);
			dataList = (ListView) rootView.findViewById(R.id.AccountsList);
			 			
			AccountList = new ArrayList<HashMap<String, String>>(); 
			
			Integer selected_index=-1;
			selected_index=AddAcountItems();
			 
			adapter=new AccountsAdapter(getActivity(), AccountList);
            dataList.setAdapter(adapter);
			 
            dataList.setOnItemClickListener(this);
           
            if (selected_index>=0) {
            	 
            	 adapter.setSelectedIndex(selected_index);
        		 adapter.notifyDataSetChanged();
            }
            
             registerForContextMenu(dataList);
            
			return rootView;
	}
 
	 
	 public Integer AddAcountItems(){
		 
		 AccountManager manager = AccountManager.get(getActivity());	
		 Account[]  accounts = manager.getAccountsByType(accountType);
		 AccountNames=new String[accounts.length];
		 
		 SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);		 
		 String selected_account_name = settings.getString("selected_account_name",null);	    		 
		 Integer selected_index=-1;
		 
		 if ( accounts.length==0) {
			 FragmentManager fragmentManager = getFragmentManager();  	  
			 fragmentManager.beginTransaction()
	         .replace(R.id.container,new AddAccountFragment())           
	         .addToBackStack(null)
	         .commit();
			 return selected_index;
		 }
		 
		 for (int i=0; i < accounts.length; i++) {
			 HashMap<String, String> map = new HashMap<String, String>();
			  Account account=accounts[i];			   
				map.put("name",account.name);
				map.put("url",manager.getUserData(account, "store_url")); 
								 
				AccountList.add(map);
				AccountNames[i]=account.name;
				if (selected_account_name.equals(account.name)) 
					selected_index=i;		
 
		  }  	
		 
		 return selected_index;
	 }
 
	 @Override
		public void onResume() {
			 super.onResume(); 
			 getActivity().invalidateOptionsMenu();
		    return; 
		}
	 
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		 adapter.setSelectedIndex(dataList.getCheckedItemPosition());
		 adapter.notifyDataSetChanged();
		 SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);
	     SharedPreferences.Editor editor = settings.edit();
	     editor.putString("selected_account_name", AccountNames[dataList.getCheckedItemPosition()]);
	     editor.commit();


        MagAuth auth=new MagAuth(this,getActivity());
        auth.setSession(getActivity(),null);
		 FragmentManager fragmentManager = getFragmentManager();  	  
		 fragmentManager.beginTransaction()
         .replace(R.id.container,new LoginFragment())           
         .addToBackStack(null)
         .commit();
		 
	}

    public void SessionReturned(String ses, Boolean status){
    }
    public void ShowProgressBar() { }
	
	@Override
	 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      if (v.getId()==R.id.AccountsList) {
	    	  menu.setHeaderTitle("Options");
	          MenuInflater inflater = getActivity().getMenuInflater();
	          inflater.inflate(R.menu.accounts_context_menu, menu);
	      }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	      switch(item.getItemId()) {
	          case R.id.menu_delete:
	        	  	RemoveAccount(info.position);
	        	  	//Log.e("Sashas", "checked: " + info.position);
	                return true;
	          default:
	                return super.onContextItemSelected(item);
	      }
	}
	
	public void RemoveAccount(int position) {
	 
		 String name_for_delete=AccountNames[position];
		 
		 AccountManager manager = AccountManager.get(getActivity());	
		 Account[]  accounts = manager.getAccountsByType(accountType);		 	 		
		 manager.removeAccount(accounts[position], null, null);
		  
		 if (dataList.getCheckedItemPosition()==position) {
			 SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);
		     SharedPreferences.Editor editor = settings.edit();
		     editor.putString("selected_account_name", null);
		     editor.commit();			 
			 adapter.setSelectedIndex(-1);	 		  
		 } 
		 adapter.remove(position);
		 
		 adapter.notifyDataSetChanged();
		 dataList.invalidateViews();
		 getActivity().invalidateOptionsMenu();
		 if ( accounts.length==0) {
			 FragmentManager fragmentManager = getFragmentManager();  	  
			 fragmentManager.beginTransaction()
	         .replace(R.id.container,new AddAccountFragment())           
	         .addToBackStack(null)
	         .commit();			 
		 }
		  		 
	}
}
