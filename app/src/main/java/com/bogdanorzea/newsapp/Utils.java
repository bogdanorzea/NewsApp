package com.bogdanorzea.newsapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    public static List<News> getNewsList(String url) {
        List data = new ArrayList<News>();

        String responseJson = null;
        try {
            responseJson = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem freeing up the InputStream resources.", e);
        }

        // TODO Parse the JSON response

        News dummy = new News();
        dummy.mTitle = responseJson;
        data.add(dummy);

        return data;
    }

    private static String makeHttpRequest(String requestUrl) throws IOException {
        String responseString = null;
        URL url = null;
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = urlConnection.getInputStream();
                responseString = readStream(in);
            } else {
                // Response code was not 200
                Log.e(LOG_TAG, "HTTP response code was: " + urlConnection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error while creating URL", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot open the connection to the URL.", e);
        } finally {
            // Clean-up resources in case of exception
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Function signature must include the IOException because of closing the InputStream
            if (in != null) {
                in.close();
            }
        }
        return responseString;
    }

    private static String readStream(InputStream in) {
        StringBuilder output = new StringBuilder();

        if (in != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            try {
                line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error reading the response stream.");
            }
        }

        return output.toString();
    }
}
