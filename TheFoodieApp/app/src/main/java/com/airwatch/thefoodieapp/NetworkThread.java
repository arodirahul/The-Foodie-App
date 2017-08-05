package com.airwatch.thefoodieapp;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rarodi on 8/19/2015.
 */

public class NetworkThread extends Thread{
    @Override
    public void run() {
        HttpURLConnection urlc = null;
        try {
            URL url = new URL("http://www.espncricinfo.com/");
            urlc = (HttpURLConnection) url.openConnection();
            urlc.setConnectTimeout(30 * 1000);
            urlc.connect();
            if (urlc.getResponseCode() == 200) {
                Log.d("Ping", "Ping Successful");
            }
        } catch (MalformedURLException e1) {
            Log.e("Ping", "MalformedURLException", e1);
        } catch (IOException e) {
            Log.e("Ping", "IOException", e);
        } finally {
            urlc.disconnect();
        }

    }

}
