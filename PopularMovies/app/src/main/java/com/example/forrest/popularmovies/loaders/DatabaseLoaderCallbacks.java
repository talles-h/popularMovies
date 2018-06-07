package com.example.forrest.popularmovies.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.forrest.popularmovies.adapters.FavoriteMoviesListAdapter;
import com.example.forrest.popularmovies.adapters.TmdbMoviesListAdapter;
import com.example.forrest.popularmovies.data.MoviesContract;

public class DatabaseLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] FAVORITE_MOVIES_PROJECTION = {
            MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.COLUMN_POSTER,
            MoviesContract.MovieEntry.COLUMN_IS_FAVORITE
    };


    private FavoriteMoviesListAdapter mAdapter;
    private Context mContext;

    public DatabaseLoaderCallbacks(Context context, FavoriteMoviesListAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable final Bundle args) {

        return new CursorLoader(mContext, MoviesContract.MovieEntry.CONTENT_URI,
                FAVORITE_MOVIES_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
