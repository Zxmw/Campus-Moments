package com.android.campusmoments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private List<Integer> images;

    public ImageAdapter(List<Integer> images) {
        this.images = images;
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
//            Log.d("ImageAdapter", "getView: " + position);
            imageView = new ImageView(parent.getContext());
            imageView.setBackgroundResource(R.drawable.bg_image);
            imageView.setClipToOutline(true);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            // 设置背景为圆角

        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(images.get(position));
        return imageView;
    }
}
