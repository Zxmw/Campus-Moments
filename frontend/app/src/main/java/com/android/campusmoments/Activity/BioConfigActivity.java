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

import static com.android.campusmoments.Service.Config.*;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;

import org.json.JSONException;
import org.json.JSONObject;

public class BioConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_config);
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == SET_BIO_SUCCESS) {
                try {
                    successSetBio(msg.obj);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (msg.what == SET_BIO_FAIL) {
                failSetBio();
            }
        }
    };

    public void setBio(View view) {
        TextView bio_view = findViewById(R.id.user_id_text);
        String bio = bio_view.getText().toString();
        Services.setBio(bio, handler);
    }

    private void successSetBio(Object obj) throws JSONException {
        JSONObject jsonObject = new JSONObject(obj.toString());
        Services.mySelf.bio = jsonObject.getString("bio");
        Toast.makeText(this.getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void failSetBio() {
        Toast.makeText(this.getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();
    }

    public void cancel(View view) {
        finish();
    }
}