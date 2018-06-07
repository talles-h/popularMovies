package com.example.forrest.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forrest.popularmovies.Utils.Constants;
import com.example.forrest.popularmovies.Utils.DbUtils;
import com.example.forrest.popularmovies.Utils.NetworkUtils;
import com.example.forrest.popularmovies.Utils.TMDBJsonUtils;
import com.example.forrest.popularmovies.adapters.ReviewListAdapter;
import com.example.forrest.popularmovies.adapters.TrailerListAdapter;
import com.example.forrest.popularmovies.data.MoviesContract;
import com.example.forrest.popularmovies.loaders.MovieDetailsLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.util.Calendar;


public class MovieActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Movie>,
        View.OnClickListener, TrailerListAdapter.ListItemClickListener {

    private static final String TAG = MovieActivity.class.getSimpleName();
    private static final int MOVIE_DETAILS_LOADER = 33;

    private long mMovieId;
    private Movie mMovie;
    private TextView mTitleTv;
    private ImageView mPosterIv;
    private TextView mMovieYearTv;
    private TextView mMovieDurationTv;
    private TextView mMovieRatingTv;
    private TextView mSynopsisTv;
    private ImageButton mFavoriteButton;

    private TrailerListAdapter mTrailersAdapter;
    private RecyclerView mTrailersRecyclerView;

    private ReviewListAdapter mReviewsAdapter;
    private RecyclerView mReviewsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mTitleTv = findViewById(R.id.tv_movie_title);
        mPosterIv = findViewById(R.id.iv_movie_poster);
        mMovieYearTv = findViewById(R.id.tv_movie_year);
        mMovieDurationTv = findViewById(R.id.tv_movie_duration);
        mMovieRatingTv = findViewById(R.id.tv_movie_rating);
        mSynopsisTv = findViewById(R.id.tv_movie_synopsis);

        mFavoriteButton = (ImageButton) findViewById(R.id.favorite_button);
        mFavoriteButton.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);


        mTrailersAdapter = new TrailerListAdapter(null, this);
        mTrailersRecyclerView = findViewById(R.id.rv_trailers_list);

        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        layoutManager = new LinearLayoutManager(this);


        mReviewsAdapter = new ReviewListAdapter(null);
        mReviewsRecyclerView = findViewById(R.id.rv_reviews_list);

        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        if (savedInstanceState != null) {
            this.mMovieId = savedInstanceState.getLong(Constants.EXTRA_MOVIE_ID);
            // Same Movie ID
            getSupportLoaderManager().initLoader(MOVIE_DETAILS_LOADER, null, this);
        } else {
            Intent intent = getIntent();
            /* Initialize using Intent parameters. */
            if (intent != null) {
                if (intent.hasExtra(Constants.EXTRA_MOVIE_ID)) {
                    mMovieId = intent.getLongExtra(Constants.EXTRA_MOVIE_ID, -1);
                    // Can be a different movie ID
                    getSupportLoaderManager().restartLoader(MOVIE_DETAILS_LOADER, null, this);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putLong(Constants.EXTRA_MOVIE_ID, this.mMovieId);
    }

    /**
     * @return The String with all main datail about the movie.
     */
    private void updateMovieDetailsUi() {

        if (mMovie.getPoster() == null) {
            String posterURL = NetworkUtils.buildPosterURL(mMovie.getPosterPath()).toString();
            Picasso.with(mPosterIv.getContext()).load(posterURL).into(getTarget());
        } else {
            mPosterIv.setImageBitmap(mMovie.getPoster());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mMovie.getReleaseDate());

        mTitleTv.setText(mMovie.getTitle());
        mMovieYearTv.setText(Integer.toString(calendar.get(Calendar.YEAR)));
        mMovieDurationTv.setText(Long.toString(mMovie.getDuration()) + " min");
        mMovieRatingTv.setText(Double.toString(mMovie.getRating()));
        mSynopsisTv.setText(mMovie.getSynopsis());
        mFavoriteButton.setSelected(mMovie.isFavorite());

        try {
            mTrailersAdapter.setTrailerList(TMDBJsonUtils.getMovieTrailers(mMovie.getTrailersJson()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            mReviewsAdapter.setReviewList(TMDBJsonUtils.getMovieReviews(mMovie.getReviewsJson()));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @NonNull
    @Override
    public Loader<Movie> onCreateLoader(int id, @Nullable Bundle args) {

        return new MovieDetailsLoader(this, mMovieId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Movie> loader, Movie data) {

        if (data != null) {
            mMovie = data;
            updateMovieDetailsUi();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie> loader) {

    }

    @Override
    public void onClick(View v) {
        v.setSelected(!v.isSelected());
        if (v.isSelected()) {
            Log.d(TAG, "Button enabled.");

            ContentValues contentValues = createContentValues(1);
            getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, contentValues);
            mMovie.setFavorite(true);

        } else {
            Log.d(TAG, "Button disabled.");
            // Build appropriate uri with String row id appended
            String stringId = Long.toString(mMovieId);
            Uri uri = MoviesContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();

            Log.d(TAG, "Deleting movie with ID = " + stringId);

            // COMPLETED (2) Delete a single row of data using a ContentResolver
            int moviesDeleted = getContentResolver().delete(uri, null, null);


            Log.e(TAG, "Movies deleted: " + moviesDeleted);


            mMovie.setFavorite(false);
        }
    }

    private ContentValues createContentValues(int isFavorite) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MoviesContract.MovieEntry._ID, mMovie.getId());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_IS_FAVORITE, isFavorite);
        contentValues.put(MoviesContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, mMovie.getOriginalTitle());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate().getTime());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_DURATION, mMovie.getDuration());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_SYNOPSIS, mMovie.getSynopsis());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_RATING, mMovie.getRating());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT, mMovie.getVoteCount());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_TRAILERS_JSON, mMovie.getTrailersJson());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_POPULARITY, mMovie.getPopularity());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_POSTER, DbUtils.getBytes(mMovie.getPoster()));

        return contentValues;
    }

    @Override
    public void onListItemClick(String key) {
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=" + key)));
        Log.i("Video", "Video Playing....");
    }

    // Target to save the poster.
    private Target getTarget(){
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                mMovie.setPoster(bitmap);
                mPosterIv.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        return target;
    }
}
