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
import android.widget.TextView;
import android.widget.Toast;

public class PasswordConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_config);
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == SET_PASSWORD_SUCCESS) {
                successSetPassword();
            } else if (msg.what == SET_PASSWORD_FAIL) {
                failSetPassword();
            }
        }
    };

    public void setPassword(View view) {
        // 获得旧密码和新密码
        TextView old_password_view = findViewById(R.id.old_password);
        TextView new_password_view = findViewById(R.id.new_password);
        String old_password = old_password_view.getText().toString();
        String new_password = new_password_view.getText().toString();
        Services.setPassword(old_password, new_password, handler);
    }
    private void successSetPassword() {
        Toast.makeText(this.getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void failSetPassword() {
        Toast.makeText(this.getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();
    }

    public void cancel(View view) {
        finish();
    }
}