package com.example.forrest.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.forrest.popularmovies.Utils.Constants;
import com.example.forrest.popularmovies.Utils.NetworkUtils;
import com.example.forrest.popularmovies.Utils.TMDBJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This is the activity that shows the list of Movies (posters)
 */
public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String TAG = "PopularMovies";

    /* Number of columns in the grid */
    private static final int NUM_COLUMNS = 2;

    /* Reference to RecyclerView */
    private RecyclerView mMoviesListRv;

    /* RecyclerView Adapter */
    private MoviesAdapter mAdapter;

    /* Determine the sort order of the movie list */
    private String mSortBy = Constants.ORDER_TOP_RATED;

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

        updateMovieList();
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
        /* Execute the AsyncTask that will fetch the movies list. */
        new FetchMoviesTask().execute(mSortBy);
    }



    /**
     * AsyncTask to fetch the movies data.
     */
    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... order) {

            URL url;
            if(order[0].equals(Constants.ORDER_POPULAR)) {
                /* Get the URL for Popular Movies list. */
                url = NetworkUtils.getPopularMoviesUrl();
            } else {
                /* Get the URl for Top Rated Movies list. */
                url = NetworkUtils.getTopRatedMoviesUrl();
            }

            ArrayList<Movie> moviesList = null;

            try {
                /* Get Json from server. */
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(url);

                /* Parse the Json int a list of Movie objects. */
                moviesList = TMDBJsonUtils.getMoviesFromJsonList(jsonMoviesResponse);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return moviesList;
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            /* If we got data, update to adapter. */
            if (movies != null) {
                mAdapter.setMoviesList(null);
                Log.d(TAG, "Setting movie list with " + movies.size() + " movies");
                mAdapter.setMoviesList(movies);
            }
        }
    }

}
