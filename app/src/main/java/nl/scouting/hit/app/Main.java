package nl.scouting.hit.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.Toast;

import nl.scouting.hit.app.courant.Kamp;
import nl.scouting.hit.app.courant.Plaats;
import nl.scouting.hit.app.courant.Project;
import nl.scouting.hit.app.model.AbstractHitEntity;
import nl.scouting.hit.app.model.HitProject;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.services.JsonData;
import nl.scouting.hit.app.services.KampInfoDownloadViaDownloadManager;

/**
 * Main of the app.
 */
public class Main extends FragmentActivity
		implements NavigationDrawer.NavigationDrawerCallbacks, HitProjectContainable {

	private ViewPager viewPager;

	private HitProject hitProject;

	public HitProject getHitProject() {
		if (hitProject == null) {
			hitProject = new JsonData(getApplicationContext()).parse(KampInfoDownloadViaDownloadManager.getLocalDataFile());
		}
		return hitProject;
	}

	public void show(AbstractHitEntity hitEntity) {
		getSupportFragmentManager().beginTransaction().addToBackStack(hitEntity.getLabel()).commit();
		int position = getHitProject().getOrderedList().indexOf(hitEntity);
		showItemAtPosition(position);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.container);
		viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

		final NavigationDrawer mNavigationDrawer = (NavigationDrawer)
				getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mNavigationDrawer.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	private class CustomPagerAdapter extends FragmentStatePagerAdapter {

		public CustomPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(final int position) {
			final AbstractHitEntity obj = getHitProject().getByIndex(position);
			return createFragmentForHitEntity(obj);
		}

		private Fragment createFragmentForHitEntity(final AbstractHitEntity obj) {
			final Fragment fragment;
			switch (obj.getType()) {
				case KAMP: {
					fragment = addArguments(new Kamp(), Kamp.PARAM_ID, obj);
					break;
				}
				case PLAATS: {
					fragment = addArguments(new Plaats(), Plaats.PARAM_ID, obj);
					break;
				}
				default:
				case PROJECT: {
					fragment = new Project();
					break;
				}
			}
			return fragment;
		}

		private Fragment addArguments(final Fragment fragment, final String key, final AbstractHitEntity obj) {
			final Bundle bundle = new Bundle();
			bundle.putLong(key, obj.getId());
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getCount() {
			return getHitProject().getOrderedList().size();
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		showItemAtPosition(position);
	}

	private void showItemAtPosition(final int position) {
		if (viewPager != null) {
			viewPager.setCurrentItem(position);
		}
	}

	private static long back_pressed;

	@Override
	public void onBackPressed() {
		int count = getSupportFragmentManager().getBackStackEntryCount();
		Log.i("Main", "BackStackCount: " + count);
		if (count == 0 && back_pressed + 1000 <= System.currentTimeMillis()) {
			Toast.makeText(getBaseContext(), getString(R.string.double_back_to_exit), Toast.LENGTH_SHORT).show();
		} else {
			super.onBackPressed();
		}
		back_pressed = System.currentTimeMillis();
	}
}