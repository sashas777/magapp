package Login;

import org.xmlrpc.android.XMLRPCClient;

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
import com.magapp.main.BaseFragment;
import com.magapp.main.R;

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
			auth=new MagAuth(this,getActivity()); 
			 
			return rootView;
	}
	 
	 public void SessionReturned(String ses){
		 progressBar.setVisibility(View.INVISIBLE);
		 session=ses;
		 url=auth.getApiUrl();
		 /* Log.e("Sashas","returned - "+session); */
		 auth.makeToast("You are logged in");
		 ShowSales();
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
	      auth=new MagAuth(this,getActivity()); 
	}

}
