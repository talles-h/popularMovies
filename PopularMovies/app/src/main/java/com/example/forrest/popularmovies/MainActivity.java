package com.example.forrest.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    /* Number of items in the recycler view grid */
    private static final int NUM_LIST_ITEMS = 100;

    /* Number of columns in the grid */
    private static final int NUM_COLUMNS = 2;

    /* Reference to RecyclerView */
    private RecyclerView mMoviesList;

    /* RecyclerView Adapter */
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies_grid);

        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_COLUMNS);

        mMoviesList.setLayoutManager(layoutManager);

        mMoviesList.setHasFixedSize(true);

        /*
        The Adapter is responsible for displaying each item in the list.
         */
        mAdapter = new MoviesAdapter(NUM_LIST_ITEMS);

        mMoviesList.setAdapter(mAdapter);

    }


    /**
     * AsyncTask to fetch the movies data, including the poster.
     */


}
