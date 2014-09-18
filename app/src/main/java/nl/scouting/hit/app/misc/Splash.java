package nl.scouting.hit.app.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import nl.scouting.hit.app.Main;
import nl.scouting.hit.app.R;

/**
 * Shows a splashscreen for a few seconds with logo, name and version.
 */
public class Splash extends Activity {



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    File JSON_file_meest_recent;
    File public_dir = new File(Environment.getExternalStorageDirectory() + File.separator + "HitApp" + File.separator + "Informatie");

    public File lastFileModified() {
        File fl = public_dir;
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                JSON_file_meest_recent = file;
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

        JSON_file_meest_recent = choise;
        return choise;
    }


    public boolean lastFileModified_check() {
        boolean aanwezig = false;

        File fl = public_dir;
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return true;
            }
        });
        long lastMod = Long.MIN_VALUE;

        for (File file : files) {
            if (file.lastModified() > lastMod) {
                lastMod = file.lastModified();

                aanwezig = true;
            }
        }
        return aanwezig;
    }




    public void doorsturen(){

        if(lastFileModified() != null){
            final int secondsDelayed = 3;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Update_Splash_Status("U wordt doorgestuurd");
                    startActivity(new Intent(Splash.this, Main.class));
                    finish();
                }
            }, secondsDelayed * 1000);
        }else{
            Update_Splash_Status("Fout: Geen informatie. App kan niet worden geopend");
            Log.e("FOUT", "Geen JSON file beschikbaar, App kan niet worden geopend");
        }



    }

    public void Update_Splash_Status(String new_status){
        TextView status_tonen = (TextView) findViewById(R.id.status);
        status_tonen.setText(new_status);

        Log.d("Splash", "Status aangepast: " + new_status);
    }

    public void DownloadJSON(String URL) {
        try {

            String TAG = "JSON Downloader";
            File dir = public_dir;
            dir.mkdirs();

            String timeStamp;


            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
            Calendar cal = Calendar.getInstance();
            timeStamp = dateFormat.format(cal.getTime());

            String FileName = "JSON_HITAPP_" + timeStamp + "_.JSON";


            URL url = new URL(URL.toString());
            File file = new File(dir + "/" + FileName);

            long startTime = System.currentTimeMillis();
            Log.d(TAG, "download begining");
            Log.d(TAG, "download url:" + url);
            Log.d(TAG, "downloaded file name:" + FileName);
            Log.d(TAG, "downloaded complete:" + dir + "/" + FileName);


            Update_Splash_Status("Bezig met verbinden");
                        /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

                        /*
                         * Define InputStreams to read from the URLConnection.
                         */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            Update_Splash_Status("Downloaden van informatie");

                        /*
                         * Read bytes to the Buffer until there is nothing more to read(-1).
                         */
            ByteArrayBuffer baf = new ByteArrayBuffer(150);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

                        /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();

            if( lastFileModified_check() ) {
                Update_Splash_Status("Informatie opgehaald");
                doorsturen();
            }else{
                Update_Splash_Status("Fout: Geen informatie. App kan niet worden geopend");
                Log.e("FOUT", "Check Download Function, Geen JSON file beschikbaar, App kan niet worden geopend");
                //Log.e("FOUT", "Filename check: "+lastFileModified().getName() );

            }

            Log.d(TAG, "download ready in"
                    + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec");

        } catch (IOException e) {
            Log.e("JSON Downloader", "Error: " + e);
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
        StrictMode.setThreadPolicy(tp);

        // Remove title bar and notification bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Setup splashscreen
        setContentView(R.layout.splash);

        // Retrieve version from manifest
        final TextView appVersion = (TextView) findViewById(R.id.app_version);
        appVersion.setText(getApplicationVersion());


        //Download nieuwe JSON data
        if(isNetworkAvailable()){
            DownloadJSON("https://hit.scouting.nl/index.php?option=com_kampinfo&task=hitapp.generate");
        }else{
                                        //Indien geen internet check voor lokale file uit verleden

                                    File[] contents = public_dir.listFiles();
                        // the directory file is not really a directory..
                                    if (contents == null) {
                                        Update_Splash_Status("Internet verbinding is nodig, probeer het later opnieuw");
                                        Log.e("FOUT", "Geen directory");
                                    }
                        // Folder is empty
                                    else if (contents.length == 0) {
                                        Update_Splash_Status("Internet verbinding is nodig, probeer het later opnieuw");
                                        Log.e("FOUT", "Folder leeg");

                                    }
                        // Folder contains files
                                    else {

                                        if( lastFileModified_check() ){

                                            //Bestand was al gedownload maar nu geen internet
                                            Update_Splash_Status("Oude informatie gevonden");
                                            doorsturen();
                                            Update_Splash_Status("Oude informatie gevonden, u wordt doorgestuurd");

                                        }else{
                                            Update_Splash_Status("Internet verbinding is nodig, probeer het later opnieuw");
                                        }

                                    }




        }


    }

    /**
     * Retrieves version from manifest.
     *
     * @return full text for textview with version indication
     */
    private String getApplicationVersion() {
        String versionName;
        final Context context = getApplicationContext();
        try {
            versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "?";
        }
        return context.getString(R.string.app_version, versionName);
    }

}
