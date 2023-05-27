package com.android.campusmoments.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.campusmoments.Fragment.MomentsFragment;
import com.android.campusmoments.R;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    private ImageView ivReturn; // 返回按钮
    private EditText etSearch; // 搜索框
    private TextView tvSearch; // 搜索按钮
    private FragmentContainerView fragmentContainerView; // 搜索结果
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }
    private void initView() {
        ivReturn = findViewById(R.id.iv_return);
        setupIvReturn();
        etSearch = findViewById(R.id.et_search);
        tvSearch = findViewById(R.id.tv_search);
        setupTvSearch();
        fragmentContainerView = findViewById(R.id.fragmentContainerView);
    }
    private void setupIvReturn() {
        ivReturn.setOnClickListener(v -> {
            finish();
        });
    }
    private void setupTvSearch() {
        tvSearch.setOnClickListener(v -> {
            if(etSearch.getText().toString().equals("")) {
                Toast.makeText(this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            // 以空格分割搜索内容
            String[] searchContent = etSearch.getText().toString().trim().split("\\s+");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MomentsFragment(MomentsFragment.TYPE_SEARCH, searchContent)).commit();
        });
    }
}