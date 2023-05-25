package com.android.campusmoments.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.campusmoments.Fragment.HomeFragment;
import com.android.campusmoments.Fragment.MomentsFragment;
import com.android.campusmoments.Fragment.MyFragment;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentContainerView fragmentContainerView;
    private HomeFragment homeFragment;
    private Fragment messageFragment;  // TODO: 消息
    private MyFragment myFragment;
    private Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        initFragment();
        setBottomNavListener();
    }
    private void initFragment() {
        homeFragment = new HomeFragment();
        messageFragment = new Fragment();
        myFragment = new MyFragment();
        fragmentManager = getSupportFragmentManager();
        // 默认显示homeFragment
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, homeFragment)
                .add(R.id.fragmentContainerView, messageFragment)
                .add(R.id.fragmentContainerView, myFragment)
                .commit();
//        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit();
        fragmentManager.beginTransaction().show(homeFragment).hide(messageFragment).hide(myFragment).commit();
        currentFragment = homeFragment;
    }
    private void showFragment(Fragment fragment) {
        if(fragment == currentFragment) return;
        fragmentManager.beginTransaction().hide(currentFragment).show(fragment).commit();
        currentFragment = fragment;
    }
    private void setBottomNavListener() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    showFragment(homeFragment);
                    return true;
                case R.id.message:
                    showFragment(messageFragment);
                    return true;
                case R.id.my:
                    showFragment(myFragment);
                    return true;
            }
            return false;
        });
        // TODO: setOnItemReselectedListener
        bottomNavigationView.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    homeFragment.refresh();
                    break;
                case R.id.message:
                    break;
                case R.id.my:
                    myFragment.refresh();
                    break;
            }
        });
    }
}