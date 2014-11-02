package nl.scouting.hit.app.components;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nl.scouting.hit.app.courant.IcoontjesFragment;
import nl.scouting.hit.app.courant.KampFragment;
import nl.scouting.hit.app.courant.KiezerFragment;
import nl.scouting.hit.app.courant.PlaatsFragment;
import nl.scouting.hit.app.courant.ProjectFragment;
import nl.scouting.hit.app.model.AbstractHitEntity;
import nl.scouting.hit.app.model.HitProject;

/**
 * Created by martijn on 2-11-14.
 */
public class HitCourantPagerAdapter extends FragmentStatePagerAdapter {

	private DataContext dataContext;

	public interface DataContext {
		HitProject getHitProject();
	}

	public HitCourantPagerAdapter(FragmentManager fragmentManager, DataContext dataContext) {
		super(fragmentManager);
		this.dataContext = dataContext;
	}

	@Override
	public Fragment getItem(final int position) {
		final AbstractHitEntity obj = dataContext.getHitProject().getByIndex(position);
		return createFragmentForHitEntity(obj);
	}

	private Fragment createFragmentForHitEntity(final AbstractHitEntity obj) {
		final Fragment fragment;
		switch (obj.getType()) {
			case KAMP: {
				fragment = addArguments(new KampFragment(), KampFragment.PARAM_ID, obj);
				break;
			}
			case PLAATS: {
				fragment = addArguments(new PlaatsFragment(), PlaatsFragment.PARAM_ID, obj);
				break;
			}
			case KIEZER: {
				fragment = new KiezerFragment();
				break;
			}
			case ICOONTJES:
			{
				fragment = new IcoontjesFragment();
				break;
			}
			default:
			case PROJECT: {
				fragment = new ProjectFragment();
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
		return dataContext.getHitProject().getOrderedList().size();
	}
}
