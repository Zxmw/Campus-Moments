package com.android.campusmoments.Fragment;

import static com.android.campusmoments.Service.Config.FIREBASE_DATABASE_URL;
import static com.android.campusmoments.Service.Config.GET_USER_FAIL;
import static com.android.campusmoments.Service.Config.GET_USER_SUCCESS;
import static com.android.campusmoments.Service.Config.firebaseDatabase;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.campusmoments.Adapter.NotificationAdapter;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Notification;
import com.android.campusmoments.Service.Services;
import com.android.campusmoments.Service.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;

public class NotificationFragment extends Fragment {
    private static final String TAG = "NotificationFragment";
    int where = 0; // 0: HomeActivity, 1: PersonCenterActivity
    private NotificationAdapter notificationAdapter;
    private final Map<Integer, User> notiUserMap = new HashMap<>();
    private List<Notification> notificationList = new ArrayList<>();
    private List<String> notificationKeyList = new ArrayList<>();

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case GET_USER_SUCCESS:
                    getUserSuccess(msg.obj);
                    break;
                case GET_USER_FAIL:
                    getUserFail();
                    break;
            }
        }
    };

    private void getUserFail() {
        Toast.makeText(getContext(), "获取通知用户失败", Toast.LENGTH_SHORT).show();
    }

    private void getUserSuccess(Object obj) {
        try {
            JSONArray arr = new JSONArray(obj.toString());
            Log.d(TAG, "onResponse: " + arr.length());
            notiUserMap.clear();
            for (int i = 0; i < arr.length(); i++) {
                User user = new User(arr.getJSONObject(i));
                notiUserMap.put(user.id, user);
            }
            notificationAdapter.setNotificationList(notificationList);
            notificationAdapter.setNotiUserMap(notiUserMap);
            notificationAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public NotificationFragment() {
    }
    public NotificationFragment(int where) {
        this.where = where;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: where = " + where);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        if (where == 1) {
            ImageView cancelButton = view.findViewById(R.id.cancel_button);
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().finish();
                }
            });
        }
        RecyclerView notificationRecyclerView = view.findViewById(R.id.recyclerViewNotifications);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(getContext(), notificationList, notiUserMap);
        notificationRecyclerView.setAdapter(notificationAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition(); // 获取滑动的位置
                Notification notification = notificationList.get(position);
                firebaseDatabase.getReference("notifications").child(String.valueOf(Services.mySelf.id)).child(notificationKeyList.get(position)).removeValue();
                refresh();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(notificationRecyclerView);

        refresh();
        return view;
    }


    public void refresh() {
        firebaseDatabase.getReference("notifications").child("6").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    // get all senderId
                    List<Integer> senderIdList = new ArrayList<>();
                    notificationList.clear();
                    notificationKeyList.clear();
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        Notification notification = snapshot.getValue(Notification.class);
                        notificationList.add(notification);
                        // only get the key
                        notificationKeyList.add(snapshot.getKey());
                        assert notification != null;
                        senderIdList.add(notification.getSenderId());
                    }
                    List<Notification> tmp = new ArrayList<>();
                    for (int i = notificationList.size() - 1; i >= 0; i--) {
                        tmp.add(notificationList.get(i));
                    }
                    notificationList = tmp;
                    List<String> tmpKey = new ArrayList<>();
                    for (int i = notificationKeyList.size() - 1; i >= 0; i--) {
                        tmpKey.add(notificationKeyList.get(i));
                    }
                    notificationKeyList = tmpKey;
                    Log.d(TAG, "onComplete: " + notificationKeyList.toString());
                    Services.getUserByIds(senderIdList, handler);
                }
            }
        });
    }
}