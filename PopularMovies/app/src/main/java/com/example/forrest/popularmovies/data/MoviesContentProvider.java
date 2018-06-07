package com.example.forrest.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.forrest.popularmovies.data.MoviesContract.MovieEntry;

public class MoviesContentProvider extends ContentProvider {

    private static final String TAG = "MoviesContentProvider";

    private MoviesDbHelper mMoviesDbHelper;

    // Constants to identify the URIs.
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {

        mMoviesDbHelper = new MoviesDbHelper(getContext());

        return true;
    }


    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /* Bind the int constants to the URIs */
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }



    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Get write access to the database.
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor resultCursor;

        Log.d(TAG, "Query URI: " + uri.toString());

        switch (match) {
            case MOVIES:
                resultCursor = db.query(MovieEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            case MOVIE_WITH_ID:

                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                String[] mSelectionArgs = new String[]{id};

                resultCursor = db.query(MovieEntry.TABLE_NAME,
                        projection,
                        MovieEntry._ID + "=?", mSelectionArgs,
                        null, null, sortOrder);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return resultCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        // Get write access to the database.
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri resultUri;

        Log.d(TAG, "Insert URI: " + uri);
        Log.d(TAG, "Insert Movie ID: " + values.getAsLong(MovieEntry._ID));

        switch (match) {
            case MOVIES:

                // Insert values to the database.
                long id = db.insert(MovieEntry.TABLE_NAME, null, values);
                Log.d(TAG, "Inserted id: " + id);
                if (id > 0) {
                    resultUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the Uri for the new inserted row.
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        switch (match) {
            case MOVIE_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);

                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(MovieEntry.TABLE_NAME, "_id=?",
                        new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
