package com.android.campusmoments.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.campusmoments.R;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentContainerView fragmentContainerView;
    public MomentsFragment allMomentsFragment;
    public HomeFragment() {
        // Required empty public constructor
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

        getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, allMomentsFragment).commit();

        return view;
    }
}