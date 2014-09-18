package nl.scouting.hit.app.courant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.scouting.hit.app.Main;
import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitPlaats;

/**
 * Shows the information for a 'HIT-plaats'.
 */
public class Plaats extends Fragment {

    public static final String PARAM_ID = "courant.plaats.id";
    public static final String PARAM_NAAM = "courant.plaats.naam";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_courant_plaats, container, false);

        long id = getArguments().getLong(PARAM_ID);
        HitPlaats plaats = ((Main) getActivity()).getHitProject().getHitPlaatsById(id);

        setTitle(inflater, plaats, view);
        setKampen(inflater, plaats, view);
        return view;
    }

    private void setKampen(LayoutInflater inflater, HitPlaats plaats, View view) {
        final ListView listview = (ListView) view.findViewById(R.id.kampen);

        final List<String> list = new ArrayList<String>();
        for (HitKamp kamp : plaats.getKampen()) {
            list.add(kamp.getNaam());
        }
        final ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, list.toArray(new String[list.size()]));
        listview.setAdapter(adapter);
    }

    private void setTitle(LayoutInflater inflater, HitPlaats plaats, View view) {
        TextView title = (TextView) view.findViewById(R.id.title);
        String name = getArguments().getString(PARAM_NAAM);

//        String titleText = inflater.getContext().getString(R.string.courant_plaats_title, name);

        String titleText = plaats.getHitCourantTekst();
        title.setText(titleText);
    }

}
