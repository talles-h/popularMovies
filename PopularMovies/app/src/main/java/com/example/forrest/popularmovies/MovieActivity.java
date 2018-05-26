package com.example.forrest.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forrest.popularmovies.Utils.Constants;
import com.example.forrest.popularmovies.Utils.NetworkUtils;
import com.example.forrest.popularmovies.Utils.TMDBJsonLoader;
import com.example.forrest.popularmovies.Utils.TMDBJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.Calendar;


public class MovieActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private static final int TMDB_MOVIE_JSON_LOADER = 33;

    private String mMovieId;
    private TextView mTitleTv;
    private ImageView mPosterIv;
    private TextView mMovieDetailsTv;
    private TextView mSynopsisTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mTitleTv = findViewById(R.id.tv_movie_title);
        mPosterIv = findViewById(R.id.iv_movie_poster);
        mMovieDetailsTv = findViewById(R.id.tv_movie_details);
        mSynopsisTv = findViewById(R.id.tv_movie_synopsis);

        if (savedInstanceState != null) {
            this.mMovieId = savedInstanceState.getString(Constants.EXTRA_MOVIE_ID);
            // Same Movie ID
            getSupportLoaderManager().initLoader(TMDB_MOVIE_JSON_LOADER, null, this);
        } else {
            Intent intent = getIntent();
            /* Initialize using Intent parameters. */
            if (intent != null) {
                if (intent.hasExtra(Constants.EXTRA_MOVIE_ID)) {
                    mMovieId = intent.getStringExtra(Constants.EXTRA_MOVIE_ID);
                    // Can be a different movie ID
                    getSupportLoaderManager().restartLoader(TMDB_MOVIE_JSON_LOADER, null, this);
                }
            }
        }




    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString(Constants.EXTRA_MOVIE_ID, this.mMovieId);
    }

    /**
     * @return The String with all main datail about the movie.
     */
    private String buildMovieDetails(Movie movie) {
        // get a calendar using the default time zone and locale.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(movie.getReleaseDate());

        return String.format("%s\n%s", calendar.get(Calendar.YEAR),movie.getRating());
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {

        URL url = NetworkUtils.buildMovieDetailsURL(mMovieId);

        return new TMDBJsonLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        /* If we got data, update to adapter. */
        if (data != null) {

            try {
                Movie movie = TMDBJsonUtils.getMovieFromDetailsJson(data);
                /* Set the Movie data to the views */
                if (movie != null) {
                    Log.d(TAG, "Title = " + movie.getTitle());
                    mTitleTv.setText(movie.getTitle());
                    String posterURL = NetworkUtils.buildPosterURL(movie.getPosterPath()).toString();
                    Picasso.with(mPosterIv.getContext()).load(posterURL).into(mPosterIv);
                    mMovieDetailsTv.setText(buildMovieDetails(movie));
                    mSynopsisTv.setText(movie.getSynopsis());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        /*
         * We aren't using this method in our example application, but we are required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
    }
}
