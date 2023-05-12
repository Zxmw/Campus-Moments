package com.android.campusmoments.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;
import com.squareup.picasso.Picasso;

public class PersonCenterActivity extends AppCompatActivity {
//  个人中心页面，从用户主页点击进入
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        ImageView userAvatar = findViewById(R.id.user_avatar);
        Picasso.get().load(Uri.parse(Services.mySelf.avatar)).into(userAvatar);
        TextView username = findViewById(R.id.username_person_center);
        username.setText(Services.mySelf.username);
        TextView bio = findViewById(R.id.bio_person_center);
        bio.setText(Services.mySelf.bio);
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

}