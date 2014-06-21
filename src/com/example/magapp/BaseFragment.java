package com.example.magapp;

 
 
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseFragment extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
	public String api_session;
	public String api_url;
	public Integer menu_id;
	public Bundle connection_params;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);       
        
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
		Bundle vars= getIntent().getExtras();			
		api_session = vars.getString("api_session");
		api_url = vars.getString("api_url");
		connection_params=vars;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    	
    		if (api_session==null) {  
				Bundle vars= getIntent().getExtras();			
				api_session = vars.getString("api_session");
				api_url = vars.getString("api_url");
				connection_params=vars;
    	   }
    	  FragmentManager fragmentManager = getFragmentManager();
    	  
        // update the main content by replacing fragments    	  
    	  Fragment screen=new Fragment();
    	  switch (position) {
          case 0: 
        	  screen=new HomeFragment();
              break;
          case 1:
        	  screen=new SalesActivity();
        	   break;     
          case 2:
        	  ShowMessage("Coming Soon");
              break;              
 
      } 
    	  screen.setArguments(connection_params);
    	  fragmentManager.beginTransaction()
          .replace(R.id.container,screen)           
          .addToBackStack(null)
          .commit();
        
    	
      /*   FragmentManager fragmentManager = getFragmentManager();
       fragmentManager.beginTransaction()
            .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
              .commit();*/ 
    }
    

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.home);
                break;
            case 2:
                mTitle = getString(R.string.sales);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.base, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	  public void ShowMessage(String text) {
		  Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	  }
    /**
     * A placeholder fragment containing a simple view.
     
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
          
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_base, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((BaseActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    } */
}
 