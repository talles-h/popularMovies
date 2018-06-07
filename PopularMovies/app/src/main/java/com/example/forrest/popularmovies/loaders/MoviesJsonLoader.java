package com.example.forrest.popularmovies.loaders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.forrest.popularmovies.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MoviesJsonLoader extends AsyncTaskLoader<String> {

    public static final String TAG = MoviesJsonLoader.class.getSimpleName();

    String mMovieDbJsonRes;
    URL mURL;


    public MoviesJsonLoader(Context context, URL url) {
        super(context);
        Log.d(TAG, "MoviesJsonLoader constructor");
        mURL = url;
        mMovieDbJsonRes = null;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");
        if (mURL == null) {
            return;
        }

        if (mMovieDbJsonRes != null) {
            deliverResult(mMovieDbJsonRes);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        Log.d(TAG, "loadInBackground");
        String jsonMoviesResponse = null;
        try {
            /* Get Json from server. */
            jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(mURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonMoviesResponse;
    }

    @Override
    public void deliverResult(@Nullable String dbJson) {
        Log.d(TAG, "deliverResult");
        mMovieDbJsonRes = dbJson;
        super.deliverResult(dbJson);
    }
}
