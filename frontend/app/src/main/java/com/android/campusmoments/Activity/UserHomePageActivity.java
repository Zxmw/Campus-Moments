package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.campusmoments.Adapter.MomentAdapter;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Moment;
import com.android.campusmoments.Service.Services;
import com.android.campusmoments.Service.User;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserHomePageActivity extends AppCompatActivity {
    public static final int FOLLOW_FAIL = 0;
    public static final int FOLLOW_SUCCESS = 1;
    public static final int UNFOLLOW_FAIL = 2;
    public static final int UNFOLLOW_SUCCESS = 3;
    public static final int BLOCK_FAIL = 4;
    public static final int BLOCK_SUCCESS = 5;
    public static final int UNBLOCK_FAIL = 6;
    public static final int UNBLOCK_SUCCESS = 7;
    public static final int GET_USER_SUCCESS = 8;
    public static final int GET_USER_FAIL = 9;
    public static final int GET_USER_MOMENTS_SUCCESS = 10;
    public static final int GET_USER_MOMENTS_FAIL = 11;
    private static int id;
    private User user;
    private boolean isFollowed = false;
    private boolean isBlocked = false;
    private ImageView avatar;
    private TextView userName;
    private TextView userId;
    private TextView userBio;
    private Button followButton;
    private Button privateMessageButton;
    private Button blockButton;
    private RecyclerView userMomentsRecyclerView;
    private MomentAdapter momentAdapter;
    private List<Moment> mMomentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        mMomentList = new ArrayList<>();
        Services.setUserHomePageHandler(handler);
        //get user id from intent
        id = getIntent().getIntExtra("id", 0);
        // get user info from server
        Services.getUser(id);
        Services.getMomentsByUser(id);
    }

    private void initUserInfo() {
        avatar = findViewById(R.id.user_avatar);
        userName = findViewById(R.id.username_text);
        userId = findViewById(R.id.user_id_text);
        userBio = findViewById(R.id.user_bio_text);
        userName.setText(user.username);
        userId.setText(String.valueOf(user.id));
        userBio.setText(user.bio);
        followButton = findViewById(R.id.follow_button);
        privateMessageButton = findViewById(R.id.private_message_button);
        blockButton = findViewById(R.id.block_button);
        if (user.avatar == null){
            avatar.setImageResource(R.drawable.avatar_default);
        }
        else {
            Picasso.get().load(Uri.parse(user.avatar)).into(avatar);
        }
        Log.d("follow", String.valueOf(Services.mySelf.followList));
        if (Services.mySelf.followList.contains(user.id)) {
            isFollowed = true;
            followButton.setText("取消关注");
            blockButton.setVisibility(View.GONE);
        }
        if (Services.mySelf.blockList.contains(user.id)) {
            isBlocked = true;
            blockButton.setText("取消屏蔽");
            followButton.setVisibility(View.GONE);
        }
    }
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FOLLOW_FAIL:
                    followFail();
                    break;
                case FOLLOW_SUCCESS:
                    try {
                        followSuccess(msg.obj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UNFOLLOW_FAIL:
                    unfollowFail();
                    break;
                case UNFOLLOW_SUCCESS:
                    try {
                        unfollowSuccess(msg.obj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case BLOCK_FAIL:
                    blockFail();
                    break;
                case BLOCK_SUCCESS:
                    try {
                        blockSuccess(msg.obj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UNBLOCK_FAIL:
                    unblockFail();
                    break;
                case UNBLOCK_SUCCESS:
                    try {
                        unblockSuccess(msg.obj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case GET_USER_SUCCESS:
                    getUserSuccess(msg.obj);
                    break;
                case GET_USER_FAIL:
                    getUserFail();
                    break;
                case GET_USER_MOMENTS_SUCCESS:
                    try {
                        getUserMomentsSuccess(msg.obj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case GET_USER_MOMENTS_FAIL:
                    getUserMomentsFail();
                    break;
            }
        }
    };

    private void getUserMomentsFail() {
        Toast.makeText(this, "获取用户动态失败", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getUserMomentsSuccess(Object obj) throws JSONException {
        initMomentsRecyclerView();
        JSONArray arr = new JSONArray(obj.toString());
        mMomentList.clear();
        for (int i = 0; i < arr.length(); i++) {
            Log.d("moment", arr.getJSONObject(i).toString());
            mMomentList.add(new Moment(arr.getJSONObject(i)));
        }

        momentAdapter.setMoments(mMomentList);
        momentAdapter.notifyDataSetChanged();

    }

    private void initMomentsRecyclerView() {
        userMomentsRecyclerView = findViewById(R.id.user_moments_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userMomentsRecyclerView.setLayoutManager(layoutManager);
        momentAdapter = new MomentAdapter(mMomentList, new MomentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(getContext(), "clicked: "+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserHomePageActivity.this, DetailedActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("avatarUri", mMomentList.get(position).getAvatar());
                intent.putExtra("username", mMomentList.get(position).getUsername());
                intent.putExtra("time", mMomentList.get(position).getTime());
                intent.putExtra("tag", mMomentList.get(position).getTag());
                intent.putExtra("title", mMomentList.get(position).getTitle());
                intent.putExtra("content", mMomentList.get(position).getContent());
                intent.putExtra("pictureUri", mMomentList.get(position).getPicture());
                intent.putExtra("videoUri", mMomentList.get(position).getVideo());
                intent.putExtra("address", mMomentList.get(position).getAddress());
                intent.putExtra("likeCount", mMomentList.get(position).getLikeCount());
                intent.putExtra("commentCount", mMomentList.get(position).getCommentCount());
                intent.putExtra("starCount", mMomentList.get(position).getStarCount());
                startActivity(intent);
                // TODO: adapter.notifyDataSetChanged(); 评论后刷新
            }
        });
        userMomentsRecyclerView.setAdapter(momentAdapter);
    }

    private void getUserFail() {
        Toast.makeText(this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getUserSuccess(Object obj) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(obj.toString());
            user = new User(jsonObject);
            initUserInfo();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void follow(View view) {
        Services.follow(user.id, isFollowed);
    }
    private void followSuccess(Object obj) throws JSONException {
        isFollowed = true;
        followButton.setText("取消关注");
        blockButton.setVisibility(View.GONE);
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.mySelf.followList = Services.jsonArrayToList(jsonObject.getJSONArray("follow_list"));
        Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
    }

    private void followFail() {
        Toast.makeText(this, "关注失败", Toast.LENGTH_SHORT).show();
    }

    public void unfollowSuccess(Object obj) throws JSONException {
        isFollowed = false;
        followButton.setText("关注");
        blockButton.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.mySelf.followList = Services.jsonArrayToList(jsonObject.getJSONArray("follow_list"));
        Toast.makeText(this, "取消关注成功", Toast.LENGTH_SHORT).show();
    }

    private void unfollowFail() {
        Toast.makeText(this, "取消关注失败", Toast.LENGTH_SHORT).show();
    }

    public void block(View view) {
        Services.block(user.id, isBlocked);
    }

    private void blockSuccess(Object obj) throws JSONException {
        isBlocked = true;
        blockButton.setText("取消屏蔽");
        followButton.setVisibility(View.GONE);
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.mySelf.blockList = Services.jsonArrayToList(jsonObject.getJSONArray("block_list"));
        Toast.makeText(this, "屏蔽成功", Toast.LENGTH_SHORT).show();
    }

    private void blockFail() {
        Toast.makeText(this, "屏蔽失败", Toast.LENGTH_SHORT).show();
    }

    private void unblockSuccess(Object obj) throws JSONException {
        isBlocked = false;
        blockButton.setText("屏蔽");
        followButton.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.mySelf.blockList = Services.jsonArrayToList(jsonObject.getJSONArray("block_list"));
        Toast.makeText(this, "取消屏蔽成功", Toast.LENGTH_SHORT).show();
    }

    private void unblockFail() {
        Toast.makeText(this, "取消屏蔽失败", Toast.LENGTH_SHORT).show();
    }
    public void privateMessage(View view) {
        Intent intent = new Intent(this, PrivateMessageActivity.class);
        intent.putExtra("id", user.id);
        intent.putExtra("username", user.username);
        startActivity(intent);
    }

    public void cancel(View view) {
        finish();
    }
}