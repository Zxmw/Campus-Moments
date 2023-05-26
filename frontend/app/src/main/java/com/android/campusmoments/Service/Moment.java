package com.android.campusmoments.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class Moment {
    private int id;
    private int userId;
    private String avatarPath;
    private String mUsername;
    private String mTime;
    private String mTag;
    private String mTitle;
    private String mContent;  // KnifeText.toHtml
    private String imagePath;
    private String videoPath;
    private String mAddress;
    private int mLikeCount;
    private int mCommentCount;
    private int mStarCount;

    public boolean isLikedByMe;
    public boolean isStaredByMe;
    public boolean isFollowedByMe;
    public boolean isBlockedByMe;
    private List<Integer> commentIds; // 评论id - list

    public Moment(JSONObject obj) {
        try {
            // 返回值不为空时，赋值
            id = obj.getInt("id");
            userId = obj.getInt("user");
            mUsername = Services.checkObjStr(obj, "usr_username");
            avatarPath = Services.checkObjStr(obj, "usr_avatar");
            mTime = Services.checkObjStr(obj, "created_at");
            mTag = Services.checkObjStr(obj, "tag");
            mTitle = Services.checkObjStr(obj, "title");
            mContent = Services.checkObjStr(obj, "content");
            imagePath = Services.checkObjStr(obj, "image");
            videoPath = Services.checkObjStr(obj, "video");
            mAddress = Services.checkObjStr(obj, "address");
            mLikeCount = obj.getInt("total_likes");
            mCommentCount = obj.getInt("total_comments");
            mStarCount = obj.getInt("total_stars");

            JSONArray comments = obj.getJSONArray("comments");
            commentIds = Services.jsonArrayToList(comments);

            JSONArray liked_by = obj.getJSONArray("liked_by");
            isLikedByMe = isByMe(liked_by);
            JSONArray stared_by = obj.getJSONArray("stared_by");
            isStaredByMe = isByMe(stared_by);

            if(Services.mySelf.followList != null) {
                isFollowedByMe = Services.mySelf.followList.contains(userId);
            }
            if(Services.mySelf.blockList != null) {
                isBlockedByMe = Services.mySelf.blockList.contains(userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 判断是否被我点赞 / 收藏
    private boolean isByMe(JSONArray jsonArray) {
        List<Integer> list = Services.jsonArrayToList(jsonArray);
        return list.contains(Services.mySelf.id);
    }

    // get-function
    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public String getImagePath() {
        return imagePath;
    }
    public String getAvatarPath() {
        return avatarPath;
    }
    public String getVideoPath() {
        return videoPath;
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
    public String getAddress() {
        return mAddress;
    }
    public int getLikeCount() {
        return mLikeCount;
    }
    public void setLikeCount(int likeCount) {
        mLikeCount = likeCount;
    }
    public int getCommentCount() {
        return mCommentCount;
    }
    public int getStarCount() {
        return mStarCount;
    }
    public void setStarCount(int starCount) {
        mStarCount = starCount;
    }
    public List<Integer> getCommentIds() {
        return commentIds;
    }
}
