package com.android.campusmoments.Service;

import android.net.Uri;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Moment {
    private int id;
    private int userId;
    private Uri mAvatar;
    private String mUsername;
    private String mTime; // 构造函数中获取当前时间
    private String mTag;
    private String mTitle;
    private String mContent;  // KnifeText.toHtml
    private Uri mPicture;
    public String imagePath;
    private Uri mVideo;
    private String videoPath;
    private String mAddress;
    private int mLikeCount;
    private int mCommentCount;
    private int mStarCount;

    private String[] mComments; // TODO: 评论

    public Moment(JSONObject obj) {
        try {
            // 返回值不为空时，赋值

            id = obj.getInt("id");
            userId = obj.getInt("user");
            mTime = Services.checkStr(obj, "created_at");
            mTag = Services.checkStr(obj, "tag");
            mTitle = Services.checkStr(obj, "title");
            mContent = Services.checkStr(obj, "content");
            imagePath = Services.checkStr(obj, "imagePath");
            videoPath = Services.checkStr(obj, "videoPath");
            mAddress = Services.checkStr(obj, "address");
            mLikeCount = obj.getInt("total_likes");
            mCommentCount = obj.getInt("total_comments");
            mStarCount = obj.getInt("total_stars");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 构造函数
    public Moment(Uri avatar, String username, String tag, String title, String content, Uri picture, Uri video, String address) {

        // 时间戳 TODO: Day-Month
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        mTime = formatter.format(curDate);

        mAvatar = avatar;
        mUsername = username;
        mTag = tag;
        mTitle = title;
        mContent = content;
        mPicture = picture;
        mVideo = video;
        mAddress = address;
    }
    // get-function
    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public Uri getAvatar() {
        return mAvatar;
    }
    public String getUsername() {
        return mUsername;
    }
    public String getTime() {
        return mTime;
    }
    public String getTag() {
        return mTag;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getContent() {
        return mContent;
    }
    public Uri getPicture() {
        return mPicture;
    }
    public Uri getVideo() {
        return mVideo;
    }
    public String getAddress() {
        return mAddress;
    }
    public int getLikeCount() {
        return mLikeCount;
    }
    public int getCommentCount() {
        return mCommentCount;
    }
    public int getStarCount() {
        return mStarCount;
    }
    // set-function
    public void setAvatar(Uri avatar) {
        mAvatar = avatar;
    }
    public void setUsername(String username) {
        mUsername = username;
    }
    public void setTime(String time) {
        mTime = time;
    }
    public void setTag(String tag) {
        mTag = tag;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public void setContent(String content) {
        mContent = content;
    }
    public void setPicture(Uri picture) {
        mPicture = picture;
    }
    public void setVideo(Uri video) {
        mVideo = video;
    }
    public void setAddress(String address) {
        mAddress = address;
    }
    public void setLikeCount(int likeCount) {
        mLikeCount = likeCount;
    }
    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }
    public void setStarCount(int starCount) {
        mStarCount = starCount;
    }
}
