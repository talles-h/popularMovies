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
        ArrayList<Movie> moviesArrayList = new ArrayList<>();

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

    public static Movie getMovieFromDetailsJson(String movieJson) throws JSONException {
        JSONObject movieJsonObj = new JSONObject(movieJson);

        /* Is there an error?
         *  If there is STATUS_CODE means something went wrong */
        if (movieJsonObj.has(Constants.STATUS_CODE)) {
            Log.e(TAG, "ERROR: " + movieJson);
            return null;
        }

        Movie movie = new Movie();

        // Get movie ID
        movie.setId(movieJsonObj.getString(Constants.ID));

        /* Get title */
        movie.setTitle(movieJsonObj.getString(Constants.TITLE));

        /* Get original_title */
        movie.setOriginalTitle(movieJsonObj.getString(Constants.ORIGINAL_TITLE));

        /* Get release_date */
        String dateStr = movieJsonObj.getString(Constants.RELEASE_DATE);
        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateStr);
            movie.setReleaseDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* Get runtime */
        movie.setDuration(movieJsonObj.getInt(Constants.DURATION));

        /* Get poster_path or backdrop_path */
        movie.setPosterPath(movieJsonObj.getString(Constants.POSTER_PATH));

        /* Get overview */
        movie.setSynopsis(movieJsonObj.getString(Constants.SYNOPSIS));

        /* Get vote_average */
        movie.setRating(movieJsonObj.getDouble(Constants.RATING));

        /* Get vote_count */
        movie.setVoteCount(movieJsonObj.getInt(Constants.VOTE_COUNT));

        /* Get popularity */
        movie.setPopularity(movieJsonObj.getDouble(Constants.POPULARITY));

        return movie;
    }

}
