package com.android.campusmoments.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.android.campusmoments.Fragment.AddFragment;
import com.android.campusmoments.R;

public class PublishMomentActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private Fragment mAddFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_moment);
        mAddFragment = new AddFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mAddFragment)
                .commit();

    }
}