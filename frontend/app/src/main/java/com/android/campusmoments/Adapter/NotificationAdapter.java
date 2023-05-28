package com.android.campusmoments.Adapter;

import static com.android.campusmoments.Service.Config.firebaseDatabase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.campusmoments.Activity.DetailedActivity;
import com.android.campusmoments.Activity.PrivateMessageActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.NotificationMessage;
import com.android.campusmoments.Service.Services;
import com.android.campusmoments.Service.User;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    private Context context;
    private List<NotificationMessage> notificationMessageList = new ArrayList<>();
    private static Map<Integer, User> notiUserMap = new HashMap<>();
    private List<String> notificationKeyList = new ArrayList<>();

    public NotificationAdapter(Context context, List<NotificationMessage> notificationMessageList, Map<Integer, User> notiUserMap) {
        this.context = context;
        this.notificationMessageList = notificationMessageList;
        this.notiUserMap = notiUserMap;
    }

    public void setNotificationList(List<NotificationMessage> notificationMessageList) {
        this.notificationMessageList = notificationMessageList;
    }
    public void setNotificationKeyList(List<String> notificationKeyList) {
        this.notificationKeyList = notificationKeyList;
    }
    public void setNotiUserMap(Map<Integer, User> notiUserMap) {
        this.notiUserMap = notiUserMap;
    }
    public static class NotificationHolder extends RecyclerView.ViewHolder {
        public ImageView avatarImageView;
        public TextView timeTextView;
        public TextView usernameTextView;
        public TextView contentTextView;
        public ImageView isReadImageView;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView_noti);
            timeTextView = itemView.findViewById(R.id.time_noti);
            usernameTextView = itemView.findViewById(R.id.usernameTextView_noti);
            contentTextView = itemView.findViewById(R.id.content_noti);
            isReadImageView = itemView.findViewById(R.id.isread_noti);
        }

        public void bindData(NotificationMessage notificationMessage, int position, Map<Integer, User> notiUserMap) {
            timeTextView.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",
                    notificationMessage.getTime()));
            contentTextView.setText(notificationMessage.getContent());
            Log.d("NotificationAdapter", "bindData: " + notificationMessage.getSenderId());
            Log.d("NotificationAdapter", "bindData: " + notiUserMap);
            usernameTextView.setText(Objects.requireNonNull(notiUserMap.get(notificationMessage.getSenderId())).username);
            String avatar = Objects.requireNonNull(notiUserMap.get(notificationMessage.getSenderId())).avatar;
            if (avatar == null || avatar.equals("")) {
                avatarImageView.setImageResource(R.drawable.avatar_1);
            } else {
                Picasso.get().load(Uri.parse(avatar)).into(avatarImageView);
            }
            if (notificationMessage.getIsRead()) {
                isReadImageView.setVisibility(View.INVISIBLE);
            } else {
                isReadImageView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public NotificationHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        NotificationHolder holder = new NotificationHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get notificationMessage
                int position = holder.getAdapterPosition();
                NotificationMessage notificationMessage = notificationMessageList.get(position);
                int senderId = notificationMessage.getSenderId();
                if (notificationMessage.getType() == 3) {
                    String username = Objects.requireNonNull(notiUserMap.get(senderId)).username;
                    String avatar = Objects.requireNonNull(notiUserMap.get(senderId)).avatar;
                    Intent intent = new Intent(context, PrivateMessageActivity.class);
                    intent.putExtra("id", senderId);
                    intent.putExtra("username", username);
                    intent.putExtra("avatar", avatar);
                    notificationMessage.setIsRead(true);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, DetailedActivity.class);
                    intent.putExtra("id", notificationMessage.getMomentId());
                    notificationMessage.setIsRead(true);
                    context.startActivity(intent);
                }
                firebaseDatabase.getReference("notifications").child(String.valueOf(Services.mySelf.id)).child(notificationKeyList.get(position)).child("isRead").setValue(true);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        NotificationMessage notificationMessage = notificationMessageList.get(position);
        holder.bindData(notificationMessage, position, notiUserMap);
    }

    @Override
    public int getItemCount() {
        return notificationMessageList.size();
    }
}
