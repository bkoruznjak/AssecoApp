package hr.from.bkoruznjak.hash;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import hr.from.bkoruznjak.hash.model.DbHandler;
import hr.from.bkoruznjak.hash.model.SharedPrefsHandler;
import hr.from.bkoruznjak.hash.model.pojo.Website;
import hr.from.bkoruznjak.hash.util.CrashReportingTree;
import hr.from.bkoruznjak.hash.util.KeyboardRemover;
import hr.from.bkoruznjak.hash.util.Toasty;
import hr.from.bkoruznjak.hash.util.URLValidator;
import hr.from.bkoruznjak.hash.util.WebsiteReader;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static TextView textWebsiteURL;
    private static TextView textWebsiteHash;
    private static TextView textWebsiteLocation;
    private EditText inputURL;
    private Button submitURLbutton;
    private boolean isValidURL;
    private DbHandler dbHandler;
    private SharedPrefsHandler sharedPrefsHandler;

    public static void updateWebsiteTextView(Website website) {
        textWebsiteURL.setText(website.getWebsiteURL());
        textWebsiteHash.setText(website.getHashCode());
        textWebsiteLocation.setText(website.getStorage());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    /**
     * Method initializes the Activity
     */
    public void initialize() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
        setViews();
        setListeners();
        dbHandler = new DbHandler(this);
        sharedPrefsHandler = new SharedPrefsHandler(this);
    }

    /**
     * Method sets all the views
     */
    public void setViews() {
        Timber.d("setting views");
        setContentView(R.layout.activity_main);
        inputURL = (EditText) findViewById(R.id.editTextURLInput);
        submitURLbutton = (Button) findViewById(R.id.buttonSubmitURL);
        textWebsiteURL = (TextView) findViewById(R.id.textViewWebsiteUrl);
        textWebsiteHash = (TextView) findViewById(R.id.textViewWebsiteHash);
        textWebsiteLocation = (TextView) findViewById(R.id.textViewWebsiteLocation);
    }

    /**
     * Method sets all the view listeners
     */
    public void setListeners() {
        Timber.d("setting listeners");
        //inputUrl on lose focus listener
        final KeyboardRemover keyboardRemover = new KeyboardRemover(this);
        inputURL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Timber.i("url input lost focus");
                    handleInputText();
                    keyboardRemover.hideKeyboard(v);
                }
            }
        });

        //inputUrl enter and return key listener
        inputURL.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Timber.i("keyboard event done");
                    handleInputText();
                    inputURL.clearFocus();
                }
                return false;
            }
        });

        //submit button on click listener
        submitURLbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle inputURl logic
                Timber.i("submit button clicked");
                handleInputText();
                if (isValidURL) {
                    //check if exists in database + shared prefs
                    Website dbWebsite = dbHandler.getWebsite(inputURL.getText().toString());
                    Website shWebsite = sharedPrefsHandler.getWebsite(inputURL.getText().toString());
                    if (dbWebsite != null) {
                        updateWebsiteTextView(dbWebsite);
                        new Toasty(getApplicationContext(), "Website exists in database", 10, Typeface.DEFAULT).show();
                        sleepButton(submitURLbutton, 5000);
                    } else if (StringUtils.isNotBlank(shWebsite.getHashCode())) {
                        updateWebsiteTextView(shWebsite);
                        new Toasty(getApplicationContext(), "Website exists in shared preferences", 10, Typeface.DEFAULT).show();
                        sleepButton(submitURLbutton, 5000);
                    } else {
                        new WebsiteReader(getApplicationContext(), inputURL.getText().toString()).execute();
                    }
                } else if (StringUtils.isBlank(inputURL.getText())) {
                    new Toasty(getApplicationContext(), "URL field empty", 10, Typeface.DEFAULT).show();
                } else {
                    new Toasty(getApplicationContext(), "Input URL is invalid", 10, Typeface.DEFAULT).show();
                }
            }
        });
    }

    /**
     * Method sets the appropriate color based on input url validation
     */
    private void handleInputText() {
        // check if URL is valid
        if (StringUtils.isBlank(inputURL.getText().toString())) {
            Timber.i("input blank, color to white");
            //getColor(colorInt, theme) needs min API lvl 23
            inputURL.setBackgroundColor(getResources().getColor(R.color.colorNoInput));
            setIsValidURL(false);
        } else {
            if (URLValidator.isValidURL(inputURL.getText().toString())) {
                Timber.i("input ok, color to turquoise");
                inputURL.setBackgroundColor(getResources().getColor(R.color.colorOkInput));
                setIsValidURL(true);
            } else {
                Timber.i("input bad, color to salmon");
                inputURL.setBackgroundColor(getResources().getColor(R.color.colorBadInput));
                setIsValidURL(false);
            }
        }
    }

    public boolean isValidURL() {
        return isValidURL;
    }

    public void setIsValidURL(boolean isValidURL) {
        this.isValidURL = isValidURL;
    }

    /**
     * puts Button to sleep/disables it for desired ammount of miliseconds
     *
     * @param button
     * @param timeToSleep
     */
    private void sleepButton(final Button button, long timeToSleep) {
        button.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(true);
            }
        }, timeToSleep);
    }
}
