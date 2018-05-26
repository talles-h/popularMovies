package com.example.forrest.popularmovies.Utils;

import android.net.Uri;
import android.util.Log;

import com.example.forrest.popularmovies.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static URL getPopularMoviesUrl() {
        URL url = buildMovieDbURL(Constants.POPULAR_MOVIES_PATH);

        return url;
    }

    public static URL getTopRatedMoviesUrl() {

        URL url = buildMovieDbURL(Constants.TOP_RATED_MOVIES_PATH);

        return url;
    }

    public static URL buildPosterURL(String posterFile) {
        Uri builtUri = Uri.parse(Constants.POSTER_BASE_URL + Constants.POSTER_SIZE + posterFile)
                .buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieDetailsURL(String movieId) {
        Uri builtUri = Uri.parse(Constants.MOVIE_DB_BASE_URL + Constants.MOVIE_DB_MOVIE
                + "/" + movieId)
                .buildUpon()
                .appendQueryParameter(Constants.KEY_PARAM, Constants.API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static URL buildMovieDbURL(String path) {
        Uri builtUri = Uri.parse(Constants.MOVIE_DB_BASE_URL + path).buildUpon()
                .appendQueryParameter(Constants.KEY_PARAM, Constants.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }




    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        Log.d(TAG, "Get: " + url.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                Log.d(TAG, "Got response from HTTP URL");
                return scanner.next();
            } else {
                Log.e(TAG, "No response from HTTP request");
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}