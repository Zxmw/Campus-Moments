package com.android.campusmoments.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.campusmoments.R;

import io.github.mthli.knife.KnifeText;

public class DetailedActivity extends AppCompatActivity {

    private ImageView avatarImageView;
    private TextView usernameTextView;
    private TextView timeTextView;
    private TextView tagTextView;
    private TextView titleTextView;
    private KnifeText contentKnifeText;
    private ImageView pictureView;
    private VideoView videoView;
    private TextView addressTextView;
    private TextView likeTextView;
    private TextView commentTextView;
    private TextView starTextView;

    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private Button sendCommentButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        // 初始化控件
        avatarImageView = findViewById(R.id.avatar_imageview);
        usernameTextView = findViewById(R.id.username_textview);
        timeTextView = findViewById(R.id.time_textview);
        tagTextView = findViewById(R.id.tag_textview);
        titleTextView = findViewById(R.id.title_textview);
        contentKnifeText = findViewById(R.id.content_knifetext);
        pictureView = findViewById(R.id.picture_imageview);
        videoView = findViewById(R.id.video_videoview);
        addressTextView = findViewById(R.id.address_textview);
        likeTextView = findViewById(R.id.like_textview);
        commentTextView = findViewById(R.id.comment_textview);
        starTextView = findViewById(R.id.star_textview);
        commentsRecyclerView = findViewById(R.id.commentRecyclerview);
        commentEditText = findViewById(R.id.comment_edittext);
        sendCommentButton = findViewById(R.id.sendcomment_button);


        // todo: thread 处理数据
        // TODO: 直接从远程数据库获取数据
        setData();
        // TODO: setup sendCommentButton
        // TODO: setup commentsRecyclerView
    }

    private void setData() {
        // get intent
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        Uri avatarUri = intent.getParcelableExtra("avatarUri");
        String username = intent.getStringExtra("username");
        String time = intent.getStringExtra("time");
        String tag = intent.getStringExtra("tag");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        Uri pictureUri = intent.getParcelableExtra("pictureUri");
        Uri videoUri = intent.getParcelableExtra("videoUri");
        String address = intent.getStringExtra("address");
        int likeCount = intent.getIntExtra("likeCount", 0);
        int commentCount = intent.getIntExtra("commentCount", 0);
        int starCount = intent.getIntExtra("starCount", 0);
        // set data
        avatarImageView.setImageURI(avatarUri);
        usernameTextView.setText(username);
        timeTextView.setText(time);
        tagTextView.setText(tag);
        titleTextView.setText(title);
        contentKnifeText.fromHtml(content);
        if (pictureUri != null) {
            pictureView.setImageURI(pictureUri);
        } else {
            pictureView.setVisibility(ImageView.GONE);
        }
        if (videoUri != null) {
            videoView.setVideoURI(videoUri);
            // TODO: MediaController, play video, permission
        } else {
            videoView.setVisibility(VideoView.GONE);
        }
        addressTextView.setText(address);
        likeTextView.setText(String.valueOf(likeCount));
        commentTextView.setText(String.valueOf(commentCount));
        starTextView.setText(String.valueOf(starCount));
    }
}