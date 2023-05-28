package com.android.campusmoments.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.android.campusmoments.Fragment.MyFragment;
import com.android.campusmoments.Fragment.NotificationFragment;
import com.android.campusmoments.R;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NotificationFragment notificationFragment = new NotificationFragment(1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentContainerView_notification, notificationFragment).commit();
        fragmentManager.beginTransaction().show(notificationFragment).commit();
    }


}