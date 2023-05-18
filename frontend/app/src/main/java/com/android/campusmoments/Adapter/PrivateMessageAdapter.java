package com.android.campusmoments.Adapter;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.PrivateMessage;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class PrivateMessageAdapter extends RecyclerView.Adapter<PrivateMessageAdapter.MessageViewHolder> {
    private List<PrivateMessage> messageList;
    private Context context;

    public PrivateMessageAdapter(List<PrivateMessage> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        PrivateMessage message = messageList.get(position);
//        Picasso.get().load(Uri.parse(message.avatar)).into(holder.avatarImageView);
        holder.avatarImageView.setImageResource(R.drawable.avatar_1);
        holder.timeTextView.setText(message.time);
        holder.usernameTextView.setText(message.username);
        holder.contentTextView.setText(message.content);

        if (message.isSentByMe) {
            // Apply styling for messages sent by the user
            holder.contentTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.myMessageBackground));
            holder.contentTextView.setTextColor(ContextCompat.getColor(context, R.color.myMessageText));
            // Align text to the right or change any other styles as needed
            holder.messageLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            holder.messageLinearLayout.removeView(holder.avatarImageView);
            holder.messageLinearLayout.addView(holder.avatarImageView);
            holder.textLinearLayout.setGravity(Gravity.END);

        } else {
            // Apply styling for messages sent by the other person
            holder.contentTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.otherPersonMessageBackground));
            holder.contentTextView.setTextColor(ContextCompat.getColor(context, R.color.otherPersonMessageText));
            // Align text to the left or change any other styles as needed
            holder.textLinearLayout.setGravity(Gravity.START);
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout messageLinearLayout;
        LinearLayout textLinearLayout;
        ImageView avatarImageView;
        TextView timeTextView;
        TextView usernameTextView;
        TextView contentTextView;
        Space space;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageLinearLayout = itemView.findViewById(R.id.messageLinearLayout);
            textLinearLayout = itemView.findViewById(R.id.textLinearLayout);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            space = itemView.findViewById(R.id.space);
        }
    }
}
