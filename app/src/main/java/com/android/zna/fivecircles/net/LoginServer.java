package com.android.zna.fivecircles.net;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ZNA on 2014/11/30.
 */
public class LoginServer {

    public static final String SERVER_URL = "http://fivecircle.sturgeon.mopaas.com/service.jsp";
    private static final String DEBUG_TAG = "ZNA_DEBUG";
    private static final String ACTION_LOGIN = "login";


    public static class CheckStatus {
        public final static int SUCCESS = 0;
        public final static int FAIL = 1;
        public final static int NET_ERROR = 2;
        public final static int DATA_ERROR = 3;
    }

    /**
     * check username and passwd
     */
    public static int checkAuth(String username, String passwd) {
        //TO-DO:set tokencache


        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(SERVER_URL + "?action=" + ACTION_LOGIN + "&username=" + username +
                    "&passwd=" + passwd);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();
            // Convert the InputStream into a string
            String contentAsString = readIt(is, 100);
            if (contentAsString != null && contentAsString.trim().equals("success")) {
                return CheckStatus.SUCCESS;
            } else {
                return CheckStatus.FAIL;
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            e.printStackTrace();
            return CheckStatus.NET_ERROR;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
