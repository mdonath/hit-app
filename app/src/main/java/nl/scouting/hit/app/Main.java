package nl.scouting.hit.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Stack;

import nl.scouting.hit.app.components.HitCourantPagerAdapter;
import nl.scouting.hit.app.components.ZoomOutPageTransformer;
import nl.scouting.hit.app.model.AbstractHitEntity;
import nl.scouting.hit.app.model.HitProject;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.nav.AbstractIndexedItem;
import nl.scouting.hit.app.nav.Item;
import nl.scouting.hit.app.services.JsonData;
import nl.scouting.hit.app.services.KampInfoDownloadViaDownloadManager;

/**
 * Main of the app.
 */
public class Main extends FragmentActivity
		implements NavigationDrawer.NavigationDrawerCallbacks, HitProjectContainable {

	private static final String TAG = "Main";
	private ViewPager viewPager;

	private HitProject hitProject;

	private Stack<Integer> huidigFragmentPosition = new Stack<Integer>();

	public HitProject getHitProject() {
		if (hitProject == null) {
			Log.i(TAG, "Loading data!!!!");
			hitProject = new JsonData(getApplicationContext()).parse(KampInfoDownloadViaDownloadManager.getLocalDataFile());
		}
		return hitProject;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.container);
		viewPager.setAdapter(new HitCourantPagerAdapter(getSupportFragmentManager(), new HitCourantPagerAdapter.DataContext() {
			@Override
			public HitProject getHitProject() {
				return Main.this.getHitProject();
			}
		}));
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

		final NavigationDrawer mNavigationDrawer = (NavigationDrawer)
				getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mNavigationDrawer.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		showItemAtPosition(0);
	}

	@Override
	public void onNavigationDrawerItemSelected(Item item) {
		showItemAtPosition(((AbstractIndexedItem) item).getIndex());
	}

	public void show(AbstractHitEntity hitEntity) {
//		getSupportFragmentManager() //
//				.beginTransaction() //
//				.addToBackStack(hitEntity.getLabel()) //
//				.commit();
		int position = getHitProject().getOrderedList().indexOf(hitEntity);
		showItemAtPosition(position);
	}

	private void showItemAtPosition(final int position) {
		if (viewPager != null) {
			viewPager.setCurrentItem(position);
			// zet het huidige fragment op de eigen implementatie van de backstack
			pushToCustomBackStack(position);
			Log.i(TAG, "eigen stack na push: " + huidigFragmentPosition);
		}
	}

	private void pushToCustomBackStack(int position) {
		if (huidigFragmentPosition.isEmpty() || huidigFragmentPosition.peek() != position) {
			huidigFragmentPosition.push(position);
		}
	}

	@Override
	public void onBackPressed() {
		if (goBackFragment()) {
			super.onBackPressed();
		}
	}

	private boolean goBackFragment() {
		if (viewPager != null) {
			if (!huidigFragmentPosition.isEmpty()) {
				final int position = popFromCustomBackStack();
				if (position == -1) {
					return true; // QUIT
				}

				showItemAtPosition(position);
			}
		}
		return false;
	}

	private int popFromCustomBackStack() {
		// kieper de huidige weg
		huidigFragmentPosition.pop();

		// Nog iets over?
		if (huidigFragmentPosition.isEmpty()) {
			// Nope
			return -1;
		} else {
			// haal nu de VORIGE op
			return huidigFragmentPosition.pop();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_search:
				openSearch();
				return true;
			case R.id.action_settings:
				// openSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void openSearch() {
		Log.i(TAG, "TODO Open HITKIEZER");
		showItemAtPosition(1);
	}

	private void openIcoontjes() {
		Log.i(TAG, "TODO Open icoontjes");
		showItemAtPosition(2);
	}
}