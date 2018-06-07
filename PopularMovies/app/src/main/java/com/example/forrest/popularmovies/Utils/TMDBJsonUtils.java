package com.example.forrest.popularmovies.Utils;

import android.util.ArrayMap;
import android.util.Log;

import com.example.forrest.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TMDBJsonUtils {

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

            /* Get the JSON object representing the movie */
            JSONObject movieJson = moviesJsonArray.getJSONObject(i);

            Movie movie = jsonObjectToMovie(movieJson);

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

        Movie movie = jsonObjectToMovie(movieJsonObj);


        return movie;
    }

    private static Movie jsonObjectToMovie(JSONObject movieJson) throws JSONException {
        Movie movie = new Movie();

        long id = movieJson.getLong(Constants.ID);
        movie.setId(id);

        movie.setTitle(movieJson.getString(Constants.TITLE));
        movie.setOriginalTitle(movieJson.getString(Constants.ORIGINAL_TITLE));

        String dateStr = movieJson.getString(Constants.RELEASE_DATE);
        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateStr);
            movie.setReleaseDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* Duration will be only on movie details. */
        if (movieJson.has(Constants.DURATION))
            movie.setDuration(movieJson.getInt(Constants.DURATION));


        movie.setPosterPath(movieJson.getString(Constants.POSTER_PATH));
        movie.setSynopsis(movieJson.getString(Constants.SYNOPSIS));
        movie.setRating(movieJson.getDouble(Constants.RATING));
        movie.setVoteCount(movieJson.getInt(Constants.VOTE_COUNT));
        movie.setPopularity(movieJson.getDouble(Constants.POPULARITY));

        return movie;

    }


    public static ArrayMap<String, String> getMovieTrailers(String tmdbJsonStr) throws JSONException {

        ArrayMap<String, String> trailersArrayMap = new ArrayMap<>();

        JSONObject tmdbJson = new JSONObject(tmdbJsonStr);

        /* Is there an error?
         *  If there is STATUS_CODE means something went wrong */
        if (tmdbJson.has(Constants.STATUS_CODE)) {
            Log.e(TAG, "ERROR: " + tmdbJsonStr);
            return null;
        }

        /* Get the list of movies */
        JSONArray trailersJsonArray = tmdbJson.getJSONArray(Constants.RESULTS);

        Log.d(TAG, "Parsing list of " + trailersJsonArray.length() + " trailers");

        /* Create a Movie object for each movie in the list. */
        for (int i = 0; i < trailersJsonArray.length(); i++) {

            /* Get the JSON object representing the movie */
            JSONObject trailerJson = trailersJsonArray.getJSONObject(i);

            String trailerName = trailerJson.getString("name");
            String trailerKey = trailerJson.getString("key");


            trailersArrayMap.put(trailerName, trailerKey);
        }

        return trailersArrayMap;
    }


    public static ArrayMap<String, String> getMovieReviews(String tmdbJsonStr) throws JSONException {

        if (tmdbJsonStr == null) return null;

        ArrayMap<String, String> trailersArrayMap = new ArrayMap<>();

        JSONObject tmdbJson = new JSONObject(tmdbJsonStr);

        /* Is there an error?
         *  If there is STATUS_CODE means something went wrong */
        if (tmdbJson.has(Constants.STATUS_CODE)) {
            Log.e(TAG, "ERROR: " + tmdbJsonStr);
            return null;
        }

        /* Get the list of movies */
        JSONArray reviewsJsonArray = tmdbJson.getJSONArray(Constants.RESULTS);

        Log.d(TAG, "Parsing list of " + reviewsJsonArray.length() + " reviews");

        /* Create a Movie object for each movie in the list. */
        for (int i = 0; i < reviewsJsonArray.length(); i++) {

            /* Get the JSON object representing the movie */
            JSONObject reviewJson = reviewsJsonArray.getJSONObject(i);

            String authorName = reviewJson.getString("author");
            String review = reviewJson.getString("content");


            trailersArrayMap.put(authorName, review);
        }

        return trailersArrayMap;
    }



}
