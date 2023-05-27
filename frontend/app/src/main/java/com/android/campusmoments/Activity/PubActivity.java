package com.android.campusmoments.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import static com.android.campusmoments.Service.Config.*;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Notification;
import com.android.campusmoments.Service.Services;
import com.android.campusmoments.Service.User;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import io.github.mthli.knife.KnifeText;

public class PubActivity extends AppCompatActivity {


    private static final String TAG = "PubActivity";

    private final Handler pubMomentHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case PUB_MOMENT_FAIL:
                    Toast.makeText(PubActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    break;
                case PUB_MOMENT_SUCCESS:
                    // from msg.obj get the moment id
                    try {
                        JSONObject jsonObject = new JSONObject((String)msg.obj);
                        int momentId = jsonObject.getInt("id");
                        for(Integer followerId : Services.mySelf.fansList){
                            firebaseDatabase.getReference("notifications").child(followerId.toString()).push()
                                    .setValue(new Notification(Services.mySelf.id, momentId,"你关注的" + Services.mySelf.username + "发布了新的动态",2));
                        }
                        Toast.makeText(PubActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private EditText tagView;
    private Spinner tagSpinner;
    private EditText titleView;


    // TODO: 富文本

//    private static final int REQUEST_CODE_KNIFE = 1;
    private ActivityResultLauncher<Intent> knifeLauncher; // 打开KnifeText编辑器
    private KnifeText knife;

    // 图片
    private ImageView pictureView;
    private String imagePath;

    private ActivityResultLauncher<Intent> pickPhotoLauncher; // 从相册获取图片
    private Uri imageUri; // 拍摄图片的Uri
    private ActivityResultLauncher<Uri> cameraLauncher; // 拍照获取图片

    // 视频
    private VideoView videoView;
    private FrameLayout videoLayout;
    private String videoPath;

    private ActivityResultLauncher<Intent> pickVideoLauncher; // 从相册获取视频
    private ActivityResultLauncher<Intent> takeVideoLauncher; // 拍摄获取视频
    private Uri videoUri; // 拍摄视频的Uri
    private ActivityResultLauncher<Uri> cameraVideoLauncher; // 拍摄获取视频

    // 定位
    private TextView positionTextView;
    private LinearLayout positionLayout;
    private ImageView positionImageView;

    private ImageButton photoBtn;
    private ImageButton videoBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);

//        Services.setPubMomentHandler(pubMomentHandler);

        ImageView avatarView = findViewById(R.id.avatarImageView);
        if(Services.mySelf.avatar != null){
            Picasso.get().load(Uri.parse(Services.mySelf.avatar)).into(avatarView);
        } else {
            avatarView.setImageResource(R.drawable.avatar_1);
        }

        // 初始化控件
        tagView = findViewById(R.id.tag_view);
        // 标签下拉框
        tagSpinner = findViewById(R.id.tagSpinner);
        String[] tagOptions = new String[]{"校园", "生活", "学习", "娱乐", "二手", "其他"};
        // 创建一个适配器，用于设置下拉框的选项内容
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tagOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将适配器设置给下拉框
        tagSpinner.setAdapter(adapter);
        tagSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tagView.setText("#" + tagOptions[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        titleView = findViewById(R.id.title_view);
        pictureView = findViewById(R.id.picture_view);
        videoView = findViewById(R.id.videoView);

        videoLayout = findViewById(R.id.videoLayout);
        // 定位
        positionTextView = findViewById(R.id.positionTextView);
        positionLayout = findViewById(R.id.positionLayout);
        positionImageView = findViewById(R.id.positionImageView);

        photoBtn = findViewById(R.id.photoBtn);
        videoBtn = findViewById(R.id.videoBtn);

        // KnifeText - 富文本相关
        knife = (KnifeText) findViewById(R.id.knifeText);

        knifeLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                String contentHtmlString = data.getStringExtra("content_html_string");
                                knife.fromHtml(contentHtmlString);
                            }
                        }
                    }
                });

        // knife.fromHtml(LINK); // 测试
        knife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PubActivity.this, KnifeActivity.class);
                intent.putExtra("content_html_string", knife.toHtml());
                knifeLauncher.launch(intent);
            }
        });




        // 初始化图片获取 - 从相册获取图片
        pickPhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                handleSelectedPicture(uri);
                            }
                        }
                    }
                });
        // 初始化图片获取 - 拍摄获取图片
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                handleSelectedPicture(imageUri);
            }
        });
        // 初始化视频获取 - 从相册获取视频
        pickVideoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                handleSelectedVideo(uri);
                            }
                        }
                    }
                });
        // 初始化视频获取 - 拍摄获取视频
        takeVideoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // 处理拍摄的视频
                handleSelectedVideo(videoUri);
            }
        });
        // 初始化视频获取 - 拍摄获取视频
        cameraVideoLauncher = registerForActivityResult(new ActivityResultContracts.TakeVideo(), result -> {
            try {

                Log.d(TAG, "cameraVideoLauncher: " + result);
                handleSelectedVideo(videoUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 事件处理
    public void cancel(View view) {
        finish();
    }

    public void post(View view) {
        String tag = tagView.getText().toString();
        String title = titleView.getText().toString();
        String content = knife.toHtml();
        String address = positionTextView.getText().toString();
        Services.pubMoment(pubMomentHandler, tag, title, content, imagePath, videoPath, address);
        // 通知上一个页面刷新
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void handleSelectedPicture(Uri uri) {
        pictureView.setImageURI(uri);
        pictureView.setVisibility(View.VISIBLE);
        imagePath = getRealPathFromURI(uri);
    }
    public void handleSelectedVideo(Uri uri) {
        videoPath = getRealPathFromURI(uri);

        // 获取视频的宽度和高度
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        String widthString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String heightString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        int videoWidth = Integer.parseInt(widthString);
        int videoHeight = Integer.parseInt(heightString);
        // 计算视频的宽高比
        float aspectRatio = (float) videoWidth / videoHeight;
        // 根据视频的宽高比，设置视频的宽度和高度
        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.width = videoLayout.getWidth();
        layoutParams.height = (int) (layoutParams.width * aspectRatio);
        // TODO: 这里的宽和高的处理有问题，有的是横屏的，有的是竖屏的
//        layoutParams.height = (int) (layoutParams.width / aspectRatio);
        videoView.setLayoutParams(layoutParams);
        videoView.setVideoURI(uri);
        videoView.setVisibility(View.VISIBLE);
        videoView.start();
        // MediaController 播放/暂停/快进/快退
        videoView.setMediaController(new MediaController(this));

    }
    public String getRealPathFromURI(Uri uri) {
        // 只有从相册中选择的图片才有真实路径，文件管理器中的图片没有真实路径
        Log.d(TAG, "getRealPathFromURI: " + uri);
        String realPath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            realPath = cursor.getString(columnIndex);
            cursor.close();
        } else {
            realPath = uri.getPath();
        }
        Log.d(TAG, "getRealPathFromURI: " + realPath);
        return realPath;
    }
    // 拍照时，创建一个临时的文件保存图片
    private Uri createImageUri() {
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        // 生成唯一的文件名
        String fileName = "temp_image_" + System.currentTimeMillis() + ".jpg";
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }
    // 录制视频时，创建一个临时的文件保存视频
    private Uri createVideoUri() {
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        // 生成唯一的文件名
        String fileName = "temp_video_" + System.currentTimeMillis() + ".mp4";
//        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES); // 保存到电影目录下
//        contentValues.put(MediaStore.Video.Media.TITLE, "temp_video");
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");

        return contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public void addPhoto(View view) {
        PopupMenu popupMenu = new PopupMenu(PubActivity.this, photoBtn);
        popupMenu.getMenuInflater().inflate(R.menu.photo_source_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.pick_photo:
                    // 两种选择方式，第一种只能从相册图片选择，第2种可以打开文件管理器
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("image/*");
                    pickPhotoLauncher.launch(intent);
                    return true;
                case R.id.take_photo:
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        takePhotoLauncher.launch(takePhotoIntent);
                        imageUri = createImageUri();
                        cameraLauncher.launch(imageUri);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                    }
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    public void addVideo(View view) {
        PopupMenu popupMenu = new PopupMenu(PubActivity.this, videoBtn);
        popupMenu.getMenuInflater().inflate(R.menu.video_source_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.pick_video:
                    // 两种选择方式，第一种只能从相册视频选择，第2种可以打开文件管理器
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("video/*");
                    pickVideoLauncher.launch(intent);
                    return true;
                case R.id.record_video:
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        // 已经授权，可以使用相机
                        // 启动相机应用
                        videoUri = createVideoUri();
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                        takeVideoLauncher.launch(takeVideoIntent);
//                        cameraVideoLauncher.launch(videoUri);
                        return true;
                    } else {
                        // 未授权，需要向用户请求相机权限
                        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, 101);
                    }
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    public void addLocation(View view) {
        // TODO: 添加位置
        // 获取LocationManager对象
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 判断是否有权限访问位置信息
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，请求权限
            Log.d(TAG, "addLocation: request permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // 如果有权限，获取位置信息
            Log.d(TAG, "addLocation: get location");
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                // 获取经度和纬度
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d(TAG, "addLocation: latitude="+latitude+" longitude="+longitude);
                // 将经度和纬度设置到TextView中, 保留1位小数， 这里是为了调试
                positionTextView.setText(String.format("latitude=%.1f, longitude=%.1f", latitude, longitude));
                // 将经度和纬度转换为地址信息
                // TODO: Google Service 需要翻墙
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "get address failed", Toast.LENGTH_SHORT).show();
                }

                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    String addressLine = address.getAddressLine(0);
                    // 将获取到的地址信息设置到TextView中
                    positionTextView.setText(addressLine);
                    // 将地址信息的TextView设置为可见
                    positionLayout.setVisibility(View.VISIBLE);
                    positionImageView.setVisibility(View.VISIBLE);
                    positionTextView.setVisibility(View.VISIBLE);
                } else {
                    // 如果没有获取到地址信息，可以给出提示信息
                    Toast.makeText(this, "Unable to get the address.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}