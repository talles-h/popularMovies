package com.example.forrest.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    /* List of Movies */
    private ArrayList<Movie> mMovieList;

    /* To handle click in movies posters. */
    private MoviesAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }


    /**
     * Constructor. Received the click handler.
     * @param clickHandler
     */
    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
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
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        /* Pass the Movie object for this position to the holder object. */
        holder.bind(mMovieList.get(position));
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
        void bind(Movie movie) {
            Log.d(TAG, "Binding movie path: " + movie.getPosterPath());
            /* Load movie poster image */
            Picasso.with(mMovieImageView.getContext()).load(movie.getPosterPath()).into(mMovieImageView);
        }

        /* Poster was clicked. */
        @Override
        public void onClick(View v) {
            Log.d(TAG, "MovieViewHolder.onclick");
            int position = getAdapterPosition();
            mClickHandler.onClick(mMovieList.get(position));
        }
    }
}
