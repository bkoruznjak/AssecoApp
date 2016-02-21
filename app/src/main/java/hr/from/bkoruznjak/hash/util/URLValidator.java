package hr.from.bkoruznjak.hash.util;

import org.apache.commons.validator.routines.UrlValidator;

import timber.log.Timber;

/**
 * Created by Borna on 18.2.16.
 */
public class URLValidator {

    /**
     * Returns true if parameter urlString represents a valid URL
     * Valid shemes are http, https, ftp
     *
     * @param urlString
     */
    public static boolean isValidURL(String urlString) {
        Timber.i("got url: %s", urlString);
        //sad sad workaround
        if (urlString.startsWith("www")
                || (urlString.startsWith("WWW"))) {
            urlString = "http://".concat(urlString);
        }
        Timber.i("validating against %s", urlString);
        UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_2_SLASHES + UrlValidator.ALLOW_ALL_SCHEMES);
        return validator.isValid(urlString);
    }

    /**
     * Method adds http for special case of www starting url's
     *
     * @param urlString
     * @return fixed url String
     */
    public static String fixURL(String urlString) {
        Timber.d("fix URL called for %s", urlString);
        return urlString = (urlString.startsWith("www")
                || (urlString.startsWith("WWW"))) ? "http://".concat(urlString) : urlString;
    }
}
