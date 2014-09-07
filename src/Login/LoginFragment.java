package Login;

import java.net.URI;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.magapp.R;
import com.magapp.BaseFragment;

public class LoginFragment  extends Fragment    implements  OnClickListener  {
	
	 
	View rootView;	 
	private String accountType = "com.magapp";
	private String desired_preferense_file="magapp";	 
	private XMLRPCClient client;
	private URI uri;
	private String session=null;
	private String url;
	private String api_username=null;
	private String api_password=null;	 
	private ProgressBar progressBar;
	 
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {

			rootView  = inflater.inflate(R.layout.fragment_login, null);		 
			Button LoginButton = (Button) rootView.findViewById(R.id.LoginWithAccount);
			Button AddAccountButton = (Button) rootView.findViewById(R.id.AddAccount);
			LoginButton.setOnClickListener(this);
			AddAccountButton.setOnClickListener(this);
			
			 
			 SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);		 
			 String selected_account_name = settings.getString("selected_account_name",null); 
			 String used_session = settings.getString("session",null); 
			 AccountManager manager = AccountManager.get(getActivity());	
			 Account[]  accounts = manager.getAccountsByType(accountType);
			   				 
			 			 
			/*Login is account specified*/
			 
			 for (int i=0; i < accounts.length; i++) {			 
				  Account account=accounts[i];			   
				 if (selected_account_name.equals(account.name))  {
					  api_username=manager.getUserData(account, "username");
					  api_password=manager.getPassword(account);
					  url=manager.getUserData(account, "store_url");				  
					  break;
				 }
					 		 
			  }	
			 
			 if (!api_username.equals(null)) {
				 url=url.concat("/index.php/api/xmlrpc/");	       
				 uri = URI.create(url);
			     client = new XMLRPCClient(uri);	       	      	      
			     LoginTask task=new LoginTask();			     
			   //  MagAuth.makeToast(getActivity(), "as");
			     task.execute(api_username,api_password);
			 }
			 
			 
			return rootView;
	}
	 
	 @Override
		public void onResume() {
			 super.onResume(); 
			 getActivity().invalidateOptionsMenu();
		    return; 
		}

	 public void ShowSales() {
		  SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString("session", session);
	      editor.commit();		 
		 
		 Intent Base = new Intent(getActivity(),BaseFragment.class);
		 Base.putExtra("api_session",session);
		 Base.putExtra("api_url",url);		 
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
		 FragmentManager fragmentManager = getFragmentManager();  	
		 SharedPreferences settings = getActivity().getSharedPreferences(desired_preferense_file, 0);		 
		 String selected_account_name = settings.getString("selected_account_name",null); 
		 
		 if (selected_account_name.equals(null)) {
			 ShowMessage("Please select account");
			 fragmentManager.beginTransaction()
	         .replace(R.id.container,new AccountsFragment())           
	         .addToBackStack(null)
	         .commit();  
			 return;
		 }
		 
		 AccountManager manager = AccountManager.get(getActivity());	
		 Account[]  accounts = manager.getAccountsByType(accountType);
		 
		 for (int i=0; i < accounts.length; i++) {			 
			  Account account=accounts[i];			   
			 if (selected_account_name.equals(account.name))  {
				  api_username=manager.getUserData(account, "username");
				  api_password=manager.getPassword(account);
				  url=manager.getUserData(account, "store_url");				  
				  break;
			 }
				 		 
		  }		 
		 
		  url=url.concat("/index.php/api/xmlrpc/");	       
		  uri = URI.create(url);
	      client = new XMLRPCClient(uri);	       	      	      
	      LoginTask task=new LoginTask();
	      task.execute(api_username,api_password);
	 
	}
	
	  public void ShowMessage(String text) {
		  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	  }
	 
	  class LoginTask extends AsyncTask<String, Void, String> {  
		  
		  protected void onPreExecute()
		    {			 
			  progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
			  progressBar.setVisibility(View.VISIBLE);
			  super.onPreExecute();
		               
		    };   
		    
		    
		    protected String doInBackground(String... credentials) {	
		     	    	 
		      try {           
		        	 session = (String) client.call("login",credentials[0], credentials[1]);        	
		        	 return session;	
		        } catch (XMLRPCException e) {   
		         	Log.e("Sashas",e.getMessage());	      
		         	session=e.getMessage().toString();	          	    	     	     
		       }  	catch (Exception e) { 
		         	Log.e("Sashas",e.getMessage());	         		     
		         	session=e.getMessage().toString();
		        }	     
		      return session;	
		    }	     
		    @Override
		      protected void onPostExecute(String result) {
		    	progressBar.setVisibility(View.INVISIBLE);
			    	if (result.contains(" ")) {
			    		ShowMessage(result);		    		 
			    	}else {
			    		ShowMessage("You are logged in");
			    		ShowSales();
			    	}
		    	}	     
		}
		  
}
