package com.android.campusmoments.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.campusmoments.Activity.UserHomePageActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Comment;
import com.squareup.picasso.Picasso;


import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<Comment> mComments;
    public CommentAdapter(List<Comment> comments) {
        mComments = comments;
    }
    public void setComments(List<Comment> comments) {
        mComments = comments;
    }
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAvatarImageView;
        private TextView mUsernameTextView;
        private TextView mTimeTextView;
        private TextView mContentTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar);
            mUsernameTextView = itemView.findViewById(R.id.tv_name);
            mTimeTextView = itemView.findViewById(R.id.tv_time);
            mContentTextView = itemView.findViewById(R.id.tv_content);
        }
        public void bindData(Comment comment) {
            mUsernameTextView.setText(comment.getUsername());
            mTimeTextView.setText(comment.getTime());
            mContentTextView.setText(comment.getContent());
            if(comment.getAvatarPath() != null) {
                Picasso.get().load(comment.getAvatarPath()).into(mAvatarImageView);
            }
            // 点击头像跳转到用户主页
            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), UserHomePageActivity.class);
                    int id = comment.getUserId();
                    intent.putExtra("id", id);
                    view.getContext().startActivity(intent);
                }
            });
            // 点击用户名跳转到用户主页
            mUsernameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), UserHomePageActivity.class);
                    int id = comment.getUserId();
                    intent.putExtra("id", id);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }
    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.bindData(comment);
    }
    @Override
    public int getItemCount() {
        if(mComments == null)
            return 0;
        return mComments.size();
    }
}
