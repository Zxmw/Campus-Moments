package com.android.campusmoments;

import android.net.Uri;

import java.util.List;

public class Moment {
    private int mAvatar;
    private String mUsername;
    private String mTime;
    private String mTitle;
    private List<Uri> mPictures;
    private String mContent;
    private int mCommentCount;
    private int mLikeCount;
    private int mFavoriteCount;

    public Moment(int avatar, String username, String time, String title, List<Uri> pictures, String content, int commentCount, int likeCount, int favoriteCount) {
        mAvatar = avatar;
        mUsername = username;
        mTime = time;
        mTitle = title;
        mPictures = pictures;
        mContent = content;
        mCommentCount = commentCount;
        mLikeCount = likeCount;
        mFavoriteCount = favoriteCount;
    }

    public int getAvatar() {
        return mAvatar;
    }

    public void setAvatar(int avatar) {
        mAvatar = avatar;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public List<Uri> getPictures() {
        if (mPictures == null) {
            return List.of();
        }
        return mPictures;
    }

    public void setPictures(List<Uri> pictures) {
        mPictures = pictures;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getCommentCount() {
        return mCommentCount;
    }


    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public int getLikeCount() {
        return mLikeCount;
    }

    public void setLikeCount(int likeCount) {
        mLikeCount = likeCount;
    }

    public int getFavoriteCount() {
        return mFavoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        mFavoriteCount = favoriteCount;
    }
}

