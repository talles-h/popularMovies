package com.example.forrest.popularmovies.Utils;

import android.util.Log;

import com.example.forrest.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class TMDBJsonUtils {

    private static final String TAG = TMDBJsonUtils.class.getSimpleName();

    private static final String STATUS_CODE = "status_code";
    private static final String TOTAL_RESULTS = "total_results";
    private static final String RESULTS = "results";
    private static final String RATING_COUNT = "vote_count";
    private static final String ID = "id";
    private static final String RATING = "vote_average";
    private static final String TITLE = "title";
    private static final String POPULARITY = "popularity";
    private static final String POSTER_PATH = "poster_path";
    private static final String ORIGINAL_LANGUAGE = "original_language";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String SYNOPSIS = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p";
    private static final String POSTER_SIZE = "/w185";

    public static ArrayList<Movie> getMoviesFromJsonList(String tmdbJsonStr) throws JSONException {
        ArrayList<Movie> moviesArrayList = new ArrayList<Movie>();

        JSONObject tmdbJson = new JSONObject(tmdbJsonStr);

        /* Is there an error?
        *  If there is STATUS_CODE means something went wrong */
        if (tmdbJson.has(STATUS_CODE)) {
            Log.e(TAG, "ERROR: " + tmdbJsonStr);
            return null;
        }

        /* Get the list of movies */
        JSONArray moviesJsonArray = tmdbJson.getJSONArray(RESULTS);

        Log.d(TAG, "Parsing list of " + moviesJsonArray.length() + " movies");

        /* Create a Movie object for each movie in the list. */
        for (int i = 0; i < moviesJsonArray.length(); i++) {

            Movie movie = new Movie();

            /* Get the JSON object representing the movie */
            JSONObject movieJson = moviesJsonArray.getJSONObject(i);

            String str = movieJson.getString(ID);
            movie.setId(str);

            movie.setTitle(movieJson.getString(ORIGINAL_TITLE));

            Float rating = (float) movieJson.getDouble(RATING);
            movie.setRating(rating);

            String dateStr = movieJson.getString(RELEASE_DATE);
            try {
                Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateStr);
                movie.setReleaseDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            movie.setSynopsis(movieJson.getString(SYNOPSIS));
            movie.setPosterPath(POSTER_BASE_URL + POSTER_SIZE + movieJson.getString(POSTER_PATH));

            moviesArrayList.add(movie);
        }

        return moviesArrayList;

    }

}
