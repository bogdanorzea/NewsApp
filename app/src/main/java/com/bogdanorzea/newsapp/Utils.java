package com.bogdanorzea.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    static List<News> getNewsList(String url) {
        List data = new ArrayList<News>();

        String response = null;
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem freeing up the InputStream resources.", e);
        }

        JSONObject rootJson = null;
        try {
            rootJson = new JSONObject(response);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the root JSON response.", e);
            e.printStackTrace();
            return data;
        }

        JSONObject responseJsonObject;
        JSONArray resultsArray;
        try {
            responseJsonObject = rootJson.getJSONObject("response");
            resultsArray = responseJsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem getting the results array.", e);
            e.printStackTrace();
            return data;

        }

        int len = resultsArray.length();
        for (int i = 0; i < len; i++) {
            String currentId = null;
            JSONObject currentResult = null;
            News currentNews = null;

            try {
                currentResult = resultsArray.getJSONObject(i);

                currentId = currentResult.getString("id");
                String currentSectionPublicationDate = currentResult.getString("webPublicationDate");
                String currentUrl = currentResult.getString("webUrl");

                currentNews = new News(currentId, currentUrl, currentSectionPublicationDate);
                currentNews.setSectionName(currentResult.getString("sectionName"));

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the news with id: " + currentId, e);
                continue;
            }

            // Add additional fields to the news
            try {
                if (currentResult.has("fields")) {
                    JSONObject fieldsJsonObject = currentResult.getJSONObject("fields");

                    if (fieldsJsonObject.has("headline")) {
                        currentNews.setHeadline(fieldsJsonObject.getString("headline"));
                    }
                    if (fieldsJsonObject.has("trailText")) {
                        currentNews.setTrailText(fieldsJsonObject.getString("trailText"));
                    }
                    if (fieldsJsonObject.has("thumbnail")) {
                        currentNews.setThumbnail(fieldsJsonObject.getString("thumbnail"));
                    }
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem adding additional fields to the news with id: " + currentId, e);
            }

            // Add additional tags to the news
            List<String> contributors = new ArrayList<>();
            try {
                if (currentResult.has("tags")) {
                    JSONArray tagsJsonArray = currentResult.getJSONArray("tags");
                    int lt = tagsJsonArray.length();
                    for (int j = 0; j < lt; j++) {
                        JSONObject currentTag = tagsJsonArray.getJSONObject(j);
                        String currentType = currentTag.getString("type");
                        if (currentType.equals("contributor")) {
                            contributors.add(currentTag.getString("webTitle"));
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem adding additional tags to the news with id: " + currentId, e);
            }
            currentNews.setContributors(contributors);

            data.add(currentNews);
        }

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
