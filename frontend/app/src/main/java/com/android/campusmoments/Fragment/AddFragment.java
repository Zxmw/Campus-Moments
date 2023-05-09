package com.android.campusmoments.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.android.campusmoments.Adapter.ImageAdapter;
import com.android.campusmoments.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddFragment extends Fragment {
    private SharedPreferences mSharedPreferences;
    private String sharedPrefFile = "com.android.campusmoments.addFragmentSharedPref";
    private int mAvatar;
    private String mUsername;
    private String mTime;
    private String mTitle;
    private List<Uri> mPictures;
    private String mContent;
    private GridView mPictureGridView;
    private EditText mTitleEditText;
    private EditText mContentEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AddFragment", "onCreate: getActivity" + getActivity());
        mSharedPreferences = requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        Log.d("AddFragment", "onCreate: mSharedPreferences" + mSharedPreferences);
        // 从SharedPreferences中获取标题，内容和图片
        mTitle = mSharedPreferences.getString("title", "");
        mContent = mSharedPreferences.getString("content", "");
        mPictures = new ArrayList<>();
        int pictureNum = mSharedPreferences.getInt("pictureNum", 0);
        for (int i = 0; i < pictureNum; i++) {
            String pictureUriString = mSharedPreferences.getString("picture" + i, "");
            Uri pictureUri = Uri.parse(pictureUriString);
            if (i != pictureNum - 1 || !pictureUri.equals(Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.bg_add_white))) {
                mPictures.add(pictureUri);
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("AddFragment", "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        Button mPublishButton = view.findViewById(R.id.publish_button);
        ImageView mAvatarImageView = view.findViewById(R.id.avatarImageView);
        mTitleEditText = view.findViewById(R.id.titleEditText);
        mContentEditText = view.findViewById(R.id.contentEditText);
        mTitleEditText.setText(mTitle);
        mContentEditText.setText(mContent);
        mPictureGridView = view.findViewById(R.id.pictureGridView);
        mPublishButton.setOnClickListener(v -> returnMoment());
        mAvatar = R.drawable.avatar_1;
        mAvatarImageView.setImageResource(mAvatar);
        mPictureGridView.setAdapter(new ImageAdapter(mPictures, 0));
        mPictureGridView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position == mPictures.size() - 1 && mPictures.size() <= 9) {
                if(mPictures.size() == 9 && !mPictures.get(mPictures.size() - 1).equals(Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.bg_add_white))) {
                    return;
                }
                mPictures.remove(mPictures.size() - 1);
                // 调用系统相册
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        });
        return view;
    }
    public void returnMoment() {

        View view = getView();
        assert view != null;
        if(mPictures.get(mPictures.size() - 1).equals(Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.bg_add_white))) {
            mPictures.remove(mPictures.size() - 1);
        }
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
        ArrayList<Uri> pictures = new ArrayList<>(mPictures);
        intent.putParcelableArrayListExtra("moment_pictures", pictures);
        intent.putExtra("moment_content", mContent);
        int mCommentCount = 0;
        intent.putExtra("moment_comment_count", mCommentCount);
        int mLikeCount = 0;
        intent.putExtra("moment_like_count", mLikeCount);
        int mFavoriteCount = 0;
        intent.putExtra("moment_favorite_count", mFavoriteCount);
        requireActivity().setResult(1, intent);


        // reset SharedPreferences
        mPictures = new ArrayList<>();
        mTitleEditText.setText("");
        mContentEditText.setText("");
        SharedPreferences.Editor preferencesEditor = mSharedPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();


        requireActivity().finish();
    }

    // Intent.ACTION_PICK
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        View view = getView();
        assert view != null;
        if (requestCode == 1 && resultCode == -1) {
            // 获取图片
            Uri uri = data.getData();
            Log.d("PublishActivity", "onActivityResult: " + uri);
            mPictures.add(uri);
            mPictureGridView.setAdapter(new ImageAdapter(mPictures, 0));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("PublishActivity", "onPause: " + mTitle + mContent + mPictures);
        mTitle = mTitleEditText.getText().toString();
        mContent = mContentEditText.getText().toString();
        // 将标题，内容和图片存入SharedPreferences
        SharedPreferences.Editor preferencesEditor = mSharedPreferences.edit();
        preferencesEditor.putString("title", mTitle);
        preferencesEditor.putString("content", mContent);
        preferencesEditor.putInt("pictureNum", mPictures.size());
        for (int i = 0; i < mPictures.size(); i++) {
            preferencesEditor.putString("picture" + i, mPictures.get(i).toString());
        }
        preferencesEditor.apply();
    }

}
