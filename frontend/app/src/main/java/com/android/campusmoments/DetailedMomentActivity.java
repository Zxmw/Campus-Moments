package com.android.campusmoments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailedMomentActivity extends AppCompatActivity {

    private int mMomentAvatar;
    private String mMomentUsername;
    private String mMomentTime;
    private String mMomentTitle;
    private List<Integer> mMomentPicture;
    private String mMomentContent;
    private int mMomentCommentCount;
    private int mMomentLikeCount;
    private int mMomentFavoriteCount;
    private ImageView mReturnButton;
    private ImageView mAvatarImageView;
    private TextView mUsernameTextView;
    private TextView mTimeTextView;
    private TextView mTitleTextView;
    private GridView mPictureGridView;
    private TextView mContentTextView;
    private TextView mCommentCountTextView;
    private TextView mLikeCountTextView;
    private TextView mFavoriteCountTextView;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_moment);

        mReturnButton = findViewById(R.id.return_buttton);
        mReturnButton.setOnClickListener(v -> {
            finish();
        });

        mMomentAvatar = getIntent().getIntExtra("moment_avatar", 0);
        mMomentUsername = getIntent().getStringExtra("moment_username");
        mMomentTime = getIntent().getStringExtra("moment_time");
        mMomentTitle = getIntent().getStringExtra("moment_title");
        mMomentPicture = getIntent().getIntegerArrayListExtra("moment_picture");
        mMomentContent = getIntent().getStringExtra("moment_content");
        mMomentCommentCount = getIntent().getIntExtra("moment_comment_count", 0);
        mMomentLikeCount = getIntent().getIntExtra("moment_like_count", 0);
        mMomentFavoriteCount = getIntent().getIntExtra("moment_favorite_count", 0);

        mAvatarImageView = findViewById(R.id.avatarImageView);
        mAvatarImageView.setImageResource(mMomentAvatar);
        mUsernameTextView = findViewById(R.id.usernameTextView);
        mUsernameTextView.setText(mMomentUsername);
        mTimeTextView = findViewById(R.id.timeTextView);
        mTimeTextView.setText(mMomentTime);
        mTitleTextView = findViewById(R.id.titleTextView);
        mTitleTextView.setText(mMomentTitle);
        mPictureGridView = findViewById(R.id.pictureGridView);
        mPictureGridView.setAdapter(new ImageAdapter(mMomentPicture));

        mContentTextView = findViewById(R.id.contentTextView);
        mContentTextView.setText(mMomentContent);
        mCommentCountTextView = findViewById(R.id.commentCountTextView);
        mCommentCountTextView.setText(String.valueOf(mMomentCommentCount));
        mLikeCountTextView = findViewById(R.id.likeCountTextView);
        mLikeCountTextView.setText(String.valueOf(mMomentLikeCount));
        mFavoriteCountTextView = findViewById(R.id.favoriteCountTextView);
        mFavoriteCountTextView.setText(String.valueOf(mMomentFavoriteCount));

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("moment_avatar", mMomentAvatar);
        outState.putString("moment_username", mMomentUsername);
        outState.putString("moment_time", mMomentTime);
        outState.putString("moment_title", mMomentTitle);
        outState.putIntegerArrayList("moment_picture", (ArrayList<Integer>) mMomentPicture);
        outState.putString("moment_content", mMomentContent);
        outState.putInt("moment_comment_count", mMomentCommentCount);
        outState.putInt("moment_like_count", mMomentLikeCount);
        outState.putInt("moment_favorite_count", mMomentFavoriteCount);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMomentAvatar = savedInstanceState.getInt("moment_avatar");
        mMomentUsername = savedInstanceState.getString("moment_username");
        mMomentTime = savedInstanceState.getString("moment_time");
        mMomentTitle = savedInstanceState.getString("moment_title");
        mMomentPicture = savedInstanceState.getIntegerArrayList("moment_picture");
        mMomentContent = savedInstanceState.getString("moment_content");
        mMomentCommentCount = savedInstanceState.getInt("moment_comment_count");
        mMomentLikeCount = savedInstanceState.getInt("moment_like_count");
        mMomentFavoriteCount = savedInstanceState.getInt("moment_favorite_count");
    }
}