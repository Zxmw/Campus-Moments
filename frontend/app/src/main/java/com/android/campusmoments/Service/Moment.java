package com.android.campusmoments.Service;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Moment {
    private static final String TAG = "Moment";
    private int id;
    public int getId() {
        return id;
    }
    private int userId;
    private Uri mAvatar;
    private String avatarPath;
    public String getAvatarPath() {
        return avatarPath;
    }
    private String mUsername;
    private String mTime; // 构造函数中获取当前时间
    private String mTag;
    private String mTitle;
    private String mContent;  // KnifeText.toHtml
    private Uri mPicture;
    private String imagePath;
    public String getImagePath() {
        return imagePath;
    }
    private Uri mVideo;
    private String videoPath;
    public String getVideoPath() {
        return videoPath;
    }
    private String mAddress;
    private int mLikeCount;
    private int mCommentCount;
    private int mStarCount;

    private String[] mComments; // TODO: 评论

    private Handler getUserHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what == 1) {
                try {
                    JSONObject obj = new JSONObject(msg.obj.toString());
                    mUsername = Services.checkStr(obj, "username");
                    avatarPath = Services.checkStr(obj, "avatar");
                    Log.d(TAG, "handleMessage: " + mUsername);
                    Log.d(TAG, "handleMessage: " + avatarPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public Moment(JSONObject obj) {
        try {
            // 返回值不为空时，赋值
            id = obj.getInt("id");
            userId = obj.getInt("user");
            mTime = Services.checkStr(obj, "created_at");
            mTag = Services.checkStr(obj, "tag");
            mTitle = Services.checkStr(obj, "title");
            mContent = Services.checkStr(obj, "content");
            imagePath = Services.checkStr(obj, "image");
            videoPath = Services.checkStr(obj, "video");
            mAddress = Services.checkStr(obj, "address");
            mLikeCount = obj.getInt("total_likes");
            mCommentCount = obj.getInt("total_comments");
            mStarCount = obj.getInt("total_stars");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Services.getUser(userId, getUserHandler); // 放在try里面有可能运行不了
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
