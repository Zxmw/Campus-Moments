package com.android.campusmoments.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.FontStyle;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import static com.android.campusmoments.Service.Config.*;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import io.github.mthli.knife.KnifeText;

public class PubActivity extends AppCompatActivity {

    // knifedemo
    private static final String BOLD = "<b>Bold</b><br><br>";
    private static final String ITALIT = "<i>Italic</i><br><br>";
    private static final String UNDERLINE = "<u>Underline</u><br><br>";
    private static final String STRIKETHROUGH = "<s>Strikethrough</s><br><br>"; // <s> or <strike> or <del>
    private static final String BULLET = "<ul><li>asdfg</li></ul>";
    private static final String QUOTE = "<blockquote>Quote</blockquote>";
    private static final String LINK = "<a href=\"https://github.com/mthli/Knife\">Link</a><br><br>";
    private static final String EXAMPLE = BOLD + ITALIT + UNDERLINE + STRIKETHROUGH + BULLET + QUOTE + LINK;



    private static final String TAG = "PubActivity";

    private final Handler pubMomentHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case PUB_MOMENT_FAIL:
                    Toast.makeText(PubActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    break;
                case PUB_MOMENT_SUCCESS:
                    Toast.makeText(PubActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private EditText tagView;
    private EditText titleView;


    // TODO: 富文本

//    private static final int REQUEST_CODE_KNIFE = 1;
    private ActivityResultLauncher<Intent> knifeLauncher; // 打开KnifeText编辑器
    private KnifeText knife;

    // 图片
    private ImageView pictureView;
    private Bitmap selectedPicture;
    private Uri selectedPictureUri;

    private ActivityResultLauncher<Intent> pickPhotoLauncher; // 从相册获取图片
    private ActivityResultLauncher<Intent> takePhotoLauncher; // 拍照获取图片

    // 视频
    private VideoView videoView;
    private ImageView videoBtn;
    private Uri selectedVideoUri;
    private ActivityResultLauncher<Intent> pickVideoLauncher; // 从相册获取视频
    private ActivityResultLauncher<Intent> takeVideoLauncher; // 拍摄获取视频

    // 定位
    private TextView locationView;
    private String addressStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);


        // 初始化控件
        tagView = findViewById(R.id.tag_view);
        titleView = findViewById(R.id.title_view);
        pictureView = findViewById(R.id.picture_view);
        videoView = findViewById(R.id.video_view);
        videoBtn = findViewById(R.id.video_button);
        locationView = findViewById(R.id.locationTextView);

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


        // MediaController - videoView - 播放/暂停/快进
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // 初始化图片获取 - 从相册获取图片
        pickPhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                try {
                                    // 获取选择的图片
                                    InputStream inputStream = getContentResolver().openInputStream(uri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    pictureView.setImageBitmap(bitmap);

                                    // 将选择的图片存储在局部变量中
                                    selectedPicture = bitmap;
                                    selectedPictureUri = uri;
                                    // TODO: 将图片上传到服务器
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

        // 初始化图片获取 - 拍照获取图片
        takePhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Bundle extras = result.getData().getExtras();
                            if (extras != null) {
                                // 获取拍摄的照片
                                Bitmap bitmap = (Bitmap) extras.get("data");
                                pictureView.setImageBitmap(bitmap);

                                // 将拍摄的照片存储在局部变量中
                                selectedPicture = bitmap;
                                // TODO: 将图片上传到服务器
                            }
                        }
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
                                // 获取选择的视频
                                videoView.setVideoURI(uri);
                                videoView.start(); // 开始播放
                                // 将选择的视频存储在局部变量中
                                selectedVideoUri = uri;
                                Log.d(TAG, "onActivityResult: "+selectedVideoUri);
                                // TODO: 将视频上传到服务器 SaveVideo(uri)
                            }
                        }
                    }
                });
        // 初始化视频获取 - 拍摄获取视频
        takeVideoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if(data != null) {
                                Uri uri = data.getData();
                                videoView.setVideoURI(uri);
                                videoView.start();  // 开始播放
                                // 将拍摄的视频存储在局部变量中
                                selectedVideoUri = uri;
                                // TODO: 将视频上传到服务器 SaveVideo(uri)
                            } else {
                                Log.e(TAG, "onActivityResult: data is null");
                            }
                        }
                    }
                });

    }

    // 事件处理
    public void cancel(View view) {
        // TODO: 取消发布
    }

    public void post(View view) {
        // TODO: 发布
        //  tag, title, content, pictureUri, videoUri, address
        // TODO: username 和 avatar 从用户中心获得
        Intent replyIntent = new Intent();
        replyIntent.putExtra("tag", tagView.getText().toString());
        replyIntent.putExtra("title", titleView.getText().toString());
        replyIntent.putExtra("content", knife.toHtml());
        replyIntent.putExtra("pictureUri", selectedPictureUri);
        replyIntent.putExtra("videoUri", selectedVideoUri);
        replyIntent.putExtra("address", locationView.getText().toString());
        setResult(RESULT_OK, replyIntent);

        String tag = tagView.getText().toString();
        String title = titleView.getText().toString();
        String content = knife.toHtml();
        String imagePath = selectedPictureUri == null ? null : getRealPathFromURI(selectedPictureUri);
        String videoPath = selectedVideoUri == null ? null : getRealPathFromURI(selectedVideoUri);
        String address = locationView.getText().toString();
        Services.pubMoment(tag, title, content, imagePath, videoPath, address, pubMomentHandler);

        finish();
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

    public void addPicture(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PubActivity.this);
        builder.setItems(new String[]{"从相册中选择", "拍照"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // 从相册中选择图片
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        pickPhotoLauncher.launch(intent);
                        break;
                    case 1:
                        // 调用相机拍照 - ACTION_IMAGE_CAPTURE 很难用，加Secure 就很好用
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                        takePhotoLauncher.launch(takePictureIntent);
                        break;
                }
            }
        });
        builder.show();
    }

    public void addVideo(View view) {
        PopupMenu popupMenu = new PopupMenu(PubActivity.this, videoBtn);
        popupMenu.getMenuInflater().inflate(R.menu.video_source_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.pick_video:
                    // 两种选择方式，第一种只能从相册视频选择，第2种可以打开文件管理器
                    //Intent pickVideoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("video/*");
                    pickVideoLauncher.launch(intent);
                    return true;
                case R.id.record_video:
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        // 已经授权，可以使用相机
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        takeVideoLauncher.launch(takeVideoIntent);
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
                locationView.setText(String.format("latitude=%.1f, longitude=%.1f", latitude, longitude));
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
                    locationView.setText(addressLine);
                    addressStr = addressLine;
                } else {
                    // 如果没有获取到地址信息，可以给出提示信息
                    Toast.makeText(this, "Unable to get the address.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}