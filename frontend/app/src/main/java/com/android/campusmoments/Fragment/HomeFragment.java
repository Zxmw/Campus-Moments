package com.android.campusmoments.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.campusmoments.Activity.HomeActivity;
import com.android.campusmoments.Activity.PubActivity;
import com.android.campusmoments.Activity.SearchActivity;
import com.android.campusmoments.Adapter.HomePagerAdapter;
import com.android.campusmoments.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";


    private ImageView ivSearch; // 搜索按钮
    private ImageView ivPub; // 发布按钮
    // 使用ViewPager2
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private List<String> mData = new ArrayList<>();

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        initData();
        initView(view);
        return view;
    }
    private void initData() {
        mData.add("关注");
        mData.add("最新");
        mData.add("热门");
        mData.add("点赞");
        mData.add("收藏");
    }

    private void initView(View view) {
        ivSearch = view.findViewById(R.id.iv_search);
        setupSearch();
        ivPub = view.findViewById(R.id.iv_add);
        setupPub();
        mTabLayout = view.findViewById(R.id.tabLayout);
        mViewPager2 = view.findViewById(R.id.viewPager2);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(requireActivity(), mData);
        mViewPager2.setAdapter(homePagerAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> tab.setText(mData.get(position))).attach();
    }

    private void setupSearch() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupPub() {
        ivPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PubActivity.class);
                startActivity(intent);
            }
        });
    }

}