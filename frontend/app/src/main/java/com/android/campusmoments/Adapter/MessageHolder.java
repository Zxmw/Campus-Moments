package com.android.campusmoments.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.campusmoments.R;

public class MessageHolder extends RecyclerView.ViewHolder {
    public LinearLayout messageLinearLayout;
    public LinearLayout textLinearLayout;
    public ImageView avatarImageView;
    public TextView timeTextView;
    public TextView usernameTextView;
    public TextView contentTextView;
    public CardView cardView;
    Space space;
    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        messageLinearLayout = itemView.findViewById(R.id.messageLinearLayout);
        textLinearLayout = itemView.findViewById(R.id.textLinearLayout);
        avatarImageView = itemView.findViewById(R.id.avatarImageView_private_message_item);
        cardView = itemView.findViewById(R.id.cardView_private_message_item);
        timeTextView = itemView.findViewById(R.id.timeTextView);
        usernameTextView = itemView.findViewById(R.id.usernameTextView);
        contentTextView = itemView.findViewById(R.id.contentTextView);
        space = itemView.findViewById(R.id.space);
    }
}
