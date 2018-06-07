package com.example.forrest.popularmovies;

import android.graphics.Bitmap;
import android.util.ArrayMap;

import java.util.Date;

/**
 * This class represents a movie, which have an ID, a title, synopsis,
 * poster, etc.
 */
public class Movie {

    /* id */
    private long id;

    /* title */
    private String mTitle;

    /* original_title */
    private String mOriginalTitle;

    /* release_date */
    private Date mReleaseDate;

    /* runtime */
    private int mDuration = -1;

    /* poster_path */
    private String mPosterPath;

    /* overview */
    private String mSynopsis;

    /* vote_average */
    private double mRating = -1;

    /* vote_count */
    private int mVoteCount = -1;

    /* popularity */
    private double mPopularity = -1;

    private Bitmap mPoster;

    private boolean isFavorite = false;

    private ArrayMap<String, String> mTrailers;

    private String mTrailersJson;

    private String mReviewsJson;

    public Movie() {

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public ArrayMap<String, String> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(ArrayMap<String, String> mTrailers) {
        this.mTrailers = mTrailers;
    }

    public String getTrailersJson() {
        return mTrailersJson;
    }

    public void setTrailersJson(String mTrailerJson) {
        this.mTrailersJson = mTrailerJson;
    }

    public String getReviewsJson() {
        return mReviewsJson;
    }

    public void setReviewsJson(String mReviewsJson) {
        this.mReviewsJson = mReviewsJson;
    }

    public Bitmap getPoster() {
        return mPoster;
    }

    public void setPoster(Bitmap mPoster) {
        this.mPoster = mPoster;
    }
}