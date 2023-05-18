package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import static com.android.campusmoments.Service.Config.*;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Moment;
import com.android.campusmoments.Service.Services;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
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
    private PlayerControlView playerControlView;
    private TextView addressTextView;
    private TextView likeTextView;
    private TextView commentTextView;
    private TextView starTextView;

    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private Button sendCommentButton;
    private Moment moment;

    private Handler setMomentUserHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what == SET_MOMENT_USER_FAIL) {
                Toast.makeText(DetailedActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                finish();
            } else if(msg.what == SET_MOMENT_USER_SUCCESS) {
                if(moment.getAvatarPath() != null) {
                    Picasso.get().load(Uri.parse(moment.getAvatarPath())).into(avatarImageView);
                } else {
                    avatarImageView.setImageResource(R.drawable.avatar_1);
                }
                usernameTextView.setText(moment.getUsername());
                timeTextView.setText(moment.getTime());
                tagTextView.setText(moment.getTag());
                titleTextView.setText(moment.getTitle());
                contentKnifeText.fromHtml(moment.getContent());
                if(moment.getImagePath() != null) {
                    Picasso.get().load(Uri.parse(moment.getImagePath())).into(pictureView);
                } else {
                    pictureView.setVisibility(ImageView.GONE);
                }

                if(moment.getVideoPath() != null) {
                    Log.d(TAG, "handleMessage: " + moment.getVideoPath());
//                    videoView.setVideoURI(Uri.parse(moment.getVideoPath()));
//                    videoView.setMediaController(new MediaController(DetailedActivity.this));
//                    videoView.start();
                    videoView.setVisibility(VideoView.GONE);
                    setPlayerControlView(moment.getVideoPath());
                } else {
                    videoView.setVisibility(VideoView.GONE);
                }
                addressTextView.setText(moment.getAddress());
                likeTextView.setText(String.valueOf(moment.getLikeCount()));
                commentTextView.setText(String.valueOf(moment.getCommentCount()));
                starTextView.setText(String.valueOf(moment.getStarCount()));
            }
        }
    };
    private Handler getMomentHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what == GET_MOMENT_FAIL) {
                Toast.makeText(DetailedActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                finish();
            } else if(msg.what == GET_MOMENT_SUCCESS) {
                try {
                    JSONObject obj = new JSONObject(msg.obj.toString());
                    moment = new Moment(obj);
                    Services.setMomentUser(0, moment, setMomentUserHandler);
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
        playerControlView = findViewById(R.id.playerControlView);
        addressTextView = findViewById(R.id.address_textview);
        likeTextView = findViewById(R.id.like_textview);
        commentTextView = findViewById(R.id.comment_textview);
        starTextView = findViewById(R.id.star_textview);
        commentsRecyclerView = findViewById(R.id.commentRecyclerview);
        commentEditText = findViewById(R.id.comment_edittext);
        sendCommentButton = findViewById(R.id.sendcomment_button);

        setData();

        // TODO: setup sendCommentButton
        // TODO: setup commentsRecyclerView
    }

    private void setData() { // 从intent中获取id，然后获取moment
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if(id == -1) {
            Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Services.getMomentById(id, getMomentHandler);
        }
    }
    private void setPlayerControlView(@NonNull String videoPath) {
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        playerControlView.setPlayer(player);
        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoPath));
        // Set the media item to be played.
        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.play();
    }
}