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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.campusmoments.Activity.HomeActivity;
import com.android.campusmoments.Activity.PubActivity;
import com.android.campusmoments.R;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentContainerView fragmentContainerView;
    public MomentsFragment allMomentsFragment;
    private ActivityResultLauncher<Intent> pubLauncher;
    private ImageView ivPub; // 发布按钮
    public HomeFragment() {
        allMomentsFragment = new MomentsFragment();
    }
    public void refresh() {
        allMomentsFragment.refresh();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerView);
        ivPub = view.findViewById(R.id.iv_add);
        setupPubLauncher();
        setupPub();

        getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, allMomentsFragment).commit();

        return view;
    }

    private void setupPubLauncher() {
        pubLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            allMomentsFragment.refresh();
                        }
                    }
                });
    }
    private void setupPub() {
        ivPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PubActivity.class);
                pubLauncher.launch(intent);
            }
        });
    }
}