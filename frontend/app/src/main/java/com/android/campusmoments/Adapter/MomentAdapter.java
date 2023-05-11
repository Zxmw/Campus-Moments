package com.android.campusmoments.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.campusmoments.R;
import com.android.campusmoments.Service.Moment;

import java.util.List;

import io.github.mthli.knife.KnifeText;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MomentViewHolder> {
    private List<Moment> mMoments;
    private OnItemClickListener mOnItemClickListener;

    public MomentAdapter(List<Moment> moments, OnItemClickListener onItemClickListener) {
        mMoments = moments;
        mOnItemClickListener = onItemClickListener;
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
            mVideoView = itemView.findViewById(R.id.video_videoview);
            mAddressTextView = itemView.findViewById(R.id.address_textview);
            mLikeTextView = itemView.findViewById(R.id.like_textview);
            mCommentTextView = itemView.findViewById(R.id.comment_textview);
            mStarTextView = itemView.findViewById(R.id.star_textview);
        }
        public void bindData(Moment moment) {
            // 将数据绑定到ViewHolder中的视图中
            if(moment.getAvatar() != null) {
                mAvatarImageView.setImageURI(moment.getAvatar());
            } else {
                mAvatarImageView.setImageResource(R.drawable.avatar_1); // 默认头像
            }
            mUsernameTextView.setText(moment.getUsername());
            mTimeTextView.setText(moment.getTime());
            mTagTextView.setText(moment.getTag());
            mTitleTextView.setText(moment.getTitle());
            mContentKnifeText.fromHtml(moment.getContent());
            if(moment.getPicture() != null) {
                mPictureImageView.setImageURI(moment.getPicture());
            } else {
                mPictureImageView.setVisibility(View.GONE);
            }
            if(moment.getVideo() != null) {
                // TODO: VideoController
                mVideoView.setVideoURI(moment.getVideo());
            } else {
                mVideoView.setVisibility(View.GONE);
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
