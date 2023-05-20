package com.android.campusmoments.Adapter;

import static com.android.campusmoments.Service.Config.BLOCK_FAIL;
import static com.android.campusmoments.Service.Config.BLOCK_SUCCESS;
import static com.android.campusmoments.Service.Config.FOLLOW_FAIL;
import static com.android.campusmoments.Service.Config.FOLLOW_SUCCESS;
import static com.android.campusmoments.Service.Config.UNBLOCK_FAIL;
import static com.android.campusmoments.Service.Config.UNBLOCK_SUCCESS;
import static com.android.campusmoments.Service.Config.UNFOLLOW_FAIL;
import static com.android.campusmoments.Service.Config.UNFOLLOW_SUCCESS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.campusmoments.Activity.UserHomePageActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;
import com.android.campusmoments.Service.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder>{
    private Context mContext;
    private ButtonInterface buttonInterface;
    private static final int TYPE_FOLLOW = 0;
    private static final int TYPE_FANS = 1;
    private static final int TYPE_BLOCK = 2;
    private static final String TAG = "UserListAdapter";
    private List<User> mUsers;
    private int mType; // FOLLOW, FANS, BLOCK

    public UserListAdapter(Context context, List<User> users, int type) {
        mContext = context;
        mUsers = users;
        mType = type;
    }
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }
    public interface ButtonInterface{
        public void onclick(View view,int position, int id);
    }

    public void setUsers(List<User> users) {
        mUsers = users;
    }
    public void setType(int type) { mType = type; }

    public static class UserListViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAvatarImageView;
        private TextView mUsernameTextView;
        private TextView mBioTextView;
        private static Button mItemButton;
        int mType;
        public UserListViewHolder(View itemView, int type) {
            super(itemView);
            mType = type;
            mAvatarImageView = itemView.findViewById(R.id.avatarImageView);
            mUsernameTextView = itemView.findViewById(R.id.usernameTextView);
            mBioTextView = itemView.findViewById(R.id.bioTextview);
            mItemButton = itemView.findViewById(R.id.item_button);
        }

        public void bindData(User user, int position, ButtonInterface buttonInterface) {
            if(user.avatar != null) {
                Log.d(TAG, user.avatar);
                Picasso.get().load(Uri.parse(user.avatar)).into(mAvatarImageView);
            } else {
                Log.d(TAG, "bindData: avatarPath is null");
                mAvatarImageView.setImageResource(R.drawable.avatar_1); // 默认头像
            }
            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), UserHomePageActivity.class);
                    intent.putExtra("id", user.id);
                    v.getContext().startActivity(intent);
                }
            });
            mAvatarImageView.setVisibility(View.VISIBLE);
            mUsernameTextView.setText(user.username);
            mBioTextView.setText(user.bio);
            mItemButton.setTag(this);
            if (mType == TYPE_FOLLOW || mType == TYPE_FANS) {
                boolean isFollowed = Services.mySelf.followList.contains(user.id);
                if(isFollowed) {
                    mItemButton.setText("已关注");
                    mItemButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                } else {
                    mItemButton.setText("关注");
                    mItemButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF4081")));
                }
                mItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(buttonInterface!=null){
                            buttonInterface.onclick(v, position, user.id);
                        }
                    }
                });
            } else if (mType == TYPE_BLOCK) {
                boolean isBlocked = Services.mySelf.blockList.contains(user.id);
                if(isBlocked) {
                    mItemButton.setText("取消屏蔽");
                } else {
                    mItemButton.setText("屏蔽");
                }
                mItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(buttonInterface!=null){
                            buttonInterface.onclick(v, position, user.id);
                        }
                    }
                });
            }
            mItemButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public UserListViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new UserListViewHolder(view, mType);
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.bindData(user, position, buttonInterface);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
