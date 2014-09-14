package nl.scouting.hit.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import nl.scouting.hit.app.courant.Kamp;
import nl.scouting.hit.app.courant.Plaats;
import nl.scouting.hit.app.misc.About;


/**
 * Main of the app.
 */
public class Main extends Activity
        implements NavigationDrawer.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawer mNavigationDrawer;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mLastScreenTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mNavigationDrawer = (NavigationDrawer)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mLastScreenTitle = getTitle();

        mNavigationDrawer.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.i("onNavigationDrawerItemSelected", "Ontvangen: " + position);

        // Titel update
        storeActionBar(position);

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        Fragment fragment;
        switch (position) {
            case 0: {
                fragment = new Welcome();
                break;
            }
            case 1: {
                fragment = new Plaats();
                Bundle bundle = new Bundle();
                bundle.putString(Plaats.PARAM_NAAM, mLastScreenTitle.toString());
                fragment.setArguments(bundle);
                break;
            }
            default: {
                fragment = new Kamp();
                Bundle bundle = new Bundle();
                bundle.putString(Kamp.PARAM_NAAM, mLastScreenTitle.toString());
                fragment.setArguments(bundle);
            }
        }

        restoreActionBar();

        fragmentManager //
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void storeActionBar(int number) {
        Log.i("storeActionBar", "Ontvangen:" + number);

        switch (number) {
            case 0:
                mLastScreenTitle = getString(R.string.title_section1);
                break;
            case 1:
                mLastScreenTitle = getString(R.string.title_section2);
                break;
            case 2:
                mLastScreenTitle = getString(R.string.title_section3);
                break;
            case 3:
                mLastScreenTitle = getString(R.string.title_section4);
                break;
        }
    }


    private void restoreActionBar() {
        Log.i("restoreActionBar", "Tabblad restored met titel: " + mLastScreenTitle);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mLastScreenTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawer.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.action_about) {
            startActivity(new Intent(this, About.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}