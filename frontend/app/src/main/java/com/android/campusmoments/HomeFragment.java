package com.android.campusmoments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<Moment> mMomentList;
    private RecyclerView mRecyclerView;
    private MomentAdapter mAdapter;

    private HomeFragmentListener mHomeFragmentListener;

    public interface HomeFragmentListener {
        void hideButtonNaviView();
        void showButtonNaviView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMomentList = new ArrayList<>();
        mMomentList.add(new Moment(R.drawable.avatar1, "Sweetnow", "昨天 10:18", "【THU春日足迹】几朵园子里的春花和鸳鸯",
                List.of(R.drawable.picture5),
                "学校里的春天，花还是很多的，荷塘也解冻了，有鸳鸯在游泳。", 2, 9, 5));
        mMomentList.add(new Moment(R.drawable.avatar2, "王政", "昨天 10:08", "【北大北大】",
                List.of(R.drawable.picture1),
                "北大我的北大我真的好想进去看看。", 999, 999, 999));
        mMomentList.add(new Moment(R.drawable.avatar2, "王政19", "昨天 9:08", "【美食美食不辜负】",
                List.of(R.drawable.picture15),
                "学校的好吃的和学校周边的好吃的。",
                1, 19, 3));
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.moment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MomentAdapter(mMomentList, new MomentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), DetailedMomentActivity.class);
                intent.putExtra("moment_avatar", mMomentList.get(position).getAvatar());
                intent.putExtra("moment_username", mMomentList.get(position).getUsername());
                intent.putExtra("moment_time", mMomentList.get(position).getTime());
                intent.putExtra("moment_title", mMomentList.get(position).getTitle());
                ArrayList<Integer> pictures = new ArrayList<Integer>(mMomentList.get(position).getPictures());
                intent.putIntegerArrayListExtra("moment_picture", pictures);
                intent.putExtra("moment_content", mMomentList.get(position).getContent());
                intent.putExtra("moment_comment_count", mMomentList.get(position).getCommentCount());
                intent.putExtra("moment_like_count", mMomentList.get(position).getLikeCount());
                intent.putExtra("moment_favorite_count", mMomentList.get(position).getFavoriteCount());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            assert data != null;
            int avatar = data.getIntExtra("moment_avatar", R.drawable.avatar1);
            String username = data.getStringExtra("moment_username");
            String time = data.getStringExtra("moment_time");
            String title = data.getStringExtra("moment_title");
            ArrayList<Integer> pictures = data.getIntegerArrayListExtra("moment_picture");
            String content = data.getStringExtra("moment_content");
            mMomentList.add(0, new Moment(avatar, username, time, title, pictures, content, 0, 0, 0));
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
