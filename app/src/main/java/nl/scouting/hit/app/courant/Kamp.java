package nl.scouting.hit.app.courant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.scouting.hit.app.Main;
import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitKamp;

/**
 * Shows the info for a 'Kamponderdeel'.
 */
public class Kamp extends Fragment {

    public static final String PARAM_ID = "courant.kamp.id";
    public static final String PARAM_NAAM = "courant.kamp.naam";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_courant_kamp, container, false);

        long id = getArguments().getLong(PARAM_ID);
        HitKamp kamp = ((Main) getActivity()).getHitProject().getHitKampById(id);

        setTitle(inflater, kamp, view);
        setInfo(inflater, kamp, view);
        return view;
    }

    private void setInfo(LayoutInflater inflater, HitKamp kamp, View view) {
        TextView info = (TextView) view.findViewById(R.id.info);
        info.setText(kamp.getHitCourantTekst());
    }

    private void setTitle(LayoutInflater inflater, HitKamp kamp, View view) {
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(kamp.getNaam());
    }
}
