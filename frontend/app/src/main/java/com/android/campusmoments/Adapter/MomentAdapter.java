package com.android.campusmoments.Adapter;

import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.campusmoments.Activity.UserHomePageActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Moment;
import com.android.campusmoments.Service.Services;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import io.github.mthli.knife.KnifeText;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MomentViewHolder> {
    private static final String TAG = "MomentAdapter";
    private List<Moment> mMoments;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    public MomentAdapter(List<Moment> moments, OnItemClickListener onItemClickListener) {
        mMoments = moments;
        mOnItemClickListener = onItemClickListener;
    }
    public MomentAdapter(Context context, List<Moment> moments, OnItemClickListener onItemClickListener) {
        mContext = context;
        mMoments = moments;
        mOnItemClickListener = onItemClickListener;
    }
    public void setMoments(List<Moment> moments) {
        mMoments = moments;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
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
        private TextView mAddressTextView;
        private TextView mLikeTextView;
        private TextView mCommentTextView;
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
            mAddressTextView = itemView.findViewById(R.id.address_textview);
            mLikeTextView = itemView.findViewById(R.id.like_textview);
            mCommentTextView = itemView.findViewById(R.id.comment_textview);
            mStarTextView = itemView.findViewById(R.id.star_textview);
        }
        public void bindData(Moment moment) {
            // 将数据绑定到ViewHolder中的视图中
            if(moment.getAvatarPath() != null) {
                Log.d(TAG, moment.getAvatarPath());
                Picasso.get().load(Uri.parse(moment.getAvatarPath())).into(mAvatarImageView);

                mAvatarImageView.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "bindData: avatarPath is null");
                mAvatarImageView.setImageResource(R.drawable.avatar_1); // 默认头像
            }
            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), UserHomePageActivity.class);
                    intent.putExtra("id", moment.getUserId());
                    v.getContext().startActivity(intent);
                }
            });
            mUsernameTextView.setText(moment.getUsername());
            mUsernameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), UserHomePageActivity.class);
                    intent.putExtra("id", moment.getUserId());
                    v.getContext().startActivity(intent);
                }
            });
            mTimeTextView.setText(moment.getTime());
            mTagTextView.setText(moment.getTag());
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
            mAddressTextView.setText(moment.getAddress());
            mLikeTextView.setText(String.valueOf(moment.getLikeCount()));
            mCommentTextView.setText(String.valueOf(moment.getCommentCount()));
            mStarTextView.setText(String.valueOf(moment.getStarCount()));
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
                mOnItemClickListener.onItemClick(position);
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
