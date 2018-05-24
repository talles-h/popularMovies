package com.example.forrest.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.forrest.popularmovies.Utils.Constants;
import com.example.forrest.popularmovies.Utils.NetworkUtils;
import com.example.forrest.popularmovies.Utils.TMDBJsonLoader;
import com.example.forrest.popularmovies.Utils.TMDBJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This is the activity that shows the list of Movies (posters)
 */
public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = "PopularMovies";

    /* Number of columns in the grid */
    private static final int NUM_COLUMNS = 2;

    /* Reference to RecyclerView */
    private RecyclerView mMoviesListRv;

    /* RecyclerView Adapter */
    private MoviesAdapter mAdapter;

    /* Determine the sort order of the movie list */
    private String mSortBy = Constants.ORDER_TOP_RATED;

    private static final String SORT_BY_EXTRA = "sort_by";

    private static final String TMDB_URL_EXTRA = "db_url";

    private static final int TMDB_JSON_LOADER = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesListRv = (RecyclerView) findViewById(R.id.rv_movies_grid);

        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        mMoviesListRv.setLayoutManager(layoutManager);

        /* The Adapter is responsible for displaying each item in the list. */
        mAdapter = new MoviesAdapter(this);

        mMoviesListRv.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            this.mSortBy = savedInstanceState.getString(SORT_BY_EXTRA);
        }

        /* Load the data */
        URL url;
        if (mSortBy.equals(Constants.ORDER_TOP_RATED))
            url = NetworkUtils.getTopRatedMoviesUrl();
        else
            url = NetworkUtils.getPopularMoviesUrl();

        Bundle bundle = new Bundle();
        bundle.putSerializable(TMDB_URL_EXTRA, url);
        getSupportLoaderManager().initLoader(TMDB_JSON_LOADER, bundle, this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SORT_BY_EXTRA, mSortBy);
    }

    /**
     * Handles click event in movie posters.
     * It will launch the activity with movie details.
     * @param movie
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieActivity.class;
        Intent intentMovieActivity = new Intent(context, destinationClass);
        intentMovieActivity.putExtra(Constants.EXTRA_MOVIE, movie);
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

        String sort = mSortBy;

        if (id == R.id.action_most_pop) {
            sort = Constants.ORDER_TOP_RATED;
        } else if (id == R.id.action_top_rated) {
            sort = Constants.ORDER_POPULAR;
        }

        /* Sort order changed? */
        if (!mSortBy.equals(sort)) {
            mSortBy = sort;
            updateMovieList();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Call the AsyncTask that will download the movies data and
     * update the RecyclerView adapter with the new data.
     */
    private void updateMovieList() {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> tmdbJsonLoader = loaderManager.getLoader(TMDB_JSON_LOADER);
        URL url;
        if (mSortBy.equals(Constants.ORDER_TOP_RATED))
            url = NetworkUtils.getTopRatedMoviesUrl();
        else
            url = NetworkUtils.getPopularMoviesUrl();

        Bundle bundle = new Bundle();
        bundle.putSerializable(TMDB_URL_EXTRA, url);

        if (tmdbJsonLoader == null) {
            loaderManager.initLoader(TMDB_JSON_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(TMDB_JSON_LOADER, bundle, this);
        }
    }



/*=============================== LoaderCallbacks ===============================*/
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {

        URL url = (URL) args.getSerializable(TMDB_URL_EXTRA);

        return new TMDBJsonLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        /* If we got data, update to adapter. */
        if (data != null) {
            ArrayList<Movie> moviesList = null;
            try {
                moviesList = TMDBJsonUtils.getMoviesFromJsonList(data);
                mAdapter.setMoviesList(null);
                Log.d(TAG, "Setting movie list with " + moviesList.size() + " movies");
                mAdapter.setMoviesList(moviesList);
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
/*============================= LoaderCallbacks End =============================*/

}
