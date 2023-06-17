package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import static com.android.campusmoments.Service.Config.*;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordConfigActivity extends AppCompatActivity {

    private int step = 0;
    private TextView email_view;
    private LinearLayout email_layout;
    private LinearLayout password_layout;
    private LinearLayout confirm_layout;
    private LinearLayout token_layout;
    private LinearLayout uidb64_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_config);
        email_view = findViewById(R.id.email_text);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.new_pass_layout);
        password_layout.setVisibility(View.GONE);
        confirm_layout = findViewById(R.id.confirm_new_pass_layout);
        confirm_layout.setVisibility(View.GONE);
        token_layout = findViewById(R.id.token_layout);
        token_layout.setVisibility(View.GONE);
        uidb64_layout = findViewById(R.id.uidb64_layout);
        uidb64_layout.setVisibility(View.GONE);
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == REQUEST_PASSWORD_FAIL) {
                Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == REQUEST_PASSWORD_SUCCESS) {
                Toast.makeText(getApplicationContext(), "请求成功，请查收邮件", Toast.LENGTH_SHORT).show();
                email_layout.setVisibility(View.GONE);
                password_layout.setVisibility(View.VISIBLE);
                confirm_layout.setVisibility(View.VISIBLE);
                token_layout.setVisibility(View.VISIBLE);
                uidb64_layout.setVisibility(View.VISIBLE);
                step = 1;
            } else if (msg.what == SET_PASSWORD_SUCCESS) {
                successSetPassword();
            } else if (msg.what == SET_PASSWORD_FAIL) {
                failSetPassword();
            }
        }
    };

    public void setPassword(View view) {
        if (step == 0) {
            String email = email_view.getText().toString();
            if (!email.equals(Services.mySelf.email)) {
                Toast.makeText(this.getApplicationContext(), "邮箱不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            Services.requestResetPassword(email, handler);
        }
        else if (step == 1) {
            String password = ((TextView)findViewById(R.id.new_password)).getText().toString();
            String confirm = ((TextView)findViewById(R.id.confirm_new_password)).getText().toString();
            if (!password.equals(confirm)) {
                Toast.makeText(this.getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            String token = ((TextView)findViewById(R.id.token_text)).getText().toString();
            String uidb64 = ((TextView)findViewById(R.id.uidb64_text)).getText().toString();
            Services.resetPassword(password, token, uidb64, handler);
        }

    }
    private void successSetPassword() {
        Toast.makeText(this.getApplicationContext(), "重置密码成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void failSetPassword() {
        Toast.makeText(this.getApplicationContext(), "重置失败，请稍后再试", Toast.LENGTH_SHORT).show();
    }

    public void cancel(View view) {
        finish();
    }
}