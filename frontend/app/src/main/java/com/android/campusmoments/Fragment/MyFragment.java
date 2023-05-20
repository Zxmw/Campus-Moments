package com.android.campusmoments.Fragment;

import static com.android.campusmoments.Service.Config.SET_MOMENT_USER_SUCCESS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.android.campusmoments.Activity.PersonCenterActivity;
import com.android.campusmoments.Activity.UserListActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Services;
import com.squareup.picasso.Picasso;

public class MyFragment extends Fragment {
    public static final int TYPE_ALL = 0;
    public static final int TYPE_PERSON = 1;
    private static final int TYPE_FOLLOW = 0;
    private static final int TYPE_FANS = 1;
    private static final int TYPE_BLOCK = 2;
    private static int id;
    private ImageButton gotoSettings;
    private ImageView avatar;
    private TextView selfUsername;
    private TextView selfUserId;
    private TextView selfFollowingNum;
    private TextView selfFansNum;
    private TextView selfBlockNum;

    private LinearLayout followingLayout;
    private LinearLayout fansLayout;
    private LinearLayout blockLayout;
    private FragmentContainerView fragmentContainerView;
    public MomentsFragment selfMomentsFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MyFragment", "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        gotoSettings = view.findViewById(R.id.gotoSettings);
        gotoSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonCenterActivity.class);
                startActivity(intent);
            }
        });
        avatar = view.findViewById(R.id.user_avatar);
        if (Services.mySelf.avatar == null){
            avatar.setImageResource(R.drawable.avatar_1);
        }
        else {
            Picasso.get().load(Uri.parse(Services.mySelf.avatar)).into(avatar);
        }
        selfUsername = view.findViewById(R.id.username_text);
        selfUsername.setText(Services.mySelf.username);
        selfUserId = view.findViewById(R.id.id_text);
        selfUserId.setText(String.valueOf(Services.mySelf.id));
        selfFollowingNum = view.findViewById(R.id.following_number_text);
        selfFollowingNum.setText(String.valueOf(Services.mySelf.followList.size()));
        followingLayout = view.findViewById(R.id.following_layout);
        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserListActivity.class);
                intent.putExtra("type", TYPE_FOLLOW);
                startActivity(intent);
            }
        });

        selfFansNum = view.findViewById(R.id.fans_number_text);
        selfFansNum.setText(String.valueOf(Services.mySelf.fansList.size()));
        fansLayout = view.findViewById(R.id.fans_layout);
        fansLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserListActivity.class);
                intent.putExtra("type", TYPE_FANS);
                startActivity(intent);
            }
        });

        selfBlockNum = view.findViewById(R.id.block_number_text);
        selfBlockNum.setText(String.valueOf(Services.mySelf.blockList.size()));
        blockLayout = view.findViewById(R.id.block_layout);
        blockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserListActivity.class);
                intent.putExtra("type", TYPE_BLOCK);
                startActivity(intent);
            }
        });
        selfMomentsFragment = new MomentsFragment(TYPE_PERSON, Services.mySelf.id);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerView_my);
        getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_my, selfMomentsFragment).commit();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        Log.d("MyFragment", "refresh: ");
        if (Services.mySelf.avatar == null){
            avatar.setImageResource(R.drawable.avatar_1);
        }
        else {
            Picasso.get().load(Uri.parse(Services.mySelf.avatar)).into(avatar);
        }
        selfUsername.setText(Services.mySelf.username);
        selfUserId.setText(String.valueOf(Services.mySelf.id));
        selfFollowingNum.setText(String.valueOf(Services.mySelf.followList.size()));
        selfFansNum.setText(String.valueOf(Services.mySelf.fansList.size()));
        selfBlockNum.setText(String.valueOf(Services.mySelf.blockList.size()));
        selfMomentsFragment.refresh();
    }
}
