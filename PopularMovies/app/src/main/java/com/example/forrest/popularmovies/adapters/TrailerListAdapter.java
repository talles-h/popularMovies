package com.example.forrest.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.forrest.popularmovies.R;

public class TrailerListAdapter  extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder>  {

    ArrayMap<String, String> mTrailers;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(String key);
    }


    public TrailerListAdapter(ArrayMap<String, String> trailers, ListItemClickListener listener) {
        mTrailers = trailers;
        mOnClickListener = listener;
    }

    public void setTrailerList(ArrayMap<String, String> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) return 0;
        return mTrailers.size();
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trailerTitleTv;
        String trailerYoutubeKey;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerTitleTv = itemView.findViewById(R.id.trailer_name_tv);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            if (mTrailers == null) return;
            trailerTitleTv.setText(mTrailers.keyAt(listIndex));
            trailerYoutubeKey = mTrailers.valueAt(listIndex);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(trailerYoutubeKey);
        }
    }
}
