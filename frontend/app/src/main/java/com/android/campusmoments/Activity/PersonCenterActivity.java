package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.android.campusmoments.Service.Config.*;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;
import com.squareup.picasso.Picasso;

public class PersonCenterActivity extends AppCompatActivity {
//  个人中心页面，从用户主页点击进入

    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        mPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "onResume");
        initViews();
    }

    private void initViews() {
        ImageView userAvatar = findViewById(R.id.user_avatar);
        Log.d("avatar", Services.mySelf.avatar + "");
        if (Services.mySelf.avatar == null) {
            // use R.drawable.default_avatar
            userAvatar.setImageResource(R.drawable.avatar_1);
        }
        else {
            Picasso.get().load(Uri.parse(Services.mySelf.avatar)).into(userAvatar);
        }
        TextView username = findViewById(R.id.user_id_text);
        username.setText(Services.mySelf.username);
        TextView bio = findViewById(R.id.user_bio_text);
        bio.setText(Services.mySelf.bio);
    }
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOGOUT_SUCCESS) {
                successLogout();
            } else if (msg.what == LOGOUT_FAIL) {
                failLogout();
            }
        }
    };

    private void successLogout() {
        mPreferences.edit().clear().apply();
        Services.mySelf = null;
        Toast.makeText(this, "退出成功", Toast.LENGTH_SHORT).show();
        // go back to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void failLogout() {
        Toast.makeText(this, "退出失败", Toast.LENGTH_SHORT).show();
    }

    public void gotoSetAvatar(View view) {
        Intent intent = new Intent(this, AvatarConfigActivity.class);
        startActivity(intent);
    }
    public void gotoSetUsername(View view) {
        Intent intent = new Intent(this, UsernameConfigActivity.class);
        startActivity(intent);
    }

    public void gotoSetBio(View view) {
        Intent intent = new Intent(this, BioConfigActivity.class);
        startActivity(intent);
    }

    public void gotoSetPassword(View view) {
        Intent intent = new Intent(this, PasswordConfigActivity.class);
        startActivity(intent);
    }

    public void gotoNotification(View view) {
//        Intent intent = new Intent(this, NotificationActivity.class);
//        startActivity(intent);
    }

    public void cancel(View view){
        finish();
    }

    public void logout(View view) {
        Services.logout(handler);
    }

}