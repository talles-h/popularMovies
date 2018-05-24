package com.example.forrest.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forrest.popularmovies.Utils.Constants;
import com.example.forrest.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/** TODO Call TMDBJsonLoader and implement loader callbacks
 * **/

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private Movie mMovie;
    private TextView mTitleTv;
    private ImageView mPosterIv;
    private TextView mMovieDetailsTv;
    private TextView mSynopsisTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();

        /* Initialize using Intent parameters. */
        if (intent != null) {
            if (intent.hasExtra(Constants.EXTRA_MOVIE)) {
                mMovie = intent.getExtras().getParcelable(Constants.EXTRA_MOVIE);
            }
        }

        mTitleTv = (TextView) findViewById(R.id.tv_movie_title);
        mPosterIv = (ImageView) findViewById(R.id.iv_movie_poster);
        mMovieDetailsTv = (TextView) findViewById(R.id.tv_movie_details);
        mSynopsisTv = (TextView) findViewById(R.id.tv_movie_synopsis);

        /* Set the Movie data to the views */
        if (mMovie != null) {
            Log.d(TAG, "Title = " + mMovie.getTitle());
            mTitleTv.setText(mMovie.getTitle());
            String posterURL = NetworkUtils.buildPosterURL(mMovie.getPosterPath()).toString();
            Picasso.with(mPosterIv.getContext()).load(posterURL).into(mPosterIv);
            mMovieDetailsTv.setText(buildMovieDetails());
            mSynopsisTv.setText(mMovie.getSynopsis());
        }
    }


    /**
     * @return The String with all main datail about the movie.
     */
    private String buildMovieDetails() {
        // get a calendar using the default time zone and locale.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mMovie.getReleaseDate());

        String str =
                String.format("%s\n%s", calendar.get(Calendar.YEAR),mMovie.getRating());

        return str;
    }

}
