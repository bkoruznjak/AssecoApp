package hr.from.bkoruznjak.hash.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hr.from.bkoruznjak.hash.model.pojo.Website;
import timber.log.Timber;

/**
 * Created by Borna on 19.2.16.
 */
public class DbHandler extends SQLiteOpenHelper implements WebsiteHandler {

    private static final String DATABASE_NAME = "hashDb";
    private static final int DATABASE_VERSION = 1;

    // Website table name
    private static final String TABLE_WEBSITES = "websites";

    // Website Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_WEBSITE_URL = "website_url";
    private static final String KEY_WEBSITE_HASH = "website_hash";
    private static final String KEY_WEBSITE_STORAGE = "website_storage";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createWebsiteTable = "CREATE TABLE "
                + TABLE_WEBSITES
                + "("
                + KEY_ID + " VARCHAR(36) PRIMARY KEY,"
                + KEY_WEBSITE_URL + " TEXT,"
                + KEY_WEBSITE_HASH + " TEXT,"
                + KEY_WEBSITE_STORAGE + " TEXT"
                + ")";

        db.execSQL(createWebsiteTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEBSITES);

        // Create tables again
        onCreate(db);
    }

    /**
     * Method adds website to database
     *
     * @param website
     */
    @Override
    public void addWebsite(Website website) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, website.getId());
        values.put(KEY_WEBSITE_URL, website.getWebsiteURL());
        values.put(KEY_WEBSITE_HASH, website.getHashCode());
        values.put(KEY_WEBSITE_STORAGE, website.getStorage());

        // Inserting Row
        db.insert(TABLE_WEBSITES, null, values);
        db.close(); // Closing database connection
        Timber.i("%s successfully added to SQLite", website.getWebsiteURL());
    }

    /**
     * Method gets website based on website URL
     *
     * @param websiteURL
     * @return Website
     */
    @Override
    public Website getWebsite(String websiteURL) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_WEBSITES, new String[]{KEY_ID,
                            KEY_WEBSITE_URL, KEY_WEBSITE_HASH, KEY_WEBSITE_STORAGE}, KEY_WEBSITE_URL + "=?",
                    new String[]{websiteURL}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            Website website = new Website(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));
            Timber.i("Found website %s with generated hash: %s", website.getWebsiteURL(), website.getHashCode());
            // return website
            return website;
        } catch (Exception e) {
            Timber.e("Exception in getWebsite: %s", e.toString());
        } finally {
            cursor.close();
            db.close();
        }
        Timber.i("Found 0 results with %s URL", websiteURL);
        return null;
    }
}
