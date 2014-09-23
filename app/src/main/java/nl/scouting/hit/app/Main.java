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
import android.widget.Toast;

import nl.scouting.hit.app.courant.Kamp;
import nl.scouting.hit.app.courant.Plaats;
import nl.scouting.hit.app.misc.About;
import nl.scouting.hit.app.model.AbstractHitEntity;
import nl.scouting.hit.app.model.HitProject;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.services.JsonData;
import nl.scouting.hit.app.services.KampInfoDownloadViaDownloadManager;

/**
 * Main of the app.
 */
public class Main extends Activity
		implements NavigationDrawer.NavigationDrawerCallbacks, HitProjectContainable {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawer mNavigationDrawer;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mLastScreenTitle;

	private HitProject hitProject;

	public HitProject getHitProject() {
		if (hitProject == null) {
			hitProject = new JsonData(getApplicationContext()).parse(KampInfoDownloadViaDownloadManager.getLocalDataFile());
		}
		return hitProject;
	}

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
		AbstractHitEntity obj = getHitProject().getByIndex(position);
		mLastScreenTitle = obj.getLabel();

		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();

		Fragment fragment;
		switch (obj.getType()) {
			case KAMP: {
				fragment = new Kamp();
				Bundle bundle = new Bundle();
				bundle.putLong(Kamp.PARAM_ID, obj.getId());
				fragment.setArguments(bundle);
				break;
			}
			case PLAATS: {
				fragment = new Plaats();
				Bundle bundle = new Bundle();
				bundle.putLong(Plaats.PARAM_ID, obj.getId());
				fragment.setArguments(bundle);

				break;
			}
			default:
			case PROJECT: {
				fragment = new Welcome();
				break;
			}
		}

		restoreActionBar();

		fragmentManager //
				.beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
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

	private static long back_pressed;

	@Override
	public void onBackPressed() {
		if (back_pressed + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			Toast.makeText(getBaseContext(), getString(R.string.double_back_to_exit), Toast.LENGTH_SHORT).show();
		}
		back_pressed = System.currentTimeMillis();
	}
}