package com.android.campusmoments.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
    private int type;
    private int userId = -1;
    private List<Moment> mMomentList;
    private int cnt = 0;
    private RecyclerView momentsRecyclerView;
    private MomentAdapter momentAdapter;
    private Handler setMomentUserHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == SET_MOMENT_USER_SUCCESS) {
                int index = msg.arg1;
                mMomentList.set(index, (Moment) msg.obj);
                cnt--;
                if(cnt == 0) {
                    momentAdapter.setMoments(mMomentList);
                    momentAdapter.notifyDataSetChanged();
                    refreshing = false;
                }
            }
        }
    };
    private Handler getMomentsHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_MOMENTS_SUCCESS) {
                try {
                    JSONArray arr = new JSONArray(msg.obj.toString());
                    Log.d(TAG, "onResponse: " + arr.length());
                    mMomentList.clear();
                    mMomentList = new ArrayList<>(arr.length());
                    // init mMomentList
                    for (int i = 0; i < arr.length(); i++) {
                        mMomentList.add(null);
                    }
                    cnt = arr.length();
                    for (int i = 0; i < arr.length(); i++) {
                        Moment moment = new Moment(arr.getJSONObject(i));
                        Services.setMomentUser(i, moment, setMomentUserHandler);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == GET_MOMENTS_FAIL) {
                Toast.makeText(requireActivity(), "获取动态失败", Toast.LENGTH_SHORT).show();
            }
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

        momentsRecyclerView = view.findViewById(R.id.following_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        momentsRecyclerView.setLayoutManager(layoutManager);
        momentAdapter = new MomentAdapter(mMomentList, new MomentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(getContext(), "clicked: "+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                intent.putExtra("id", mMomentList.get(position).getId());
                startActivity(intent);
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