package com.android.campusmoments.Activity;

import static com.android.campusmoments.Service.Config.firebaseDatabase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.campusmoments.Fragment.HomeFragment;
import com.android.campusmoments.Fragment.MomentsFragment;
import com.android.campusmoments.Fragment.MyFragment;
import com.android.campusmoments.Fragment.NotificationFragment;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;
import com.android.campusmoments.Service.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentContainerView fragmentContainerView;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private MyFragment myFragment;
    private Fragment currentFragment;

    private static ChildEventListener childEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        initFragment();
        setBottomNavListener();
        initNotificationListener();
    }

    private void initNotificationListener() {
         childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int senderId = snapshot.child("senderId").getValue(Integer.class);
                int momentId = snapshot.child("momentId").getValue(Integer.class);
                String content = snapshot.child("content").getValue(String.class);
                String username = snapshot.child("username").getValue(String.class);
                boolean isNotified = snapshot.child("isNotified").getValue(Boolean.class);
                if(isNotified) return;
                Services.sendNotification(senderId, momentId, content, username, getApplicationContext());
                firebaseDatabase.getReference("notifications").child(String.valueOf(Services.mySelf.id)).child(Objects.requireNonNull(snapshot.getKey())).child("isNotified").setValue(true);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        firebaseDatabase.getReference("notifications").child(String.valueOf(Services.mySelf.id)).addChildEventListener(childEventListener);
    }
    private void initFragment() {
        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();
        myFragment = new MyFragment();
        fragmentManager = getSupportFragmentManager();
        // 默认显示homeFragment
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, homeFragment)
                .add(R.id.fragmentContainerView, notificationFragment)
                .add(R.id.fragmentContainerView, myFragment)
                .commit();
//        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit();
        fragmentManager.beginTransaction().show(homeFragment).hide(notificationFragment).hide(myFragment).commit();
        currentFragment = homeFragment;
    }
    private void showFragment(Fragment fragment) {
        if(fragment == currentFragment) return;
        fragmentManager.beginTransaction().hide(currentFragment).show(fragment).commit();
        currentFragment = fragment;
    }
    @SuppressLint("NonConstantResourceId")
    private void setBottomNavListener() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    showFragment(homeFragment);
                    return true;
                case R.id.message:
                    notificationFragment.refresh();
                    showFragment(notificationFragment);
                    return true;
                case R.id.my:
                    showFragment(myFragment);
                    return true;
            }
            return false;
        });
    }
}