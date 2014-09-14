package nl.scouting.hit.app.courant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.scouting.hit.app.R;

/**
 * Shows the info for a 'Kamponderdeel'.
 */
public class Kamp extends Fragment {

    //public static final String PARAM_ID = "courant.kamp.id";
    public static final String PARAM_NAAM = "courant.kamp.naam";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_courant_kamp, container, false);
        setTitle(inflater, view);
        return view;
    }

    private void setTitle(LayoutInflater inflater, View view) {
        TextView title = (TextView) view.findViewById(R.id.title);
        String name = getArguments().getString(PARAM_NAAM);
        String titleText = inflater.getContext().getString(R.string.courant_kamp_title, name);
        title.setText(titleText);
    }
}
