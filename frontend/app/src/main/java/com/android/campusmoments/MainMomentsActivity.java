package com.android.campusmoments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainMomentsActivity extends AppCompatActivity {

    private ConstraintLayout mConstraintLayout;
    private TabLayout mTabLayout;
    private BottomNavigationView mBottomNavigationView;

    private FragmentManager mFragmentManager;
    private HomeFragment mHomeFragment;
    private Fragment mTopicFragment;
    private Fragment mGuideFragment;
    private Fragment mMyFragment;
    private Fragment mNewCommentFragment;
    private Fragment mHotFragment;
    private Fragment mFollowFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_moments);
        mFragmentManager = getSupportFragmentManager();
//        mHomeFragment = new HomeFragment();
//        mTopicFragment = new TopicFragment();
//        mGuideFragment = new GuideFragment();
//        mMyFragment = new MyFragment();
//        mNewCommentFragment = new NewCommentFragment();
//        mHotFragment = new HotFragment();
//        mFollowFragment = new FollowFragment();
        mConstraintLayout = findViewById(R.id.top_navigation);
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText("新发表"));
        mTabLayout.addTab(mTabLayout.newTab().setText("新回复"));
        mTabLayout.addTab(mTabLayout.newTab().setText("热门"));
        mTabLayout.addTab(mTabLayout.newTab().setText("关注"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                switch (tab.getPosition()) {
                    case 0:
                        hideFragment(transaction);
                        if(mHomeFragment == null) {
                            mHomeFragment = new HomeFragment();
                            transaction.add(R.id.fragment_container, mHomeFragment);
                        }
                        transaction.show(mHomeFragment);
                        mConstraintLayout.setVisibility(ConstraintLayout.VISIBLE);
                        Objects.requireNonNull(mTabLayout.getTabAt(0)).select();
                        break;
                    case 1:
                        hideFragment(transaction);
                        if(mNewCommentFragment == null) {
                            mNewCommentFragment = new NewCommentFragment();
                            transaction.add(R.id.fragment_container, mNewCommentFragment);
                        }
                        transaction.show(mNewCommentFragment);
                        break;
                    case 2:
                        hideFragment(transaction);
                        if(mHotFragment == null) {
                            mHotFragment = new HotFragment();
                            transaction.add(R.id.fragment_container, mHotFragment);
                        }
                        transaction.show(mHotFragment);
                        break;
                    case 3:
                        hideFragment(transaction);
                        if(mFollowFragment == null) {
                            mFollowFragment = new FollowFragment();
                            transaction.add(R.id.fragment_container, mFollowFragment);
                        }
                        transaction.show(mFollowFragment);
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hideFragment(transaction);
                    if(mHomeFragment == null) {
                        mHomeFragment = new HomeFragment();
                        transaction.add(R.id.fragment_container, mHomeFragment);
                    }
                    transaction.show(mHomeFragment);
                    mConstraintLayout.setVisibility(ConstraintLayout.VISIBLE);
                    Objects.requireNonNull(mTabLayout.getTabAt(0)).select();
                    break;
                case R.id.navigation_topic:
                    mConstraintLayout.setVisibility(ConstraintLayout.GONE);
                    hideFragment(transaction);
                    if(mTopicFragment == null) {
                        mTopicFragment = new TopicFragment();
                        transaction.add(R.id.fragment_container, mTopicFragment);
                    }
                    transaction.show(mTopicFragment);
                    break;
                case R.id.navigation_add:
                    mConstraintLayout.setVisibility(ConstraintLayout.GONE);
                    Intent intent = new Intent(MainMomentsActivity.this, PublishMomentActivity.class);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.navigation_guide:
                    mConstraintLayout.setVisibility(ConstraintLayout.GONE);
                    hideFragment(transaction);
                    if(mGuideFragment == null) {
                        mGuideFragment = new GuideFragment();
                        transaction.add(R.id.fragment_container, mGuideFragment);
                    }
                    transaction.show(mGuideFragment);
                    break;
                case R.id.navigation_my:
                    mConstraintLayout.setVisibility(ConstraintLayout.GONE);
                    hideFragment(transaction);
                    if(mMyFragment == null) {
                        mMyFragment = new MyFragment();
                        transaction.add(R.id.fragment_container, mMyFragment);
                    }
                    transaction.show(mMyFragment);
                    break;
            }
            transaction.commit();
            return true;
        });

        //    onClick add button in button navigation view, goto publish activity
        mBottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_add:
                    Intent intent = new Intent(MainMomentsActivity.this, PublishMomentActivity.class);
                    startActivityForResult(intent, 1);
                    break;
            }
        });
        //   set default fragment
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if(mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            transaction.add(R.id.fragment_container, mHomeFragment);
        }
        transaction.show(mHomeFragment);
        transaction.commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            mHomeFragment.onActivityResult(requestCode, resultCode, data);
        }
        mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
        Objects.requireNonNull(mTabLayout.getTabAt(0)).select();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mTopicFragment != null) {
            transaction.hide(mTopicFragment);
        }
        if (mGuideFragment != null) {
            transaction.hide(mGuideFragment);
        }
        if (mMyFragment != null) {
            transaction.hide(mMyFragment);
        }
        if (mNewCommentFragment != null) {
            transaction.hide(mNewCommentFragment);
        }
        if (mHotFragment != null) {
            transaction.hide(mHotFragment);
        }
        if (mFollowFragment != null) {
            transaction.hide(mFollowFragment);
        }
    }
}