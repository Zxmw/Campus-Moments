package com.android.campusmoments.Fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static com.android.campusmoments.Service.Config.*;
import com.android.campusmoments.Activity.DetailedActivity;
import com.android.campusmoments.Adapter.MomentAdapter;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Moment;
import com.android.campusmoments.Service.Services;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MomentsFragment extends Fragment {
    private static final String TAG = "MomentsFragment";
    private boolean refreshing = false;

    public static final int TYPE_ALL = 0;
    public static final int TYPE_PERSON = 1;
    private final int type;
    private int userId = -1;
    private List<Moment> mMomentList;
    private RecyclerView momentsRecyclerView;
    private MomentAdapter momentAdapter;
    private ActivityResultLauncher<Intent> detailedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        refresh();
                    }
                }
            });
    private final Handler getMomentsHandler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_MOMENTS_SUCCESS) {
                try {
                    JSONArray arr = new JSONArray(msg.obj.toString());
                    Log.d(TAG, "onResponse: " + arr.length());
                    mMomentList.clear();
                    mMomentList = new ArrayList<>(arr.length());
                    for (int i = 0; i < arr.length(); i++) {
                        mMomentList.add(new Moment(arr.getJSONObject(i)));
                    }
                    momentAdapter.setMoments(mMomentList);
                    momentAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == GET_MOMENTS_FAIL) {
                Toast.makeText(requireActivity(), "获取动态失败", Toast.LENGTH_SHORT).show();
            }
            refreshing = false;
        }
    };
    public MomentsFragment(int type, int userId) {
        this.type = type;
        this.userId = userId;
    }
    public MomentsFragment() {
        this.type = TYPE_ALL;
    }
    public void refresh() {
        if (refreshing) {
            return;
        }
        refreshing = true;
        if (type == TYPE_PERSON) {
            Services.getMomentsByUser(userId, getMomentsHandler);
        } else if(type == TYPE_ALL) {
            Services.getMomentsAll(getMomentsHandler);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMomentList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moments, container, false);

        momentsRecyclerView = view.findViewById(R.id.user_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        momentsRecyclerView.setLayoutManager(layoutManager);
        momentAdapter = new MomentAdapter(mMomentList, new MomentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(getContext(), "clicked: "+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                intent.putExtra("id", mMomentList.get(position).getId());
//                startActivity(intent);
                detailedLauncher.launch(intent);
            }

            @Override
            public void onLikeClick(int position) {
                Moment clickedMoment = mMomentList.get(position);
                Services.likeOrStar("like", clickedMoment.getId(), !clickedMoment.isLikedByMe, new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(@NonNull android.os.Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            clickedMoment.isLikedByMe = !clickedMoment.isLikedByMe;
                            int likeCount = clickedMoment.getLikeCount();
                            if(clickedMoment.isLikedByMe) {
                                clickedMoment.setLikeCount(likeCount + 1);
                            } else {
                                clickedMoment.setLikeCount(likeCount - 1);
                            }
                            mMomentList.set(position, clickedMoment);
                            momentAdapter.setMoments(mMomentList);
                            momentAdapter.notifyItemChanged(position);
                        } else if (msg.what == 0) {
                            Toast.makeText(requireActivity(), "点赞失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onStarClick(int position) {
                Moment clickedMoment = mMomentList.get(position);
                Services.likeOrStar("star", clickedMoment.getId(), !clickedMoment.isStaredByMe, new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(@NonNull android.os.Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            clickedMoment.isStaredByMe = !clickedMoment.isStaredByMe;
                            int starCount = clickedMoment.getStarCount();
                            if(clickedMoment.isStaredByMe) {
                                clickedMoment.setStarCount(starCount + 1);
                            } else {
                                clickedMoment.setStarCount(starCount - 1);
                            }
                            mMomentList.set(position, clickedMoment);
                            momentAdapter.setMoments(mMomentList);
                            momentAdapter.notifyItemChanged(position);
                        } else if (msg.what == 0) {
                            Toast.makeText(requireActivity(), "收藏失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        momentsRecyclerView.setAdapter(momentAdapter);
        if (type == TYPE_ALL) {
            Services.getMomentsAll(getMomentsHandler);
        } else if(type == TYPE_PERSON) {
            Services.getMomentsByUser(userId, getMomentsHandler);
        }
        return view;
    }
}