package nl.scouting.hit.app;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.scouting.hit.app.model.HitIcon;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitPlaats;
import nl.scouting.hit.app.model.HitProject;

/**
 * Reads json data from a file in the folder <code>assets</code>.
 */
public class JsonData {

    File public_dir = new File(Environment.getExternalStorageDirectory() + File.separator + "HitApp" + File.separator + "Informatie");

    public File lastFileModified() {
        File fl = public_dir;
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {

                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choise = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choise = file;
                lastMod = file.lastModified();
            }
        }


        return choise;
    }



    private final Context context;

    public JsonData(Context myContext) {
        this.context = myContext;
    }

    public HitProject parse(int year) {
        return parse(readFromAssets(year));
    }

    public String readFromAssets(int year) {
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try {
            //br = new BufferedReader(new InputStreamReader(context.getAssets().open("hitapp." + year + ".json")));

            br = new BufferedReader(new FileReader( lastFileModified() ));


            String temp;
            while ((temp = br.readLine()) != null) {
                result.append(temp);
            }
        } catch (IOException e) {
            // TODO exception handling
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close(); // stop reading
                }
            } catch (IOException e) {
                // TODO exception handling
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    public HitProject parse(String json) {
        try {
            return parseHitProject(new JSONObject(json), "project");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO exception handling
            e.printStackTrace();
        }
        return null;
    }

    private HitProject parseHitProject(JSONObject object, String name) throws JSONException, ParseException {
        final JSONObject project = object.getJSONObject(name);
        final HitProject hitProject = new HitProject();
        hitProject.setId(project.getInt("id"));
        hitProject.setJaar(project.getInt("jaar"));
        hitProject.setVrijdag(asDate(project.getString("vrijdag")));
        hitProject.setMaandag(asDate(project.getString("maandag")));
        hitProject.setInschrijvingStartdatum(asDate(project.getString("inschrijvingStartdatum")));
        hitProject.setInschrijvingEinddatum(asDate(project.getString("inschrijvingEinddatum")));

        hitProject.setGebruikteIconen(parseIconen(project, "gebruikteIconen"));
        hitProject.setPlaatsen(parsePlaatsen(project, "plaatsen"));
        return hitProject;
    }

    private List<HitIcon> parseIconen(JSONObject object, String name) throws JSONException {
        final List<HitIcon> result = new ArrayList<HitIcon>();
        final JSONArray iconen = object.getJSONArray(name);
        for (int i = 0; i < iconen.length(); i++) {
            final JSONObject icon = iconen.getJSONObject(i);
            result.add(HitIcon.create(icon.getString("naam"), icon.getString("tekst")));
        }
        return result;
    }

    private List<HitPlaats> parsePlaatsen(JSONObject object, String name) throws JSONException, ParseException {
        final List<HitPlaats> result = new ArrayList<HitPlaats>();
        final JSONArray json = object.getJSONArray(name);
        for (int i = 0; i < json.length(); i++) {
            result.add(parsePlaats(json.getJSONObject(i)));
        }
        return result;
    }

    private HitPlaats parsePlaats(JSONObject plaats) throws JSONException, ParseException {
        final HitPlaats result = new HitPlaats();
        result.setId(plaats.getInt("id"));
        result.setNaam(plaats.getString("naam"));
        result.setHitCourantTekst(plaats.getString("hitCourantTekst"));
        result.setKampen(parseKampen(plaats, "kampen"));
        return result;
    }

    private List<HitKamp> parseKampen(JSONObject plaats, String name) throws JSONException, ParseException {
        final List<HitKamp> result = new ArrayList<HitKamp>();
        final JSONArray json = plaats.getJSONArray(name);
        for (int i = 0; i < json.length(); i++) {
            result.add(parseKamp(json.getJSONObject(i)));
        }
        return result;
    }

    private HitKamp parseKamp(JSONObject kamp) throws JSONException, ParseException {
        final HitKamp result = new HitKamp();
        result.setId(kamp.getInt("id"));
        result.setNaam(kamp.getString("naam"));
        result.setHitCourantTekst(kamp.getString("hitCourantTekst"));
        result.setMinimumLeeftijd(kamp.getInt("minimumLeeftijd"));
        result.setMaximumLeeftijd(kamp.getInt("maximumLeeftijd"));
        result.setSubgroep(kamp.getString("subgroep"));
        result.setDeelnamekosten(kamp.getInt("deelnamekosten"));
        result.setStartDatumTijd(asDateTime(kamp.getString("startDatumTijd")));
        result.setEindDatumTijd(asDateTime(kamp.getString("eindDatumTijd")));
        return result;
    }

    private Date asDate(String s) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(s);
    }

    private Date asDateTime(String s) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(s);
    }
}
