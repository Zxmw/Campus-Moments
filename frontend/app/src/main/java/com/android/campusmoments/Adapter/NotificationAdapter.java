package com.android.campusmoments.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.campusmoments.Activity.DetailedActivity;
import com.android.campusmoments.Activity.PrivateMessageActivity;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Notification;
import com.android.campusmoments.Service.User;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    private Context context;
    private List<Notification> notificationList;
    private Map<Integer, User> notiUserMap;

    public NotificationAdapter(Context context, List<Notification> notificationList, Map<Integer, User> notiUserMap) {
        this.context = context;
        this.notificationList = notificationList;
        this.notiUserMap = notiUserMap;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public void setNotiUserMap(Map<Integer, User> notiUserMap) {
        this.notiUserMap = notiUserMap;
    }
    public static class NotificationHolder extends RecyclerView.ViewHolder {
        public ImageView avatarImageView;
        public TextView timeTextView;
        public TextView usernameTextView;
        public TextView contentTextView;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView_noti);
            timeTextView = itemView.findViewById(R.id.time_noti);
            usernameTextView = itemView.findViewById(R.id.usernameTextView_noti);
            contentTextView = itemView.findViewById(R.id.content_noti);
        }

        public void bindData(Notification notification, int position, Map<Integer, User> notiUserMap) {
            timeTextView.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",
                    notification.getTime()));
            contentTextView.setText(notification.getContent());
            usernameTextView.setText(Objects.requireNonNull(notiUserMap.get(notification.getSenderId())).username);
            String avatar = Objects.requireNonNull(notiUserMap.get(notification.getSenderId())).avatar;
            if (avatar == null || avatar.equals("")) {
                avatarImageView.setImageResource(R.drawable.avatar_1);
            } else {
                Picasso.get().load(Uri.parse(avatar)).into(avatarImageView);
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
                //get notification
                int position = holder.getAdapterPosition();
                Notification notification = notificationList.get(position);
                int senderId = notification.getSenderId();
                if (notification.getType() == 3) {
                    // goto privateMessageActivity

                    String username = Objects.requireNonNull(notiUserMap.get(senderId)).username;
                    String avatar = Objects.requireNonNull(notiUserMap.get(senderId)).avatar;
                    Intent intent = new Intent(context, PrivateMessageActivity.class);
                    intent.putExtra("id", senderId);
                    intent.putExtra("username", username);
                    intent.putExtra("avatar", avatar);
                    context.startActivity(intent);
                }
                else {
                    // goto momentDetailActivity
                    Intent intent = new Intent(context, DetailedActivity.class);
                    intent.putExtra("id", notification.getMomentId());
                    context.startActivity(intent);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.bindData(notification, position, notiUserMap);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
