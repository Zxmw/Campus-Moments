package com.android.campusmoments.Adapter;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;
import com.android.campusmoments.Activity.UserHomePageActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Moment;
import com.android.campusmoments.Service.Services;
import com.squareup.picasso.Picasso;

import java.util.List;
import io.github.mthli.knife.KnifeText;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MomentViewHolder> {
    private static final String TAG = "MomentAdapter";
    private List<Moment> mMoments;
    private static OnItemClickListener mOnItemClickListener;
    public MomentAdapter(List<Moment> moments, OnItemClickListener onItemClickListener) {
        mMoments = moments;
        mOnItemClickListener = onItemClickListener;
    }

    public void setMoments(List<Moment> moments) {
        mMoments = moments;
    }
    public List<Moment> getMomentsList() {
        return mMoments;
    }
    public interface OnItemClickListener {
        void onItemClick(View v, int position, int id);
        void onAvatarClick(View v, int position, int id);
        void onLikeClick(int position);
        void onStarClick(int position);
    }

    public static class MomentViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvatarImageView;
        private TextView mUsernameTextView;
        private TextView mTimeTextView;
        private TextView mTagTextView;
        private TextView mTitleTextView;

        private KnifeText mContentKnifeText;
        private ImageView mPictureImageView;
        private ImageView mVideoFrameView;
        private VideoView mVideoView;
        private LinearLayout posLayout;
        private ImageView posImageView;
        private TextView mAddressTextView;
        private ImageView mLikeImageView;
        private TextView mLikeTextView;
        private TextView mCommentTextView;
        private ImageView mStarImageView;
        private TextView mStarTextView;
        public MomentViewHolder(View itemView) {
            super(itemView);
            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mUsernameTextView = itemView.findViewById(R.id.username_textview);
            mTimeTextView = itemView.findViewById(R.id.time_textview);
            mTagTextView = itemView.findViewById(R.id.tag_textview);
            mTitleTextView = itemView.findViewById(R.id.title_textview);
            mContentKnifeText = itemView.findViewById(R.id.content_knifetext);
            mPictureImageView = itemView.findViewById(R.id.picture_imageview);
            mVideoView = itemView.findViewById(R.id.videoView);
            mVideoFrameView = itemView.findViewById(R.id.videoFrameView);
            posLayout = itemView.findViewById(R.id.posContainer);
            posImageView = itemView.findViewById(R.id.posImageView);
            mAddressTextView = itemView.findViewById(R.id.address_textview);
            mLikeImageView = itemView.findViewById(R.id.likeImageView);
            mLikeTextView = itemView.findViewById(R.id.like_textview);
            mCommentTextView = itemView.findViewById(R.id.comment_textview);
            mStarImageView = itemView.findViewById(R.id.starImageView);
            mStarTextView = itemView.findViewById(R.id.star_textview);
        }
        public void bindData(Moment moment) {
            int position = getBindingAdapterPosition();
            // 将数据绑定到ViewHolder中的视图中
            if(moment.getAvatarPath() != null) {
                Log.d(TAG, moment.getAvatarPath());
                Picasso.get().load(Uri.parse(moment.getAvatarPath())).into(mAvatarImageView);
            } else {
                Log.d(TAG, "bindData: avatarPath is null");
                mAvatarImageView.setImageResource(R.drawable.avatar_1); // 默认头像
            }
            mAvatarImageView.setVisibility(View.VISIBLE);
            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = moment.getUserId();
                    mOnItemClickListener.onAvatarClick(v, position, id);
                }
            });
            mUsernameTextView.setText(moment.getUsername());
            mUsernameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = moment.getUserId();
                    mOnItemClickListener.onAvatarClick(v, position, id);
                }
            });
            mTimeTextView.setText(moment.getTime());
            if (moment.getTag() != null && !moment.getTag().equals("")) {
                mTagTextView.setVisibility(View.VISIBLE);
                mTagTextView.setText(moment.getTag());
            } else {
                mTagTextView.setVisibility(View.GONE);
            }
            mTitleTextView.setText(moment.getTitle());
            if (moment.getContent() != null && !moment.getContent().equals("")) {
                mContentKnifeText.setVisibility(View.VISIBLE);
                mContentKnifeText.fromHtml(moment.getContent());
            } else {
                mContentKnifeText.setVisibility(View.GONE);
            }
            if(moment.getImagePath() != null) {
                Picasso.get().load(Uri.parse(moment.getImagePath())).into(mPictureImageView);
                mPictureImageView.setVisibility(View.VISIBLE);
            } else {
                mPictureImageView.setVisibility(View.GONE);
            }
            //TODO: 直接放一个图标表示存在视频，或者放一个视频的第一帧
            if(moment.getVideoPath() != null) {
//                mVideoView.setVideoURI(Uri.parse(moment.getVideoPath()));
//                mVideoView.setVisibility(View.VISIBLE);
//                mVideoView.start();
                mVideoFrameView.setVisibility(View.VISIBLE);
            } else {
                mVideoView.setVisibility(View.GONE);
                mVideoFrameView.setVisibility(View.GONE);
            }
            if (moment.getAddress() != null && !moment.getAddress().equals("")) {
                posLayout.setVisibility(View.VISIBLE);
                posImageView.setVisibility(View.VISIBLE);
                mAddressTextView.setVisibility(View.VISIBLE);
                mAddressTextView.setText(moment.getAddress());
            } else {
                posLayout.setVisibility(View.GONE);
                posImageView.setVisibility(View.GONE);
                mAddressTextView.setVisibility(View.GONE);
            }
            // TODO: 点赞、评论、收藏的点击事件
            if(moment.isLikedByMe) {
                mLikeImageView.setImageResource(R.drawable.ic_moment_thumbup_red);
            } else {
                mLikeImageView.setImageResource(R.drawable.ic_moment_thumbup);
            }
            mLikeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onLikeClick(position);
                }
            });
            mLikeTextView.setText(String.valueOf(moment.getLikeCount()));
            mCommentTextView.setText(String.valueOf(moment.getCommentCount()));
            mStarTextView.setText(String.valueOf(moment.getStarCount()));
            if(moment.isStaredByMe) {
                mStarImageView.setImageResource(R.drawable.ic_moment_star_yellow);
            } else {
                mStarImageView.setImageResource(R.drawable.ic_moment_star);
            }
            mStarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onStarClick(position);
                }
            });
        }
    }

    @Override
    public MomentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moment_overview, parent, false);
        final MomentViewHolder viewHolder = new MomentViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                int id = mMoments.get(position).getId();
                mOnItemClickListener.onItemClick(v, position, id);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MomentViewHolder holder, int position) {
        Moment moment = mMoments.get(position);
        holder.bindData(moment);
    }

    @Override
    public int getItemCount() {
        if (mMoments == null) {
            return 0;
        }
        return mMoments.size();
    }

}
