package hr.from.bkoruznjak.hash.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import hr.from.bkoruznjak.hash.MainActivity;
import hr.from.bkoruznjak.hash.model.DbHandler;
import hr.from.bkoruznjak.hash.model.SharedPrefsHandler;
import hr.from.bkoruznjak.hash.model.enums.StorageEnum;
import hr.from.bkoruznjak.hash.model.pojo.Website;
import timber.log.Timber;

/**
 * Created by Borna on 18.2.16.
 */
public class WebsiteReader extends AsyncTask<Void, Void, Void> {

    private String websiteURL;
    private Context context;
    private String shaOneHex;
    private String storage;
    private DbHandler dbHandler;
    private SharedPrefsHandler sharedPrefsHandler;
    private Website website;

    public WebsiteReader(Context context, String websiteURL) {
        this.context = context;
        this.websiteURL = websiteURL;
        this.dbHandler = new DbHandler(context);
        this.sharedPrefsHandler = new SharedPrefsHandler(context);
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Timber.d("do in background called...");
        InputStream webStream = getWebsiteStream(websiteURL);
        Timber.d("webStream success for %s", websiteURL);
        shaOneHex = convertWebsiteStream(webStream);
        Timber.d("SHA1 success");
        if (new ShaOneHexGenerator().isHashByteEven(shaOneHex)) {
            storage = StorageEnum.DATABASE.toString();
            website = new Website(websiteURL, shaOneHex, storage);
            dbHandler.addWebsite(website);
        } else {
            storage = StorageEnum.SHARED_PREFERENCES.toString();
            website = new Website(websiteURL, shaOneHex, storage);
            sharedPrefsHandler.addWebsite(website);
        }
        Timber.i("website stored in %s", storage);
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Timber.i("onPostExecute");
        MainActivity.updateWebsiteTextView(website);
        new Toasty(context, "stored new website", 10, Typeface.DEFAULT).show();
    }

    /**
     * Method returns inputStream of a website provided by the webURLString
     *
     * @param webURLString
     * @return inputStream
     */
    private InputStream getWebsiteStream(String webURLString) {
        InputStream webStreamData = null;
        try {
            //get the riddles file
            URL webURL = new URL(URLValidator.fixURL(webURLString));
            HttpURLConnection urlConnection = (HttpURLConnection) webURL.openConnection();
            webStreamData = urlConnection.getInputStream();
        } catch (Exception e) {
            Timber.e("Website retrieval fail caused by %s", e.toString());
        }
        return webStreamData;
    }

    /**
     * Method converts the inputStream into a String and retrieves the SHA1 hex string
     *
     * @param inputStream
     * @return SHA1 hex String
     */
    private String convertWebsiteStream(InputStream inputStream) {
        StringBuilder siteContentBuilder = new StringBuilder();
        try {
            //try with res min API lvl 19 ;(
            BufferedReader breader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String dataLine;
                while ((dataLine = breader.readLine()) != null) {
                    siteContentBuilder.append(dataLine);
                }
            } finally {
                breader.close();
            }
        } catch (IOException ioEx) {
            Timber.e("Buffer reading fail caused by %s", ioEx.toString());
        }

        ShaOneHexGenerator hash = new ShaOneHexGenerator();
        String hashString = "";
        try {
            hashString = hash.generateSHA1Hash(siteContentBuilder.toString());
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException mcEx) {
            Timber.e("Hashcode conversion fail caused by %s", mcEx.toString());
            mcEx.printStackTrace();
        }
        Timber.i("SHA1 hex: %s", hashString);
        return hashString;
    }

}
