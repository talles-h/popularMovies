package com.example.forrest.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * This class represents a movie, which have an ID, a title, synopsis,
 * poster, etc.
 */
public class Movie implements Parcelable {

    /* id */
    private String id;

    /* title */
    private String mTitle;

    /* original_title */
    private String mOriginalTitle;

    /* release_date */
    private Date mReleaseDate;

    /* runtime */
    private int mDuration = -1;

    /* poster_path or backdrop_path */
    private String mPosterPath;

    /* overview */
    private String mSynopsis;

    /* vote_average */
    private double mRating = -1;

    /* vote_count */
    private int mVoteCount = -1;

    /* popularity */
    private double mPopularity = -1;


    public Movie() {

    }

    public Movie(Parcel in) {
        readFromParcel(in);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String mSynopsis) {
        this.mSynopsis = mSynopsis;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.mTitle);
        dest.writeString(this.mPosterPath);
        dest.writeString(this.mSynopsis);
        dest.writeDouble(this.mRating);
        if (this.mReleaseDate != null)
            dest.writeLong(this.mReleaseDate.getTime());
        else
            dest.writeLong(-1);

        dest.writeInt(mDuration);

    }

    private void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.mTitle = in.readString();
        this.mPosterPath = in.readString();
        this.mSynopsis = in.readString();
        this.mRating = in.readDouble();
        long date = in.readLong();
        if (date != -1)
            this.mReleaseDate = new Date(date);
        this.mDuration = in.readInt();

    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double mPopularity) {
        this.mPopularity = mPopularity;
    }
}