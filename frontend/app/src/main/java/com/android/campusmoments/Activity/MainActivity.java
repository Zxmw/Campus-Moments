package com.android.campusmoments.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.campusmoments.R;
import com.android.campusmoments.Fragment.login.LoginFragment;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private final String sharedPrefFile = "com.android.campusmoments.user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
//        String token = mPreferences.getString("token", null);
//        if (token != null) {
//            // TODO:check if the token is valid
//            Intent intent = new Intent(MainActivity.this, MainMomentsActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        Button loginButton = findViewById(R.id.button_login);
//        loginButton.setOnClickListener(v -> {
//            loginButton.setVisibility(View.GONE);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragmentContainerView, new LoginFragment());
//            transaction.commit();
//        });

        // 直接打开发布页，测试用
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoRegister(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}