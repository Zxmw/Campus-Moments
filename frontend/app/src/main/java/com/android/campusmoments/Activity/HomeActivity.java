package com.android.campusmoments.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.campusmoments.Fragment.HomeFragment;
import com.android.campusmoments.Fragment.MomentsFragment;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;

public class HomeActivity extends AppCompatActivity {

    private ImageButton homeButton, messageButton, mineButton;
    private ImageButton addMomentButton;
    private ImageButton profileButton;
    private ActivityResultLauncher<Intent> pubLauncher;

    HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // home page fragment
        FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView);
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(fragmentContainerView.getId(), homeFragment).commit();

        homeButton = findViewById(R.id.homeButton);
        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PersonCenterActivity.class);
                startActivity(intent);
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment.refresh();
            }
        });

        setupPubLauncher();
        addMomentButton = findViewById(R.id.addButton);
        addMomentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PubActivity.class);
                pubLauncher.launch(intent);
            }
        });
    }
    private void setupPubLauncher() {
        pubLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            homeFragment.refresh();
                            // todo : add moment
                            Intent data = result.getData();
                            if (data != null) {
//                                data.putExtra("username", /*TODO: getUsername*/);
//                                homeFragment.addMoment(data);
                            }
                        }
                    }
                });
    }
}