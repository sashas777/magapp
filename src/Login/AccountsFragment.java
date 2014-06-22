package Login;

import java.util.ArrayList;
import java.util.HashMap;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.example.magapp.R;

public class AccountsFragment  extends Fragment    implements  OnItemClickListener, OnItemLongClickListener  {
	
	 ListView dataList;
	 View rootView;
	 String[] AccountNames;
	
	 private String accountType = "com.example.magapp";
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
            dataList.setOnItemLongClickListener(this);
            
            if (selected_index>=0) {
            	 
            	 adapter.setSelectedIndex(selected_index);
        		 adapter.notifyDataSetChanged();
            }
            
            registerForContextMenu(dataList);
            
			return rootView;
	}
	 
	 public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
     {

		 Log.e("Sashas", "checked: " +arg2);
		  
        // adapter.notifyDataSetChanged();
         //adapter.notifyDataSetInvalidated();
         return true;
     }
	 
	 public Integer AddAcountItems(){
		 
		 AccountManager manager = AccountManager.get(getActivity());	
		 Account[]  accounts = manager.getAccountsByType(accountType);
		 AccountNames=new String[accounts.length];
		 
		 SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);		 
		 String selected_account_name = settings.getString("selected_account_name",null);	    		 
		 Integer selected_index=-1;
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		 adapter.setSelectedIndex(dataList.getCheckedItemPosition());
		 adapter.notifyDataSetChanged();
		 SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);
	     SharedPreferences.Editor editor = settings.edit();
	     editor.putString("selected_account_name", AccountNames[dataList.getCheckedItemPosition()]);
	     editor.commit();
		 
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      if (v.getId()==R.id.AccountsList) {
	          MenuInflater inflater = getActivity().getMenuInflater();
	          inflater.inflate(R.menu.accounts_context_menu, menu);
	      }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	      switch(item.getItemId()) {
	          case R.id.menu_delete:
	        // remove stuff here
	                return true;
	          default:
	                return super.onContextItemSelected(item);
	      }
	}
}
