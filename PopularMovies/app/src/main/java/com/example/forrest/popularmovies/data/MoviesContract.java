package com.example.forrest.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    // Content provider authority. Must be same as declared in manifest.
    public static final String AUTHORITY = "com.example.forrest.popularmovies";

    // Content provider base URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Path to saved movies table
    public static final String PATH_MOVIES = "saved_movies";


    // Movie entry
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "saved_movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_DURATION = "runtime";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_SYNOPSIS = "overview";
        public static final String COLUMN_RATING = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_TRAILERS_JSON = "videos";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_IS_FAVORITE = "isFavorite";
    }




}
