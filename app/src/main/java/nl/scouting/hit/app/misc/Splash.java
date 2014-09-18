package nl.scouting.hit.app.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import java.io.OutputStream;
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
            }, secondsDelayed * 150);
        }else{
            Update_Splash_Status("Fout: Geen informatie. App kan niet worden geopend");
            Log.e("FOUT", "Geen JSON file beschikbaar, App kan niet worden geopend");
        }



    }

    public void Update_Splash_Status(String new_status){

        final String update_string = new_status;

        this.runOnUiThread(new Runnable() {
            public void run() {


                TextView status_tonen = (TextView) findViewById(R.id.status);
                status_tonen.setText(update_string);

                Log.d("Splash", "Status aangepast: " + update_string);

            }
        });
    }


    String TAG = "JSON Downloader";

        private class DownloadFile extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... sUrl) {
                try {

                    URL url = new URL(sUrl[0]);

                    Update_Splash_Status("Bezig met verbinden");

                    URLConnection connection = url.openConnection();
                    connection.connect();
                    Log.i(TAG, "connected");

                    Update_Splash_Status("Verbonden");

                    // this will be useful so that you can show a typical 0-100% progress bar

                    Update_Splash_Status("Opzoeken informatie");
                    int fileLength = connection.getContentLength();



                    File dir = public_dir;
                    dir.mkdirs();

                    String timeStamp;


                    DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HH");
                    Calendar cal = Calendar.getInstance();
                    timeStamp = dateFormat.format(cal.getTime());

                    String FileName = "JSON_HITAPP_" + timeStamp + "_.JSON";




                    // download the file


                    InputStream input = new BufferedInputStream(url.openStream());
                    Log.i(TAG, "input set");
                    OutputStream output = new FileOutputStream(dir + "/" + FileName);
                    Log.i(TAG, "output set");

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;



                        long nog_te_doen = fileLength - total;
                        long dg = nog_te_doen/fileLength;

                        Log.i(TAG, "Nu gedaan: "+total+", geheel: "+fileLength+" percentage: "+(dg * 100));
                        long voortgang = 0;

                        if(fileLength < 0) {
                            voortgang = 0;
                        }else{
                            voortgang = (dg * 100);
                        }

                        publishProgress((int) voortgang);

                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                }catch(Exception e){
                    Log.e("ERROR_TAG", e.getMessage());
                    return "fine";
                }
                String s ="fine";
                Log.i(TAG, "end download");
                return s;
            }




        protected void onProgressUpdate(Integer... progress) {
            Log.i(TAG, "Progress update: "+progress[0]);

            if(progress[0] < 1){
                Update_Splash_Status("Bezig met downloaden");
            }else{
                Update_Splash_Status("Downloaden ("+progress[0].toString()+"%)");
            }


        }

            protected void onPostExecute(String s) {

            Log.i("Download geslaagd", "Status: "+s.toString());
                Update_Splash_Status("Informatie opgehaald");

            if( lastFileModified_check() ) {
                Update_Splash_Status("Informatie opgehaald");
                doorsturen();
            }else{
                Update_Splash_Status("Fout: Geen informatie. App kan niet worden geopend");
                Log.e("FOUT", "Check Download Function, Geen JSON file beschikbaar, App kan niet worden geopend");
                //Log.e("FOUT", "Filename check: "+lastFileModified().getName() );

            }

        }
    }






    public void DownloadJSON(String URL) {
        try {

            String TAG = "JSON Downloader";
            File dir = public_dir;
            dir.mkdirs();

            String timeStamp;


            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HH");
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








    //Kopieer de Backup JSON Export vanuit Assets

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;


        try {
            files = assetManager.list("");
            Log.i("Array assets", "Filename: "+ files[0]);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {

                if( filename.equals("hitapp.2014.json") ) {

                    in = assetManager.open(filename);

                    Log.i("Asset copy", "Filename: " + filename);

                    File outFile = new File(public_dir, filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                }
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
        //StrictMode.setThreadPolicy(tp);

        // Remove title bar and notification bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Setup splashscreen
        setContentView(R.layout.splash);

        // Retrieve version from manifest
        final TextView appVersion = (TextView) findViewById(R.id.app_version);
        appVersion.setText(getApplicationVersion());


        //Kopieer de Backup JSON export
        File dir = public_dir;
        dir.mkdirs();

        copyAssets();




        //Download nieuwe JSON data
        if(isNetworkAvailable()){
            //DownloadJSON("https://hit.scouting.nl/index.php?option=com_kampinfo&task=hitapp.generate");


            //Indien er in het laatste uur al een JSON Export is gedownload dan openen we die gewoon
                                DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HH");
                                Calendar cal = Calendar.getInstance();

                                String timeStamp;
                                timeStamp = dateFormat.format(cal.getTime());

                                String FileName = "JSON_HITAPP_" + timeStamp + "_.JSON";


                                File file = new File(public_dir + "/" + FileName);


                                if(file.exists() && !file.isDirectory()){
                                    Update_Splash_Status("Recente informatie gevonden");
                                    doorsturen();
                                    Update_Splash_Status("U wordt doorgestuurd");
                                }else {

                                    DownloadFile downloadFile = new DownloadFile();
                                    downloadFile.execute("https://hit.scouting.nl/index.php?option=com_kampinfo&task=hitapp.generate");
                                }

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
                                            Update_Splash_Status("U wordt doorgestuurd");

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
