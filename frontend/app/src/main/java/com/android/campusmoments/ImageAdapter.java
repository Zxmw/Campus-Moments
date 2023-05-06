package com.android.campusmoments;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private List<Uri> images;
    private int where; // 0: add, 1: Main, 2: Detail

    public ImageAdapter(List<Uri> images, int where) {
        this.images = images;
        this.where = where;
        if(where == 0 && images.size() < 9) {
            images.add(Uri.parse("android.resource://com.android.campusmoments/" + R.drawable.bg_add_white));
        }
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            Log.d("ImageAdapter", "getView: " + position);
            imageView = new ImageView(parent.getContext());
            imageView.setBackgroundResource(R.drawable.bg_image);
            imageView.setClipToOutline(true);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        if(ContextCompat.checkSelfPermission(parent.getContext(), "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((MainMomentsActivity) parent.getContext(), new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 100);
        }
        imageView.setImageURI(images.get(position));
        return imageView;
    }
}