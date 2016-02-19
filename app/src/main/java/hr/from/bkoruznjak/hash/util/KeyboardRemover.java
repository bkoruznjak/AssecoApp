package hr.from.bkoruznjak.hash.util;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import timber.log.Timber;

/**
 * Created by Borna on 11.6.15.
 */
public class KeyboardRemover {

    Context myContext;

    public KeyboardRemover(Context myContext) {
        this.myContext = myContext;
    }

    /**
     * Method for hiding the keyboard when user touches the screen outside the
     * keyboard
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        Timber.i("hide keyboard called");
        InputMethodManager inputMethodManager = (InputMethodManager) myContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}