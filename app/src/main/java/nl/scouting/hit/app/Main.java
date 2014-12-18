package nl.scouting.hit.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Stack;

import nl.scouting.hit.app.components.HitCourantPagerAdapter;
import nl.scouting.hit.app.components.ZoomOutPageTransformer;
import nl.scouting.hit.app.misc.AboutActivity;
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

	public long current_viewpager_item = 0;

	private void showItemAtPosition(final int position) {
		if (viewPager != null) {
			viewPager.setCurrentItem(position);

			current_viewpager_item = viewPager.getCurrentItem();

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
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		// action_share uitzetten hoeft niet als er geen internet is. Delen kan ook naar notities of via bluetooth.

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_search:
				openSearch();
				return true;
			case R.id.action_about:
				openAbout();
				return true;
			case R.id.action_share:
				shareCurrentItem();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void shareCurrentItem() {
		int currentItemIndex = viewPager.getCurrentItem();
		final AbstractHitEntity o = getHitProject().getByIndex(currentItemIndex);

		new AlertDialog.Builder(this)
				.setMessage("Je staat op het punt om tekst te delen met anderen, aan het verzenden kunnen kosten verbonden zijn. Wil je doorgaan?")
				.setPositiveButton("Natuurlijk! Deel het!", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						Intent sendIntent = new Intent();
						sendIntent.setAction(Intent.ACTION_SEND);
						sendIntent.putExtra(Intent.EXTRA_TEXT, o.getShareText()); //
						sendIntent.setType("text/plain");
						startActivity(Intent.createChooser(sendIntent, "Deel deze info van de HIT"));
					}
				})
				.setNegativeButton("Nee, dit wil ik niet", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getApplicationContext(), "Er is niets gedeeld",
								Toast.LENGTH_LONG).show();
					}
				})
				.show();
	}

	private void openSearch() {
		showItemAtPosition(1);
	}

	private void openIcoontjes() {
		showItemAtPosition(2);
	}

	private void openAbout() {
		Intent aboutIntent = new Intent(this, AboutActivity.class);
		startActivity(aboutIntent);
	}
}