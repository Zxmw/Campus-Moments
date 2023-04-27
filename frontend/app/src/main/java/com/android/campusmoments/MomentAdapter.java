package com.android.campusmoments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        private TextView mTitleTextView;

        private LinearLayout mPictureLinearLayout;
        private GridView mPictureGridView;
        private TextView mContentTextView;
        private TextView mCommentCountTextView;
        private TextView mLikeCountTextView;
        private TextView mFavoriteCountTextView;
        public MomentViewHolder(View itemView) {
            super(itemView);
            mAvatarImageView = itemView.findViewById(R.id.avatarImageView);
            mUsernameTextView = itemView.findViewById(R.id.usernameTextView);
            mTimeTextView = itemView.findViewById(R.id.timeTextView);
            mTitleTextView = itemView.findViewById(R.id.titleTextView);
            mPictureLinearLayout = itemView.findViewById(R.id.pictureLinearLayout);
            mPictureGridView = itemView.findViewById(R.id.pictureGridView);
            mContentTextView = itemView.findViewById(R.id.contentTextView);
            mCommentCountTextView = itemView.findViewById(R.id.commentCountTextView);
            mLikeCountTextView = itemView.findViewById(R.id.likeCountTextView);
            mFavoriteCountTextView = itemView.findViewById(R.id.favoriteCountTextView);
        }
        public void bindData(Moment moment) {
            // 将数据绑定到ViewHolder中的视图中
            mAvatarImageView.setImageResource(moment.getAvatar());
            mUsernameTextView.setText(moment.getUsername());
            mTimeTextView.setText(moment.getTime());
            mTitleTextView.setText(moment.getTitle());
            mContentTextView.setText(moment.getContent());
            mCommentCountTextView.setText(String.valueOf(moment.getCommentCount()));
            mLikeCountTextView.setText(String.valueOf(moment.getLikeCount()));
            mFavoriteCountTextView.setText(String.valueOf(moment.getFavoriteCount()));

            List<Integer> pictures = moment.getPictures();
            if (pictures.size() == 0) {
                mPictureLinearLayout.setVisibility(View.GONE);
                mPictureGridView.setVisibility(View.GONE);
            } else {
                mPictureLinearLayout.setVisibility(View.VISIBLE);
                mPictureGridView.setVisibility(View.VISIBLE);
                mPictureGridView.setAdapter(new ImageAdapter(pictures));
            }
        }
    }

    @Override
    public MomentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_detaied_moment, parent, false);
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
