package com.example.forrest.popularmovies.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.forrest.popularmovies.Movie;
import com.example.forrest.popularmovies.R;
import com.example.forrest.popularmovies.Utils.DbUtils;
import com.example.forrest.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class TmdbMoviesListAdapter
        extends RecyclerView.Adapter<TmdbMoviesListAdapter.MovieViewHolder> {

    private static final String TAG = FavoriteMoviesListAdapter.class.getSimpleName();

    private final Context mContext;

    /* List of Movies */
    private ArrayList<Movie> mMovieList;

    /* To handle click in movies posters. */
    private MoviesListOnClickHandler mClickHandler;

    /**
     * Constructor. Received the click handler.
     * @param clickHandler
     */
    public TmdbMoviesListAdapter(@NonNull Context context, MoviesListOnClickHandler clickHandler) {
        mContext = context;
        mMovieList = null;
        mClickHandler = clickHandler;
    }


    /**
     * Set the list of Movies to be displayed.
     * @param movies
     */
    public void setMoviesList(ArrayList<Movie> movies) {
        mMovieList = movies;
        notifyDataSetChanged();
    }


    /**
     * @return The current number of movies in the list.
     */
    @Override
    public int getItemCount() {
        if (mMovieList == null) return 0;

        return mMovieList.size();
    }


    @Override
    public TmdbMoviesListAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TmdbMoviesListAdapter.MovieViewHolder viewHolder = new TmdbMoviesListAdapter.MovieViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(TmdbMoviesListAdapter.MovieViewHolder holder, int position) {

        String posterPath = mMovieList.get(position).getPosterPath();

        /* Pass the Movie poster path. */
        holder.bind(posterPath);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ImageView that will be used to show the movie's poster. */
        public ImageView mMovieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.img_view_item);
            itemView.setOnClickListener(this);
        }

        /* Bind the poster image to the ImageView. */
        void bind(String posterPath) {
            /* Load movie poster image */
            /* TODO In case the load fails, I should load an image indicating error. */
            String posterURL = NetworkUtils.buildPosterURL(posterPath).toString();
            Picasso.with(mMovieImageView.getContext()).load(posterURL).into(mMovieImageView);
        }

        /* Poster was clicked. */
        @Override
        public void onClick(View v) {
            Log.d(TAG, "MovieViewHolder.onclick");
            int position = getAdapterPosition();
            long movieId = mMovieList.get(position).getId();
            mClickHandler.onClick(movieId);
        }
    }
}
