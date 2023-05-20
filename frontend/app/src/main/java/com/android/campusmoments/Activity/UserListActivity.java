package com.android.campusmoments.Activity;

import static com.android.campusmoments.Service.Config.BLOCK_FAIL;
import static com.android.campusmoments.Service.Config.BLOCK_SUCCESS;
import static com.android.campusmoments.Service.Config.FOLLOW_FAIL;
import static com.android.campusmoments.Service.Config.FOLLOW_SUCCESS;
import static com.android.campusmoments.Service.Config.GET_ALL_USERS_FAIL;
import static com.android.campusmoments.Service.Config.GET_ALL_USERS_SUCCESS;
import static com.android.campusmoments.Service.Config.GET_MOMENTS_FAIL;
import static com.android.campusmoments.Service.Config.GET_MOMENTS_SUCCESS;
import static com.android.campusmoments.Service.Config.GET_USER_SUCCESS;
import static com.android.campusmoments.Service.Config.UNBLOCK_FAIL;
import static com.android.campusmoments.Service.Config.UNBLOCK_SUCCESS;
import static com.android.campusmoments.Service.Config.UNFOLLOW_FAIL;
import static com.android.campusmoments.Service.Config.UNFOLLOW_SUCCESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.campusmoments.Adapter.UserListAdapter;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Moment;
import com.android.campusmoments.Service.Services;
import com.android.campusmoments.Service.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private static final int TYPE_FOLLOW = 0;
    private static final int TYPE_FANS = 1;
    private static final int TYPE_BLOCK = 2;
    private static final String TAG = "UserListActivity";
    private RecyclerView userRecyclerView;
    private UserListAdapter userListAdapter;
    private int mType; // FOLLOW, FANS, BLOCK
    private List<User> mAllUsers;
    private TextView mFollowTextView;
    private TextView mFansTextView;
    private TextView mBlockTextView;
    private static Button tempButton;
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case GET_ALL_USERS_SUCCESS:
                    getUserSuccess(msg.obj);
                    break;
                case GET_ALL_USERS_FAIL:
                    getUserFail();
                    break;
                case FOLLOW_SUCCESS:
                    try {
                        followSuccess(msg.obj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case FOLLOW_FAIL:
                    followFail();
                    break;
                case UNFOLLOW_SUCCESS:
                    try {
                        unfollowSuccess(msg.obj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UNFOLLOW_FAIL:
                    unfollowFail();
                    break;
                case BLOCK_SUCCESS:
                    try {
                        blockSuccess(msg.obj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case BLOCK_FAIL:
                    blockFail();
                    break;
                case UNBLOCK_SUCCESS:
                    try {
                        unblockSuccess(msg.obj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UNBLOCK_FAIL:
                    unblockFail();
                    break;
            }
        }
    };

    private void getUserFail() {
        Toast.makeText(this, "获取用户列表失败", Toast.LENGTH_SHORT).show();
    }

    private void getUserSuccess(Object obj) {
        try {
            JSONArray arr = new JSONArray(obj.toString());
            Log.d(TAG, "onResponse: " + arr.length());
            mAllUsers.clear();
            mAllUsers = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                mAllUsers.add(new User(arr.getJSONObject(i)));
            }
            // print mAllUsers
            for (User user : mAllUsers) {
                Log.d(TAG, "getUserSuccess: " + user.toString());
            }
            refresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mFollowTextView = findViewById(R.id.follow_text);
        mFansTextView = findViewById(R.id.fans_text);
        mBlockTextView = findViewById(R.id.block_text);
        mAllUsers = new ArrayList<>();
        userListAdapter = new UserListAdapter(this, mAllUsers, mType);
        userListAdapter.buttonSetOnclick(new UserListAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position, int id) {
                tempButton = view.findViewById(R.id.item_button);
                if (mType == TYPE_FOLLOW || mType == TYPE_FANS) {
                    boolean isFollowed = Services.mySelf.followList.contains(id);
                    Services.follow(id, isFollowed, handler);
                } else if (mType == TYPE_BLOCK) {
                    boolean isBlocked = Services.mySelf.blockList.contains(id);
                    Services.block(id, isBlocked, handler);
                }
            }
        });
        userRecyclerView = findViewById(R.id.user_recycler_view);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setAdapter(userListAdapter);
        mType = getIntent().getIntExtra("type", TYPE_FOLLOW);
        Services.getAllUsers(handler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAllUsers.size() > 0) {
            refresh();
        }
    }

    public void setFollowUsers(View view) {
        mType = TYPE_FOLLOW;
        refresh();
    }
    public void setFansUsers(View view) {
        mType = TYPE_FANS;
        refresh();
    }
    public void setBlockUsers(View view) {
        mType = TYPE_BLOCK;
        refresh();
    }
    public void refresh() {
        // id list
        List<User> users = new ArrayList<>();
        if (mType == TYPE_FOLLOW) {
            mFollowTextView.setTextColor(getResources().getColor(R.color.purple_200));
            mFansTextView.setTextColor(getResources().getColor(R.color.gray));
            mBlockTextView.setTextColor(getResources().getColor(R.color.gray));
            for(int id : Services.mySelf.followList) {
                users.add(mAllUsers.get(id-1));
            }
        } else if (mType == TYPE_FANS) {
            mFollowTextView.setTextColor(getResources().getColor(R.color.gray));
            mFansTextView.setTextColor(getResources().getColor(R.color.purple_200));
            mBlockTextView.setTextColor(getResources().getColor(R.color.gray));
            for(int id : Services.mySelf.fansList) {
                users.add(mAllUsers.get(id - 1));
            }
        } else if (mType == TYPE_BLOCK) {
            mFollowTextView.setTextColor(getResources().getColor(R.color.gray));
            mFansTextView.setTextColor(getResources().getColor(R.color.gray));
            mBlockTextView.setTextColor(getResources().getColor(R.color.purple_200));
            for (int id : Services.mySelf.blockList) {
                users.add(mAllUsers.get(id-1));
            }
        }
        userListAdapter.setUsers(users);
        userListAdapter.setType(mType);
        userRecyclerView.setAdapter(userListAdapter);
        userListAdapter.notifyDataSetChanged();
    }
    private static void followFail() {}
    private static void followSuccess(Object obj) throws JSONException {
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.mySelf.followList = Services.jsonArrayToList(jsonObject.getJSONArray("follow_list"));
        tempButton.setText("已关注");
        tempButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
    }

    private static void unfollowFail() {}
    private static void unfollowSuccess(Object obj) throws JSONException {
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.mySelf.followList = Services.jsonArrayToList(jsonObject.getJSONArray("follow_list"));
        tempButton.setText("关注");
        tempButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF4081")));
    }

    private static void blockFail() {}
    private static void blockSuccess(Object obj) throws JSONException {
        tempButton.setText("已屏蔽");
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.mySelf.blockList = Services.jsonArrayToList(jsonObject.getJSONArray("block_list"));
    }

    private static void unblockFail() {}

    private static void unblockSuccess(Object obj) throws JSONException {
        tempButton.setText("屏蔽");
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.mySelf.blockList = Services.jsonArrayToList(jsonObject.getJSONArray("block_list"));
    }
    public void cancel(View view) {
        finish();
    }

}