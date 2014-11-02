package nl.scouting.hit.app.misc;

import android.app.Activity;
import android.os.Bundle;

import nl.scouting.hit.app.R;


/**
 * Shows the About screen, displays contributors and ThankYou's.
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
