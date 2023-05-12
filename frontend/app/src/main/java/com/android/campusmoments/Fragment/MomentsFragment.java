package com.android.campusmoments.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.campusmoments.Activity.DetailedActivity;
import com.android.campusmoments.Adapter.MomentAdapter;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Moment;

import java.util.ArrayList;
import java.util.List;

public class MomentsFragment extends Fragment {
    private List<Moment> mMomentList;
    private RecyclerView momentsRecyclerView;
    private MomentAdapter momentAdapter;


    public MomentsFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMomentList = new ArrayList<>();
        mMomentList.add(new Moment(null, "Sweetnow", "二手交易", "THU春日足迹", "几朵园子里的春花和鸳鸯",
                Uri.parse("https://pics4.baidu.com/feed/0eb30f2442a7d933b1f65b421211f61f73f0010f.jpeg@f_auto?token=4914a109a410fd49d266364470e42677"), null, "THU-102b"));
        mMomentList.add(new Moment(Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.picture5),
                "王政", "校园日常", "北大北大", "北大我的北大我真的好想进去看看。",
                Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.picture1), null, "PKU-102a"));
        mMomentList.add(new Moment(Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.picture15),
        "geh", "美食", "美食美食不辜负", "学校的好吃的和学校周边的好吃的。",
                Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.picture9), null, "PKU"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moments, container, false);

        momentsRecyclerView = view.findViewById(R.id.momentsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        momentsRecyclerView.setLayoutManager(layoutManager);
        momentAdapter = new MomentAdapter(mMomentList, new MomentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "clicked: "+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("avatarUri", mMomentList.get(position).getAvatar());
                intent.putExtra("username", mMomentList.get(position).getUsername());
                intent.putExtra("time", mMomentList.get(position).getTime());
                intent.putExtra("tag", mMomentList.get(position).getTag());
                intent.putExtra("title", mMomentList.get(position).getTitle());
                intent.putExtra("content", mMomentList.get(position).getContent());
                intent.putExtra("pictureUri", mMomentList.get(position).getPicture());
                intent.putExtra("videoUri", mMomentList.get(position).getVideo());
                intent.putExtra("address", mMomentList.get(position).getAddress());
                intent.putExtra("likeCount", mMomentList.get(position).getLikeCount());
                intent.putExtra("commentCount", mMomentList.get(position).getCommentCount());
                intent.putExtra("starCount", mMomentList.get(position).getStarCount());
                startActivity(intent);
                // TODO: adapter.notifyDataSetChanged(); 评论后刷新
            }
        });
        momentsRecyclerView.setAdapter(momentAdapter);
        return view;
    }

    public void addMoment(@NonNull Intent data){
        // tag, title, content, pictureUri, videoUri, address
        String username = "guo";
        String tag = data.getStringExtra("tag");
        String title = data.getStringExtra("title");
        String content = data.getStringExtra("content");
        Uri pictureUri = data.getParcelableExtra("pictureUri");
        Uri videoUri = data.getParcelableExtra("videoUri");
        String address = data.getStringExtra("address");
        mMomentList.add(0, new Moment(null, username, tag, title, content, pictureUri, videoUri, address));
        momentAdapter.notifyDataSetChanged();
    }
}