package com.example.forrest.popularmovies.loaders;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.ArrayMap;
import android.util.Log;

import com.example.forrest.popularmovies.Movie;
import com.example.forrest.popularmovies.Utils.DbUtils;
import com.example.forrest.popularmovies.Utils.NetworkUtils;
import com.example.forrest.popularmovies.Utils.TMDBJsonUtils;
import com.example.forrest.popularmovies.data.MoviesContract.MovieEntry;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class MovieDetailsLoader extends AsyncTaskLoader<Movie> {

    private static final String TAG = "MovieDetailsLoader";

    public static final String[] FAVORITE_MOVIES_PROJECTION = {
            MovieEntry._ID,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_DURATION,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_SYNOPSIS,
            MovieEntry.COLUMN_RATING,
            MovieEntry.COLUMN_VOTE_COUNT,
            MovieEntry.COLUMN_POPULARITY,
            MovieEntry.COLUMN_TRAILERS_JSON,
            MovieEntry.COLUMN_POSTER,
            MovieEntry.COLUMN_IS_FAVORITE
    };


    private Movie mMovie;
    private long mMovieId;
    private Context mContext;

    public MovieDetailsLoader(Context context, long movieId) {
        super(context);
        mContext = context;
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (mMovieId < 0) {
            return;
        }

        if (mMovie != null) {
            deliverResult(mMovie);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public Movie loadInBackground() {
        Movie movie = null;

        // Search in the saved movies database first
        String stringId = Long.toString(mMovieId);
        Uri uri = MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        Cursor cursor = mContext.getContentResolver().query(uri,
                FAVORITE_MOVIES_PROJECTION, null, null,
                MovieEntry.COLUMN_RATING);

        if (cursor != null && cursor.moveToFirst()) {
            Log.d(TAG, "Movie already in database");
            movie = cursorToMovie(cursor);

            movie.setReviewsJson(getReviewsJson());

        } else {
            Log.d(TAG, "Movie NOT in database");
            String jsonMoviesResponse = null;
            try {
                /* Get Json from server. */
                URL url = NetworkUtils.buildMovieDetailsURL(String.valueOf(mMovieId));
                jsonMoviesResponse = getJsonFromHttp(url);

                if (jsonMoviesResponse == null) {
                    Log.e(TAG, "Failed to get movie details.");
                    return null;
                }

                movie = TMDBJsonUtils.getMovieFromDetailsJson(jsonMoviesResponse);
                movie.setFavorite(false);

                movie.setTrailersJson(getTrailersJson());

                movie.setReviewsJson(getReviewsJson());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return movie;
    }

    @Override
    public void deliverResult(@Nullable Movie movie) {
        mMovie = movie;
        super.deliverResult(movie);
    }


    private Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();

        movie.setId(cursor.getLong(cursor.getColumnIndex(MovieEntry._ID)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)));
        movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE)));
        Date date = new Date(cursor.getLong(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE)));
        movie.setReleaseDate(date);
        movie.setDuration(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_DURATION)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)));
        movie.setSynopsis(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_SYNOPSIS)));
        movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_RATING)));
        movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT)));
        movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY)));
        movie.setTrailersJson(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TRAILERS_JSON)));
        movie.setPoster(DbUtils.getImage(cursor.getBlob(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER))));
        movie.setFavorite(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_IS_FAVORITE)) == 1 ? true : false);

        return movie;
    }

    private String getTrailersJson() {
        /* Get trailers. */
        String jsonResponse = null;

        URL url = NetworkUtils.buildMovieTrailersURL(String.valueOf(mMovieId));
        jsonResponse = getJsonFromHttp(url);

        return jsonResponse;
    }

    private String getReviewsJson() {

        String jsonResponse = null;
        /* Get reviews. */
        URL url = NetworkUtils.buildMovieReviewsURL(String.valueOf(mMovieId));
        jsonResponse = getJsonFromHttp(url);

        return jsonResponse;
    }

    private String getJsonFromHttp(URL url) {
        String jsonResponse = null;

        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }


}
