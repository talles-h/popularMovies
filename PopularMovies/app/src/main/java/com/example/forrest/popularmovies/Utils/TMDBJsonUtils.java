package com.example.forrest.popularmovies.Utils;

import android.util.Log;

import com.example.forrest.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class TMDBJsonUtils {

    private static final String TAG = TMDBJsonUtils.class.getSimpleName();

    public static ArrayList<Movie> getMoviesFromJsonList(String tmdbJsonStr) throws JSONException {
        ArrayList<Movie> moviesArrayList = new ArrayList<Movie>();

        JSONObject tmdbJson = new JSONObject(tmdbJsonStr);

        /* Is there an error?
        *  If there is STATUS_CODE means something went wrong */
        if (tmdbJson.has(Constants.STATUS_CODE)) {
            Log.e(TAG, "ERROR: " + tmdbJsonStr);
            return null;
        }

        /* Get the list of movies */
        JSONArray moviesJsonArray = tmdbJson.getJSONArray(Constants.RESULTS);

        Log.d(TAG, "Parsing list of " + moviesJsonArray.length() + " movies");

        /* Create a Movie object for each movie in the list. */
        for (int i = 0; i < moviesJsonArray.length(); i++) {

            Movie movie = new Movie();

            /* Get the JSON object representing the movie */
            JSONObject movieJson = moviesJsonArray.getJSONObject(i);

            String str = movieJson.getString(Constants.ID);
            movie.setId(str);

            movie.setTitle(movieJson.getString(Constants.ORIGINAL_TITLE));

            Float rating = (float) movieJson.getDouble(Constants.RATING);
            movie.setRating(rating);

            String dateStr = movieJson.getString(Constants.RELEASE_DATE);
            try {
                Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateStr);
                movie.setReleaseDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            movie.setSynopsis(movieJson.getString(Constants.SYNOPSIS));
            movie.setPosterPath(movieJson.getString(Constants.POSTER_PATH));

            moviesArrayList.add(movie);
        }

        return moviesArrayList;
    }

}
