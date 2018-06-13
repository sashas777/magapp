/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2018 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class BaseActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public Integer menu_id;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Obtain the shared Tracker instance.

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "HomeFragment");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);



        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, findViewById(R.id.drawer_layout));

    }

    public void setActionBarTitle(String title) {
        mTitle = title;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getFragmentManager();
        mTitle = getTitle();

        // update the main content by replacing fragments
        Fragment screen = new Fragment();
        switch (position) {
            case 0:
                screen = new HomeFragment();
                break;
            case 1:

                screen = new SalesListFragment();
                break;
            case 2:

                screen = new SettingsFragment();
                break;

        }
        Log.e("Sashas", "BaseActivity:onNavigationDrawerItemSelected");
        fragmentManager.beginTransaction().replace(R.id.container, screen).commit();

		/*
         * FragmentManager fragmentManager = getFragmentManager();
		 * fragmentManager.beginTransaction() .replace(R.id.container,
		 * PlaceholderFragment.newInstance(position + 1)) .commit();
		 */
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
                mTitle = getString(R.string.settings);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        mTitle = getTitle();
        /*Issue on the home fragment when click back*/
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {

            fm.popBackStack();
            //super.onBackPressed();
        } else {
            finish();
            // super.onBackPressed();
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


        Log.e("Sashas", "BaseActivity:onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.action_period:
                return true;
            case R.id.twoytd:
                return false;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void ShowMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * A placeholder fragment containing a simple view.
     * <p>
     * public static class PlaceholderFragment extends Fragment { /** The
     * fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section number.
     *
     * public static PlaceholderFragment newInstance(int sectionNumber) {
     * PlaceholderFragment fragment = new PlaceholderFragment(); Bundle args =
     * new Bundle(); args.putInt(ARG_SECTION_NUMBER, sectionNumber);
     * fragment.setArguments(args); return fragment; }
     *
     * public PlaceholderFragment() { }
     *
     * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
     *           container, Bundle savedInstanceState) { View rootView =
     *           inflater.inflate(R.layout.fragment_base, container, false);
     *           TextView textView = (TextView)
     *           rootView.findViewById(R.id.section_label);
     *           textView.setText(Integer
     *           .toString(getArguments().getInt(ARG_SECTION_NUMBER))); return
     *           rootView; }
     * @Override public void onAttach(Activity activity) {
     *           super.onAttach(activity); ((BaseActivity)
     *           activity).onSectionAttached(
     *           getArguments().getInt(ARG_SECTION_NUMBER)); } }
     */


}
