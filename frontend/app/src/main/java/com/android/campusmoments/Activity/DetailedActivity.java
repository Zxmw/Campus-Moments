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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.campusmoments.R;
import com.android.campusmoments.Service.Moment;
import com.android.campusmoments.Service.Services;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.mthli.knife.KnifeText;

public class DetailedActivity extends AppCompatActivity {

    private static final String TAG = "DetailedActivity";
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
    private Moment moment;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what == 0) {
                Toast.makeText(DetailedActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                finish();
            } else if(msg.what == 1) {
                try {
                    JSONObject obj = new JSONObject(msg.obj.toString());
                    moment = new Moment(obj);
                    Log.d(TAG, "handleMessage: " + moment.getAvatarPath());
                    Log.d(TAG, "handleMessage: " + moment.getUsername());
                    usernameTextView.setText(moment.getUsername());
                    timeTextView.setText(moment.getTime());
                    tagTextView.setText(moment.getTag());
                    titleTextView.setText(moment.getTitle());
                    contentKnifeText.fromHtml(moment.getContent());
                    if(moment.getAvatarPath() != null) {
                        Picasso.get().load(Uri.parse(moment.getAvatarPath())).into(avatarImageView);
                    } else {
                        avatarImageView.setImageResource(R.drawable.avatar_1);
                    }
                    if(moment.getImagePath() != null) {
                        Picasso.get().load(Uri.parse(moment.getImagePath())).into(pictureView);
                        Log.d(TAG, "handleMessage: imagePath: " + moment.getImagePath());
//                        pictureView.setImageURI(Uri.parse(moment.getImagePath()));
                    } else {
                        Log.d(TAG, "handleMessage: imagePath is null");
                        pictureView.setVisibility(ImageView.GONE);
                    }
                    if(moment.getVideoPath() != null) {
                        videoView.setVideoURI(Uri.parse(moment.getVideoPath()));
                        videoView.setMediaController(new MediaController(DetailedActivity.this));
                        videoView.start();
                    } else {
                        videoView.setVisibility(VideoView.GONE);
                    }
                    addressTextView.setText(moment.getAddress());
                    likeTextView.setText(String.valueOf(moment.getLikeCount()));
                    commentTextView.setText(String.valueOf(moment.getCommentCount()));
                    starTextView.setText(String.valueOf(moment.getStarCount()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };
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
        videoView = findViewById(R.id.videoView);
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
        int id = intent.getIntExtra("id", -1);
        if(id == -1) {
            Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Services.getDetailedMoment(id, handler);
            return;
        }

        // 老方法
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