package com.android.campusmoments.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.campusmoments.R;

public class PersonCenterActivity extends AppCompatActivity {
//  个人中心页面，从用户主页点击进入
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
    }
    public void gotoSetAvatar() {
        Intent intent = new Intent(this, AvatarConfigActivity.class);
        startActivity(intent);
    }
    public void gotoSetUsername() {
        Intent intent = new Intent(this, UsernameConfigActivity.class);
        startActivity(intent);
    }

    public void gotoSetBio() {
        Intent intent = new Intent(this, BioConfigActivity.class);
        startActivity(intent);
    }

    public void gotoSetPassword() {
        Intent intent = new Intent(this, PasswordConfigActivity.class);
        startActivity(intent);
    }

}