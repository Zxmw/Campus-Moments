package com.android.campusmoments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddFragment extends Fragment {

    private int mAvatar;
    private String mUsername;
    private String mTime;
    private String mTitle;
    private List<Integer> mPictures;
    private String mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        Button mPublishButton = view.findViewById(R.id.publish_button);
        ImageView mAvatarImageView = view.findViewById(R.id.avatarImageView);
        GridView mPictureGridView = view.findViewById(R.id.pictureGridView);
        mPublishButton.setOnClickListener(v -> {
            returnMoment();
        });
        mAvatar = R.drawable.avatar_1;
        mPictures = new ArrayList<Integer>();
        mPictures.add(R.drawable.picture11);
        mPictures.add(R.drawable.picture12);
        mPictures.add(R.drawable.picture13);
        mPictures.add(R.drawable.picture14);
        mAvatarImageView.setImageResource(mAvatar);
        mPictureGridView.setAdapter(new ImageAdapter(mPictures));

        return view;
    }

    public void returnMoment() {
        View view = getView();
        assert view != null;
        TextView mUsernameTextView = view.findViewById(R.id.usernameTextView);
        mUsername = mUsernameTextView.getText().toString();
        EditText mTitleEditText = view.findViewById(R.id.titleEditText);
        mTitle = mTitleEditText.getText().toString();
        EditText mContentEditText = view.findViewById(R.id.contentEditText);
        mContent = mContentEditText.getText().toString();
        Log.d("PublishActivity", "returnMoment: " + mAvatar + mUsername + mTitle + mPictures + mContent);
        Intent intent = new Intent();
        intent.putExtra("moment_avatar", mAvatar);
        intent.putExtra("moment_username", mUsername);
        // get time in format of "yyyy-MM-dd HH:mm:ss"
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        mTime = dateFormat.format(date);
        intent.putExtra("moment_time", mTime);
        intent.putExtra("moment_title", mTitle);
        ArrayList<Integer> pictures = new ArrayList<Integer>(mPictures);
        intent.putIntegerArrayListExtra("moment_picture", pictures);
        intent.putExtra("moment_content", mContent);
        int mCommentCount = 0;
        intent.putExtra("moment_comment_count", mCommentCount);
        int mLikeCount = 0;
        intent.putExtra("moment_like_count", mLikeCount);
        int mFavoriteCount = 0;
        intent.putExtra("moment_favorite_count", mFavoriteCount);
        Objects.requireNonNull(getActivity()).setResult(1, intent);
        getActivity().finish();
    }

}
