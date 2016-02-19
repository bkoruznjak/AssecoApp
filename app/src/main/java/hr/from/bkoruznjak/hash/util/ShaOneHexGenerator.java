package hr.from.bkoruznjak.hash.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import timber.log.Timber;

/**
 * Created by Borna on 18.2.16.
 */
public class ShaOneHexGenerator {

    /**
     * Method returns SHA1 hex string based on input String parameter
     *
     * @param input String
     * @return hex SHA1 String of given input
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String generateSHA1Hash(String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }
        Timber.i("SHA1 generated: %s", hexStr);
        return hexStr;
    }

    /**
     * Method returns true if the first byte of the SHA1 hex string is even
     *
     * @param hashString
     */
    public boolean isHashByteEven(String hashString) {
        int firstByteInt = Integer.valueOf(hashString.substring(0, 1), 16);
        return (firstByteInt % 2 == 0);
    }
}
