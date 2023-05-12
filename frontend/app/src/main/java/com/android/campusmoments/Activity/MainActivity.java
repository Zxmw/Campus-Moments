package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String token = mPreferences.getString("token", null);
//        if (token != null) {
//            Services.getSelf(token);
//            if (Services.mySelf == null) {
//                SharedPreferences.Editor editor = mPreferences.edit();
//                editor.clear();
//                editor.apply();
//                Toast.makeText(MainActivity.this, "登录信息已过期，请重新登录", Toast.LENGTH_SHORT).show();
//            }
//            else {
//            Intent intent = new Intent(MainActivity.this, PersonCenterActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
////            }
//        }
        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // 直接打开发布页，测试用
//        Intent intent = new Intent(MainActivity.this, PersonCenterActivity.class);
//        startActivity(intent);
//        finish();
    }

    public void gotoRegister(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}