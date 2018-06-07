package com.example.forrest.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.forrest.popularmovies.R;
import com.example.forrest.popularmovies.Utils.DbUtils;
import com.example.forrest.popularmovies.Utils.NetworkUtils;
import com.example.forrest.popularmovies.data.MoviesContract.MovieEntry;
import com.squareup.picasso.Picasso;

public class FavoriteMoviesListAdapter
        extends RecyclerView.Adapter<FavoriteMoviesListAdapter.MovieViewHolder> {

    private static final String TAG = FavoriteMoviesListAdapter.class.getSimpleName();

    private final Context mContext;

    /* List of Movies */
    private Cursor mCursor;

    /* To handle click in movies posters. */
    private MoviesListOnClickHandler mClickHandler;


    /**
     * Constructor. Received the click handler.
     * @param clickHandler
     */
    public FavoriteMoviesListAdapter(@NonNull Context context, MoviesListOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }


    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * @return The current number of movies in the list.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;

        return mCursor.getCount();
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {


        /* Pass the Movie poster path. */
        holder.bind(position);

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
        void bind(int position) {
            mCursor.moveToPosition(position);

            byte[] posterByte = mCursor.getBlob(mCursor.getColumnIndex(MovieEntry.COLUMN_POSTER));

            if (posterByte != null) {
                Bitmap poster = DbUtils.getImage(posterByte);
                mMovieImageView.setImageBitmap(poster);
            } else {
                String posterPath = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));

                /* Load movie poster image */
                String posterURL = NetworkUtils.buildPosterURL(posterPath).toString();
                Picasso.with(mMovieImageView.getContext()).load(posterURL).into(mMovieImageView);
            }
        }

        /* Poster was clicked. */
        @Override
        public void onClick(View v) {
            Log.d(TAG, "MovieViewHolder.onclick");
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            long movieId = mCursor.getLong(mCursor.getColumnIndex(MovieEntry._ID));
            mClickHandler.onClick(movieId);
        }


    }
}
