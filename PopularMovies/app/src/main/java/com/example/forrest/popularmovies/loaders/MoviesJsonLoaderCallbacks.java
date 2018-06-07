package com.example.forrest.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.forrest.popularmovies.MainActivity;
import com.example.forrest.popularmovies.Movie;
import com.example.forrest.popularmovies.Utils.Constants;
import com.example.forrest.popularmovies.Utils.TMDBJsonUtils;
import com.example.forrest.popularmovies.adapters.TmdbMoviesListAdapter;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

public class MoviesJsonLoaderCallbacks implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = MoviesJsonLoaderCallbacks.class.getSimpleName();

    private TmdbMoviesListAdapter mAdapter;
    private Context mContext;

    public MoviesJsonLoaderCallbacks(Context context, TmdbMoviesListAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        URL url = null;

        if (args != null) {
            url = (URL) args.getSerializable(Constants.TMDB_URL_EXTRA);
        }
        return new MoviesJsonLoader(mContext, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        //* If we got data, update to adapter. *//*
        if (data != null) {
            ArrayList<Movie> moviesList;
            try {
                moviesList = TMDBJsonUtils.getMoviesFromJsonList(data);
                mAdapter.setMoviesList(null);
                mAdapter.setMoviesList(moviesList);
            } catch (JSONException e) {
                Log.d(TAG, "Error parsing JSON");
                e.printStackTrace();
                mAdapter.setMoviesList(null);
            }
        } else {
            mAdapter.setMoviesList(null);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {


    }
}
