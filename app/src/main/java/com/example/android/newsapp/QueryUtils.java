package com.example.android.newsapp;

import android.text.TextUtils;
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

/**
 * Created by Jaren Lynch on 8/20/2016.
 */
public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    /**
     * @param requestURL api address
     * @return list of article objects that have been built up from parsing a JSON response
     */
    public static List<Article> fetchArticleData(String requestURL) {
        //Create URL object
        URL url = createUrl(requestURL);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        //Extract relevant fields from the JSON response and create an Article object.
        List<Article> article = extractArticles(jsonResponse);

        //Return the article
        return article;
    }

    /**
     * @param stringUrl given string url
     * @return new URL object from the given string url
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating url", e);
        }
        return url;
    }

    /**
     * @param url given url
     * @return Make an HTTP request to the given url and return a String as the response.
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, then return early
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request was successful (response code 200),
            //then read the input stream and parse the response.

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem receiving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * @param inputStream = string obtained from httpRequest.
     * @return String which contains the whole JSON response from the server
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.defaultCharset().forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Article> extractArticles(String articleJSON) {
        //If the JSON string is empty or null, then return early.
        if(TextUtils.isEmpty(articleJSON)) {
            return null;
        }
        //Create an empty ArrayList that we can start adding articles to.
        List<Article> articles = new ArrayList<>();

        //Try to parse jsonResponse. If json is messed up, a JSONException will be thrown. Catches exception.
        try {
            //Create a JSONObject from the JSON response string
            JSONObject jsonRootObject = new JSONObject(articleJSON);

            //Extract tje JSON Array associated with the key called "response",
            // which represents a list of the top articles.
            JSONArray jsonArray = jsonRootObject.optJSONArray("response");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentArticle = jsonArray.getJSONObject(i);
                JSONObject editorPicks = currentArticle.getJSONObject("editorsPicks");

                String title = editorPicks.optString("webTitle");
                String url = editorPicks.optString("webUrl");
                articles.add(new Article(title, url));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the article JSON results");
        }

        //return the list of articles
        return articles;
    }
}
