package hr.from.bkoruznjak.hash.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.lang3.StringUtils;

import hr.from.bkoruznjak.hash.model.enums.StorageEnum;
import hr.from.bkoruznjak.hash.model.pojo.Website;
import timber.log.Timber;

/**
 * Created by Borna on 19.2.16.
 */
public class SharedPrefsHandler implements WebsiteHandler {

    private static final String PREFERENCES_NAME = "hashPrefs";
    private final Context context;
    private final SharedPreferences sharedPrefs;

    public SharedPrefsHandler(Context context) {
        this.context = context;
        sharedPrefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    /**
     * Method adds website to shared preferences
     *
     * @param website
     */
    @Override
    public void addWebsite(Website website) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(website.getWebsiteURL(), website.getHashCode());
        editor.apply();
        Timber.i("%s added to shared preferences", website.getWebsiteURL());
    }

    /**
     * Method gets website based on website URL
     *
     * @param websiteURL
     * @return Website
     */
    @Override
    public Website getWebsite(String websiteURL) {
        String hashString = sharedPrefs.getString(websiteURL, null);
        if (StringUtils.isBlank(hashString)) {
            Timber.i("%s not found in Shared Preferences", websiteURL);
        } else {
            Timber.i("%s found in Shared Preferences", websiteURL);
        }
        return new Website(websiteURL, hashString, StorageEnum.SHARED_PREFERENCES.toString());
    }
}
