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

public class ReviewListAdapter  extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder>  {


    ArrayMap<String, String> mReviews;



    public ReviewListAdapter(ArrayMap<String, String> reviews) {
        mReviews = reviews;
    }

    public void setReviewList(ArrayMap<String, String> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ReviewListAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        ReviewListAdapter.ReviewViewHolder viewHolder = new ReviewListAdapter.ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.size();
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewerNameTv;
        TextView reviewTv;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewerNameTv = itemView.findViewById(R.id.reviewer_name_tv);
            reviewTv = itemView.findViewById(R.id.review_tv);
        }

        void bind(int listIndex) {
            if (mReviews == null) return;
            reviewerNameTv.setText(mReviews.keyAt(listIndex));
            reviewTv.setText(mReviews.valueAt(listIndex));
        }


    }
}
