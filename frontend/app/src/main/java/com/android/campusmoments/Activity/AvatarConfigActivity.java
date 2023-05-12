package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AvatarConfigActivity extends AppCompatActivity {
    private ImageView avatar;
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
        Button saveButton = findViewById(R.id.save_avatar);
        selectButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
        saveButton.setOnClickListener(v -> {
            // 获取avatar中的图象
            Bitmap bitmap = Bitmap.createBitmap(avatar.getWidth(), avatar.getHeight(), Bitmap.Config.ARGB_8888);
            // 将bitmap转换为base64
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String base64 = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
            Services.setAvatar(base64);
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
            avatar.setImageURI(data.getData());
        }
    }

    private void successSetAvatar() {
        Toast.makeText(AvatarConfigActivity.this, "头像设置成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void failSetAvatar() {
        Toast.makeText(AvatarConfigActivity.this, "头像设置失败", Toast.LENGTH_SHORT).show();
    }

    public void cancel(View view) {
        finish();
    }
}