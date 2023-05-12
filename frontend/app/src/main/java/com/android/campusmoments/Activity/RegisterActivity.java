package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;

public class RegisterActivity extends AppCompatActivity {
    public static final int REGISTER_SUCCESS = 0;
    public static final int REGISTER_FAIL = 1;
    TextView username_register;
    TextView password_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username_register = findViewById(R.id.username_register);
        password_register = findViewById(R.id.password_register);
        Services.setRegisterHandler(handler);
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == REGISTER_SUCCESS) {
                successRegister();
            } else if (msg.what == REGISTER_FAIL) {
                failRegister();
            }
        }
    };

    public void register(View view) {
        String username = username_register.getText().toString();
        String password = password_register.getText().toString();
        // 如果用户名或密码为空
        if (username.equals("") || password.equals("")) {
            Toast.makeText(RegisterActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (username.length() < 4 || username.length() > 20) {
            Toast.makeText(RegisterActivity.this, "用户名长度应为4-20位", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(RegisterActivity.this, "密码长度应为6-20位", Toast.LENGTH_SHORT).show();
        } else {
            Services.register(username, password);
        }
    }

    private void successRegister() {
        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void failRegister() {
        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
    }

    public void returnToLogin(View view) {
        finish();
    }
}