package nl.scouting.hit.app.model;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import nl.scouting.hit.app.R;

/**
 * Created by Bas on 30-9-2014.
 */
public class HitIconAlert {

    private Context context;

    public HitIconAlert(Context context){
        this.context = context;
    }


    public void tonen(HitIcon targeticon){

        //Ophalen informatie
        String uitleg = targeticon.getTekst();
        String titel = targeticon.getNaam();
        int image_res_id = targeticon.getResId();


        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.icon_alert);
        dialog.setTitle("Icoon "+titel);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text_icon_alert);
        text.setText(uitleg);
        ImageView image = (ImageView) dialog.findViewById(R.id.image_icon_alert);
        image.setImageResource(image_res_id);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }




}
