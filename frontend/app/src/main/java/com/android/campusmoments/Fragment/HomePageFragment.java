package com.android.campusmoments.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.campusmoments.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        FragmentContainerView fragmentContainerView = view.findViewById(R.id.fragmentContainerView);
        // TODO: 根据mParam1的值，加载不同的Fragment
        if(mParam1.equals("关注")) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MomentsFragment(MomentsFragment.TYPE_FOLLOW)).commit();
        }
        if(mParam1.equals("推荐")) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MomentsFragment(MomentsFragment.TYPE_ALL)).commit();
        }
        if(mParam1.equals("热门")) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MomentsFragment(MomentsFragment.TYPE_HOT)).commit();
        }
        return view;
    }
}