package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import static com.android.campusmoments.Service.Config.*;

import com.android.campusmoments.Fragment.CommentsFragment;
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
    private TextView followTextView;
    private TextView tagTextView;
    private TextView titleTextView;
    private KnifeText contentKnifeText;
    private ImageView pictureView;
    private PlayerView playerView;
    private LinearLayout locationLayout;
    private ImageView locationImageView;
    private TextView addressTextView;
    private TextView likeTextView;
    private ImageView likeImageView;
    private TextView commentTextView;
    private TextView starTextView;
    private ImageView starImageView;
    private ImageView shareImageView;
    private ImageView returnImageView; // 返回按钮
    private FragmentContainerView commentFragmentContainerView;
    private CommentsFragment commentsFragment;
    private EditText commentEditText;
    private Button sendCommentButton;
    private Moment moment;

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
                    refresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };
    private Handler postCommentHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what == 0) {
                Toast.makeText(DetailedActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
            } else if(msg.what == 1) {
                Toast.makeText(DetailedActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                commentEditText.setText("");
                Services.getMomentById(moment.getId(), getMomentHandler);
            }
        }
    };
    private Handler likeStarHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what == 0) {
                Toast.makeText(DetailedActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else if(msg.what == 1) {
                Toast.makeText(DetailedActivity.this, "成功", Toast.LENGTH_SHORT).show();
                Services.getMomentById(moment.getId(), getMomentHandler);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        // 初始化控件
        returnImageView = findViewById(R.id.returnImageView);
        avatarImageView = findViewById(R.id.avatar_imageview);
        usernameTextView = findViewById(R.id.username_textview);
        timeTextView = findViewById(R.id.time_textview);
        tagTextView = findViewById(R.id.tag_textview);
        followTextView = findViewById(R.id.follow_textview);
        titleTextView = findViewById(R.id.title_textview);
        contentKnifeText = findViewById(R.id.content_knifetext);
        pictureView = findViewById(R.id.picture_imageview);
        playerView = findViewById(R.id.playerView);
        locationLayout = findViewById(R.id.locationLayout);
        locationImageView = findViewById(R.id.locationImageView);
        addressTextView = findViewById(R.id.address_textview);
        likeTextView = findViewById(R.id.like_textview);
        likeImageView = findViewById(R.id.likeImageView);
        commentTextView = findViewById(R.id.comment_textview);
        starTextView = findViewById(R.id.star_textview);
        starImageView = findViewById(R.id.starImageView);
        commentFragmentContainerView = findViewById(R.id.commentFragmentContainerView);
        commentEditText = findViewById(R.id.comment_edittext);
        sendCommentButton = findViewById(R.id.sendcomment_button);

        commentsFragment = new CommentsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.commentFragmentContainerView, commentsFragment).commit();

        setupGoToUserHome();
        setupReturnView();
        setupLikeView();
        setupStarView();
        setupCommentBtn();
        setData();
    }
    private void setupGoToUserHome() {
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, UserHomePageActivity.class);
                intent.putExtra("id", moment.getUserId());
                startActivity(intent);
            }
        });
        usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, UserHomePageActivity.class);
                intent.putExtra("id", moment.getUserId());
                startActivity(intent);
            }
        });
    }
    private void setupReturnView() {
        returnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    // 点赞事件
    private void setupLikeView() {
        likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: likeImageView");
                Services.likeOrStar("like",  moment.getId(), !moment.isLikedByMe, likeStarHandler);
            }
        });
    }
    private void setupStarView() {
        starImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starImageView");
                Services.likeOrStar("star",  moment.getId(), !moment.isStaredByMe, likeStarHandler);
            }
        });
    }
    private void setupCommentBtn() {
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEditText.getText().toString();
                if(comment.equals("")) {
                    Toast.makeText(DetailedActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Services.postComment(postCommentHandler, comment, moment.getId());
                }
            }
        });
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
    // 显示网络视频
    private void setPlayerView(String videoPath) {
        // exoplayer: https://developer.android.com/guide/topics/media/exoplayer/hello-world
        if (videoPath == null) {
            playerView.setVisibility(VideoView.GONE);
            return;
        } else {
            playerView.setVisibility(VideoView.VISIBLE);
            ExoPlayer player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoPath));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        }
    }

    private void refresh() {
        if(moment.getAvatarPath() != null) {
            Picasso.get().load(Uri.parse(moment.getAvatarPath())).into(avatarImageView);
        } else {
            avatarImageView.setImageResource(R.drawable.avatar_1);
        }
        usernameTextView.setText(moment.getUsername());
        timeTextView.setText(moment.getTime());
        if(moment.getTag()==null || moment.getTag().equals("")) {
            tagTextView.setVisibility(TextView.GONE);
        } else {
            tagTextView.setText(moment.getTag());
        }
        if(!moment.isFollowedByMe) {
            followTextView.setVisibility(TextView.GONE);
        }
        titleTextView.setText(moment.getTitle());
        if(moment.getContent().equals("")) {
            contentKnifeText.setVisibility(KnifeText.GONE);
        } else {
            contentKnifeText.fromHtml(moment.getContent());
        }
        if(moment.getImagePath() != null) {
            Picasso.get().load(Uri.parse(moment.getImagePath())).into(pictureView);
        } else {
            pictureView.setVisibility(ImageView.GONE);
        }
        setPlayerView(moment.getVideoPath());
        if(moment.getAddress() == null || moment.getAddress().equals("")) {
            locationLayout.setVisibility(LinearLayout.GONE);
            locationImageView.setVisibility(ImageView.GONE);
            addressTextView.setVisibility(TextView.GONE);
        } else {
            addressTextView.setText(moment.getAddress());
        }

        likeTextView.setText(String.valueOf(moment.getLikeCount()));
        commentTextView.setText(String.valueOf(moment.getCommentCount()));
        starTextView.setText(String.valueOf(moment.getStarCount()));
        if(moment.isLikedByMe){
            likeImageView.setImageResource(R.drawable.ic_moment_thumbup_red);
        } else {
            likeImageView.setImageResource(R.drawable.ic_moment_thumbup);
        }
        if(moment.isStaredByMe){
            starImageView.setImageResource(R.drawable.ic_moment_star_yellow);
        } else {
            starImageView.setImageResource(R.drawable.ic_moment_star);
        }

        // 设置评论
        commentsFragment.setCommentIdList(moment.getCommentIds());
    }
}