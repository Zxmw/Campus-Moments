package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAIL = 1;
    TextView username_login;
    TextView password_login;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_login = findViewById(R.id.username_login);
        password_login = findViewById(R.id.password_login);
        Services.setLoginHandler(handler);

        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOGIN_SUCCESS) {
                try {
                    successLogin(msg.obj);
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (msg.what == LOGIN_FAIL) {
                failLogin();
            }
        }
    };


    public void login(View view) {
        String username = username_login.getText().toString();
        String password = password_login.getText().toString();
        // 如果用户名或密码为空
        if (username.equals("") || password.equals("")) {
            // Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (username.length() < 4 || username.length() > 20) {
            // Toast.makeText(LoginActivity.this, "用户名长度应为4-20位", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6 || password.length() > 20) {
            // Toast.makeText(LoginActivity.this, "密码长度应为6-20位", Toast.LENGTH_SHORT).show();
        } else {
            Services.login(username, password);
        }
    }

    private void failLogin() {
        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
    }

    private void successLogin(Object obj) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.token = jsonObject.getString("token");
        editor.putString("user", jsonObject.getString("user"));
        editor.putString("token", jsonObject.getString("token"));
        editor.apply();
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        Services.getSelf(Services.token);
        Intent intent = new Intent(LoginActivity.this, PersonCenterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}