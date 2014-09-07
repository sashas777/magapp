package Login;

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

public class AddAccountFragment  extends Fragment    implements  OnClickListener  {
	
	 
	View rootView;
	 
	 private String accountType = "com.magapp.main";
	 private String desired_preferense_file="magapp";	 
	 
	 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.fragment_add_account, null);		 
			Button SaveAccButton = (Button) rootView.findViewById(R.id.save_acc);
		    SaveAccButton.setOnClickListener(this);
			Button BackButton = (Button) rootView.findViewById(R.id.create_account_back_screen);
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
		 Account[]  accounts = manager.getAccountsByType(accountType);
		 
		 switch (v.getId()) {
		case R.id.save_acc:
			AddAccount();
			break;
		case R.id.create_account_back_screen: {	
			 if ( accounts.length==0) {
				 FragmentManager fragmentManager = getFragmentManager();  	  
				 fragmentManager.beginTransaction()
		         .replace(R.id.container,new LoginFragment())           
		         .addToBackStack(null)
		         .commit();				
			 }else 
				 getFragmentManager().popBackStack();	
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
	      String store_url=StoreUrl.getText().toString();
	      String account_name=AccountName.getText().toString();
	       
	      if (account_name.length() < 1) {  
	            hasErrors = true;  
	            AccountName.setBackgroundColor(Color.parseColor("#F2DEDE"));  
	            ShowMessage("Please specify account name");
	            
	      }else if ( store_url.length() < 1 || store_url.equals("http://") || !store_url.substring(0, 7).equals("http://") ) {  
	            hasErrors = true;  
	            StoreUrl.setBackgroundColor(Color.parseColor("#F2DEDE"));  
	            ShowMessage("Store Url is not correct");
	      }else if (username.length() < 1) {  
	            hasErrors = true;  
	            ApiUsername.setBackgroundColor(Color.parseColor("#F2DEDE"));  
	            ShowMessage("Please specify API username");
	        }else if (password.length() < 1) {  
	            hasErrors = true;  
	            ApiPass.setBackgroundColor(Color.parseColor("#F2DEDE"));
	            ShowMessage("Please specify API password");
	        }  	      
  	        
		  if (hasErrors) {  
	            return;  
	        }  
		   /*Accounts*/
		  AccountManager manager = AccountManager.get(getActivity());	
		  Account[]  accounts = manager.getAccountsByType(accountType);
		  final Bundle extraData = new Bundle();
		    extraData.putString("username", username);
		    extraData.putString("store_url", store_url);		    
		  final Account account = new Account(account_name, accountType);
		  manager.addAccountExplicitly(account, password, extraData);

		  SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString("selected_account_name", account_name);
	      editor.commit();
		  ShowMessage("Account has been added.");
 
		  FragmentManager fragmentManager = getFragmentManager();  	  
      	  Fragment screen=new AccountsFragment();
      	 fragmentManager.beginTransaction()
         .replace(R.id.container,screen)           
         .addToBackStack(null)
         .commit(); 
      	 
	  }
	 
	  public void ShowMessage(String text) {
		  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	  }
	 
}
