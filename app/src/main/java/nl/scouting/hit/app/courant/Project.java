package nl.scouting.hit.app.courant;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.components.ExpandableHeightListView;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitKampRO;
import nl.scouting.hit.app.model.HitPlaats;
import nl.scouting.hit.app.model.HitProject;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.style.PlaatsStyle;

/**
 * Shows the welcome information.
 */
public class Project extends Fragment implements AdapterView.OnItemClickListener {

	private ExpandableHeightListView kampenLijst;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
//		int layout = R.layout.fragment_welcome;
		int layout = R.layout.fragment_courant_kiezer;
		View view = inflater.inflate(layout, container, false);
		kampenLijst = (ExpandableHeightListView) view.findViewById(R.id.kampen);
		setPrijzen(view);
		setPlaatsen(view);
		setLeeftijd(view);

		setKampen(view);
		return view;
	}

	private HitProject getHitProject() {
		return getHitContainer().getHitProject();
	}

	private HitProjectContainable getHitContainer() {
		return ((HitProjectContainable) this.getActivity());
	}

	/**
	 * Returns only items of type HitKamp, sorted by name.
	 *
	 * @return a list containing only HitKamp
	 */
	private List<HitKamp> getHitKampsOnly() {
		final List<HitKamp> list = new ArrayList<HitKamp>();
		final HitProject hitProject = getHitProject();
		for (HitKamp kamp : hitProject.getKampen()) {
			list.add(new HitKampRO(kamp) {
				@Override
				public String toString() {
					return getNaam();
				}
			});
		}
		Collections.sort(list, new Comparator<HitKamp>() {
			@Override
			public int compare(final HitKamp lhs, final HitKamp rhs) {
				return strip(lhs.getNaam()).compareTo(strip(rhs.getNaam()));
			}

			// ignore quotes while sorting
			private String strip(String s) {
				return s.replace("\"", "");
			}
		});
		return list;
	}

	private void setKampen(final View view) {
		kampenLijst.setExpanded(true);
		kampenLijst.setAdapter(new HitKampArrayAdapter(
				getActivity().getApplicationContext()
				, getHitKampsOnly()
		));
		kampenLijst.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		HitKamp kamp = (HitKamp) parent.getItemAtPosition(position);
		getHitContainer().show(kamp);
	}

	static class HitPlaatsSpinnerModel {
		HitPlaats plaats;

		public HitPlaatsSpinnerModel(HitPlaats plaats) {
			this.plaats = plaats;
		}

		public String toString() {
			if (plaats == null) {
				return "<Maakt niet uit>";
			}
			return "HIT " + plaats.getNaam();
		}
	}

	private void setPlaatsen(final View view) {
		Spinner plaats = (Spinner) view.findViewById(R.id.plaats);

		plaats.setAdapter(new HitPlaatsArrayAdapter(getActivity().getApplicationContext(), R.layout.plaats_spinner_item
				, HitPlaatsArrayAdapter.getModel(getHitProject())));
		plaats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view2, final int position, final long id) {
				final HitKampArrayAdapter.KampFilter filter = (HitKampArrayAdapter.KampFilter) ((Filterable) kampenLijst.getAdapter()).getFilter();
				filter.plaats = ((HitPlaatsSpinnerModel) parent.getItemAtPosition(position)).plaats;
				filter.filter("plaats", new Filter.FilterListener() {
					@Override
					public void onFilterComplete(final int count) {
						TextView aantal = (TextView) view.findViewById(R.id.aantal_gevonden);
						aantal.setText(view.getContext().getString(R.string.aantal_gevonden, count, filter.criteria()));
					}
				});
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});
	}

	public static class HitPlaatsArrayAdapter extends ArrayAdapter<HitPlaatsSpinnerModel> {

		private List<HitPlaatsSpinnerModel> data;

		public HitPlaatsArrayAdapter(Context context, int resource, List<HitPlaatsSpinnerModel> data) {
			super(context, resource, data);
			this.data = data;
		}

		public HitPlaatsArrayAdapter(Context context, int resource, int id, List<HitPlaatsSpinnerModel> data) {
			super(context, resource, id, data);
			this.data = data;
		}

		public static final List<HitPlaatsSpinnerModel> getModel(HitProject project) {
			final List<HitPlaatsSpinnerModel> result = new ArrayList<HitPlaatsSpinnerModel>();
			result.add(new HitPlaatsSpinnerModel(null));
			for (HitPlaats p : project.getPlaatsen()) {
				result.add(new HitPlaatsSpinnerModel(p));
			}
			return result;
		}
	}

	static class PrijsSpinnerModel {
		Integer prijs;

		public PrijsSpinnerModel(Integer prijs) {
			this.prijs = prijs;
		}

		public String toString() {
			if (prijs == null) {
				return "<Maakt niet uit>";
			}
			return "€ " + prijs;
		}
	}

	private void setPrijzen(final View view) {
		Spinner prijs = (Spinner) view.findViewById(R.id.prijs);
		final List<PrijsSpinnerModel> prijzen = HitPrijsArrayAdapter.getModel(getHitProject());
		prijs.setAdapter(new HitPrijsArrayAdapter(getActivity().getApplicationContext(), R.layout.prijs_spinner_item
				, prijzen));
		prijs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view2, final int position, final long id) {
				final HitKampArrayAdapter.KampFilter filter = (HitKampArrayAdapter.KampFilter) ((Filterable) kampenLijst.getAdapter()).getFilter();
				filter.prijs = ((PrijsSpinnerModel) parent.getItemAtPosition(position)).prijs;
				filter.filter("prijs", new Filter.FilterListener() {
					@Override
					public void onFilterComplete(final int count) {
						TextView aantal = (TextView) view.findViewById(R.id.aantal_gevonden);
						aantal.setText(view.getContext().getString(R.string.aantal_gevonden, count, filter.criteria()));
					}
				});
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
			}
		});
	}

	public static class HitPrijsArrayAdapter extends ArrayAdapter<PrijsSpinnerModel> {
		private LayoutInflater mInflater;

		public HitPrijsArrayAdapter(Context context, int resource, List<PrijsSpinnerModel> items) {
			super(context, resource, items);
			mInflater = LayoutInflater.from(context);
		}

		public static final List<PrijsSpinnerModel> getModel(HitProject project) {
			Set<Integer> prijzen = new TreeSet<Integer>();
			for (HitKamp kamp : project.getKampen()) {
				prijzen.add(kamp.getDeelnamekosten());
			}

			List<PrijsSpinnerModel> result = new ArrayList<PrijsSpinnerModel>();
			result.add(new PrijsSpinnerModel(null));
			for (Integer prijs : prijzen) {
				result.add(new PrijsSpinnerModel(prijs));
			}
			return result;
		}
	}

	private void setLeeftijd(final View view) {
		SeekBar leeftijd = (SeekBar) view.findViewById(R.id.leeftijd);
		leeftijd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
				final HitKampArrayAdapter.KampFilter filter = (HitKampArrayAdapter.KampFilter) ((Filterable) kampenLijst.getAdapter()).getFilter();
				filter.leeftijd = progress < 5 ? null : progress;
				filter.filter("leeftijd", new Filter.FilterListener() {
					@Override
					public void onFilterComplete(final int count) {
                        TextView aantal = (TextView) view.findViewById(R.id.aantal_gevonden);
                        if(count > 1) {
                            aantal.setText(view.getContext().getString(R.string.aantal_gevonden, count, filter.criteria()));
                        }else{
                            if (count < 1){
                                aantal.setText(view.getContext().getString(R.string.aantal_gevonden_geen, count, filter.criteria()));
                            } else {
                                aantal.setText(view.getContext().getString(R.string.aantal_gevonden_enkelvoud, count, filter.criteria()));
                            }
                        }
					}
				});
			}

			@Override
			public void onStartTrackingTouch(final SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(final SeekBar seekBar) {

			}
		});
	}

	public class HitKampArrayAdapter extends ArrayAdapter<HitKamp> {
		private List<HitKamp> originalList;
		private List<HitKamp> currentList;
		private KampFilter filter;

		private LayoutInflater mInflater;

		public HitKampArrayAdapter(Context context, List<HitKamp> items) {
			super(context, 0, items);
			this.originalList = new ArrayList<HitKamp>(items);
			this.currentList = new ArrayList<HitKamp>(items);
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HitKamp kamp = getItem(position);
			View view;
			if (convertView == null) {
				view = mInflater.inflate(R.layout.list_plaats_kampen_met_plaats, null);
			} else {
				view = convertView;
			}

			PlaatsStyle style = PlaatsStyle.by(kamp);

			TextView tv = (TextView) view.findViewById(R.id.naam);
			tv.setText(kamp.getNaam());

			ImageView iv = (ImageView) view.findViewById(R.id.plaats);
			iv.setImageResource(style.logoSmall);

			return view;
		}

		@Override
		public Filter getFilter() {
			if (filter == null) {
				filter = new KampFilter();
			}
			return filter;
		}

		private class KampFilter extends Filter {
			public Integer prijs;
			public HitPlaats plaats;
			public Integer leeftijd;

			public boolean matches(HitKamp kamp) {
				boolean result = true;
				if (plaats != null && !plaats.equals(kamp.getPlaats())) {
					result = false;
				}
				if (prijs != null && kamp.getDeelnamekosten() > prijs) {
					result = false;
				}
				if (leeftijd != null) {
					if (kamp.getMinimumLeeftijd() > leeftijd || leeftijd > kamp.getMaximumLeeftijd()) {
						return false;
					}
				}
				return result;
			}

			@Override
			protected FilterResults performFiltering(final CharSequence constraint) {
				FilterResults result = new FilterResults();
				List<HitKamp> filteredItems = new ArrayList<HitKamp>();
				for (HitKamp k : originalList) {
					if (matches(k)) {
						filteredItems.add(k);
					}
				}
				result.values = filteredItems;
				result.count = filteredItems.size();
				return result;
			}

			@Override
			protected void publishResults(final CharSequence constraint, final FilterResults results) {
				List<HitKamp> list = (ArrayList<HitKamp>) results.values;
				notifyDataSetChanged();
				clear();
				for (HitKamp k : list) {
					add(k);
				}
				notifyDataSetInvalidated();
			}

			public String toString() {
				return new StringBuilder() //
						.append(", plaats: ").append(plaats == null ? "maakt niet uit" : plaats.getNaam()) //
						.append(", leeftijd: ").append(leeftijd == null ? "maakt niet uit" : leeftijd) //
						.append(", prijs: ").append(prijs == null ? "maakt niet uit" : prijs) //
						.toString();
			}

			public String criteria() {
				StringBuilder result = new StringBuilder();
				if (leeftijd != null) {
					result.append(" voor de leeftijd van ").append(leeftijd).append(" jaar");
				}
				if (prijs != null) {
					if (result.length() > 0) {
						if (plaats == null) {
							result.append(" en");
						} else {
							result.append(",");
						}
					} else {
						result.append(" met");
					}
					result.append(" een prijs niet hoger dan €").append(prijs);
				}
				if (plaats != null) {
					if (result.length() > 0) {
						result.append(" en");
					}
					result.append(" bij HIT ").append(plaats.getNaam());
				}
				return result.toString();
			}
		}
	}
}
