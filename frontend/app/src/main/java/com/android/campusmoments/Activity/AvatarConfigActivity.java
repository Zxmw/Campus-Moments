package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AvatarConfigActivity extends AppCompatActivity {
    private ImageView avatar;
    private boolean edit = false;
    private String avatarPath;
    public static final int SET_AVATAR_SUCCESS = 0;
    public static final int SET_AVATAR_FAIL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_config);
        avatar = findViewById(R.id.avatar_config_show);
        avatar.post(() -> {
            avatar.getLayoutParams().height = avatar.getWidth();
        });
//        avatar.setImageURI(Uri.parse(Services.mySelf.avatar));
        avatar.setImageURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.avatar_1));
        Button selectButton = findViewById(R.id.select_avatar);
        Button saveButton = findViewById(R.id.register_button);
        selectButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
        saveButton.setOnClickListener(v -> {
            if (!edit) {
                Toast.makeText(AvatarConfigActivity.this, "请先选择头像", Toast.LENGTH_SHORT).show();
            }
            else {
                Services.setAvatar(avatarPath);
            }
        });
        Services.setSetAvatarHandler(handler);
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == SET_AVATAR_SUCCESS) {
                successSetAvatar();
            } else if (msg.what == SET_AVATAR_FAIL) {
                failSetAvatar();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 获得选择图片的uri
            Uri tempUri = data.getData();
            avatar.setImageURI(tempUri);
            avatarPath = getRealPathFromURI(tempUri);
            edit = true;
        }
    }

    private void successSetAvatar() {
        Toast.makeText(AvatarConfigActivity.this, "头像设置成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void failSetAvatar() {
        Toast.makeText(AvatarConfigActivity.this, "头像设置失败", Toast.LENGTH_SHORT).show();
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public void cancel(View view) {
        finish();
    }
}