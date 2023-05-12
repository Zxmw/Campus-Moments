package com.android.campusmoments.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.android.campusmoments.R;
import android.os.Bundle;
import android.view.View;

public class UserHomePageActivity extends AppCompatActivity {
    // 是否已经关注用户
    private boolean isFollowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
    }

    public void follow(View view) {
        // Do something in response to button
    }

    public void privateMessage(View view) {
        // Do something in response to button
    }

    public void block(View view) {
        // Do something in response to button
    }

    public void cancel(View view) {
        // Do something in response to button
    }
}