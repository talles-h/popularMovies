package com.example.forrest.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.forrest.popularmovies.Utils.Constants;
import com.example.forrest.popularmovies.Utils.NetworkUtils;
import com.example.forrest.popularmovies.adapters.FavoriteMoviesListAdapter;
import com.example.forrest.popularmovies.adapters.MoviesListOnClickHandler;
import com.example.forrest.popularmovies.adapters.TmdbMoviesListAdapter;
import com.example.forrest.popularmovies.loaders.DatabaseLoaderCallbacks;
import com.example.forrest.popularmovies.loaders.MoviesJsonLoaderCallbacks;

import java.net.URL;

import static com.example.forrest.popularmovies.Utils.Constants.TMDB_URL_EXTRA;

/**
 * This is the activity that shows the list of Movies (posters)
 */
public class MainActivity extends AppCompatActivity
        implements MoviesListOnClickHandler {

    private static final String SORT_BY_EXTRA = "sort_by";
    private static final int TMDB_JSON_LOADER = 25;
    private static final int DATABASE_LOADER = 26;

    /* Number of columns in the grid */
    private static final int NUM_COLUMNS = 2;

    /* Determine the sort order of the movie list */
    private int mShowOption = Constants.SHOW_TOP_RATED;

    /* RecyclerView adapter to handle movies list.
    * Can be TmdbMoviesListAdapter or FavoriteMoviesListAdapter.
    * */
    private FavoriteMoviesListAdapter mFavoritesAdapter;
    private TmdbMoviesListAdapter mTmdbListAdapter;

    /**
     * Callbacks for the loaders.
     * Can be from class DatabaseLoaderCallbacks or
     * MoviesJsonLoaderCallbacks.
     */
    private DatabaseLoaderCallbacks mDatabaseLoaderCallbacks;
    private MoviesJsonLoaderCallbacks mMoviesJsonLoaderCallbacks;


    /* The RecyclerView to list the movies. */
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Get the last option selected by user.
        * TODO Use shared preferences to save this.
        * */
        if (savedInstanceState != null) {
            this.mShowOption = savedInstanceState.getInt(SORT_BY_EXTRA);
        }

        mRecyclerView = findViewById(R.id.rv_movies_grid);

        /* Use the GridLayout to display the movies in columns. */
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        mRecyclerView.setLayoutManager(layoutManager);

        /* Initialize the adapters. */
        mFavoritesAdapter = new FavoriteMoviesListAdapter(this,this);
        mTmdbListAdapter = new TmdbMoviesListAdapter(this, this);

        /* Set the current adapter based on the selected
        option (top rated, most popular or favorites). */
        setAdapter();

        /* Create the loaders and start the right one based on
        * the selected option (top rated, most popular or favorites). */
        initializeLoader();
    }

    /** The Adapter is responsible for displaying each item in the list.
     * This method will set the right adapter for the user selected option.
     */
    private void setAdapter() {
        switch (mShowOption) {
            case Constants.SHOW_FAVORITES:
                mRecyclerView.setAdapter(mFavoritesAdapter);
                break;
            case Constants.SHOW_POPULAR:
            case Constants.SHOW_TOP_RATED:
                mRecyclerView.setAdapter(mTmdbListAdapter);
                break;
        }

    }

    /**
     * NOTES:
     * initLoader:
     *    - If the loader does not exist, create a new one by calling onCreateLoader().
     *    - If the loader already exist, it is reused. If it have already generated data,
     *    the system calls onLoadFinished() immediately (the loader does not lose the data).
     *
     */
    private void initializeLoader() {
        Bundle bundle = new Bundle();
        mDatabaseLoaderCallbacks = new DatabaseLoaderCallbacks(this, mFavoritesAdapter);
        mMoviesJsonLoaderCallbacks = new MoviesJsonLoaderCallbacks(this, mTmdbListAdapter);

        switch (mShowOption) {
            case Constants.SHOW_FAVORITES:
                getSupportLoaderManager().initLoader(DATABASE_LOADER, bundle, mDatabaseLoaderCallbacks);
                break;

            case Constants.SHOW_POPULAR:
            case Constants.SHOW_TOP_RATED:
                /* Set the right URL. */
                URL url;
                if (mShowOption == Constants.SHOW_TOP_RATED)
                    url = NetworkUtils.getTopRatedMoviesUrl();
                else
                    url = NetworkUtils.getPopularMoviesUrl();
                bundle.putSerializable(Constants.TMDB_URL_EXTRA, url);
                getSupportLoaderManager().initLoader(TMDB_JSON_LOADER, bundle, mMoviesJsonLoaderCallbacks);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SORT_BY_EXTRA, mShowOption);
    }


    /**
     * Handles click event in movie posters.
     * It will launch the activity with movie details.
     * @param movieId the movie clicked
     */
    @Override
    public void onClick(long movieId) {
        Context context = this;
        Class destinationClass = MovieActivity.class;
        Intent intentMovieActivity = new Intent(context, destinationClass);
        intentMovieActivity.putExtra(Constants.EXTRA_MOVIE_ID, movieId);
        startActivity(intentMovieActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main_activity_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        int sort = mShowOption;

        if (id == R.id.action_most_pop) {
            sort = Constants.SHOW_POPULAR;
        } else if (id == R.id.action_top_rated) {
            sort = Constants.SHOW_TOP_RATED;
        } else if (id == R.id.action_favorites) {
            sort = Constants.SHOW_FAVORITES;
        }

        /* Sort order changed? */
        if (mShowOption != sort) {
            mShowOption = sort;
            updateMovieList();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Restart the loader to update the movies list according to the user selection.
     * Notes:
     * About restartLoader():
     *       - If the loader with the given ID exists, it will restart the loader and replace
     *       its data.
     *       - If the loader does not exist, it starts a new loader.
     */
    private void updateMovieList() {

        LoaderManager loaderManager = getSupportLoaderManager();

        // Set the correct adapter.
        setAdapter();

        switch (mShowOption) {
            case Constants.SHOW_FAVORITES:
                /* Get movies from local Database */
                loaderManager.restartLoader(DATABASE_LOADER, new Bundle(), mDatabaseLoaderCallbacks);
                break;
            default: // SHOW_POPULAR or SHOW_TOP_RATED
                /* Get movies from server */
                URL url;
                if (mShowOption == Constants.SHOW_TOP_RATED)
                    url = NetworkUtils.getTopRatedMoviesUrl();
                else
                    url = NetworkUtils.getPopularMoviesUrl();

                Bundle bundle = new Bundle();
                bundle.putSerializable(TMDB_URL_EXTRA, url);

                loaderManager.restartLoader(TMDB_JSON_LOADER, bundle, mMoviesJsonLoaderCallbacks);
                break;
        }
    }
}
