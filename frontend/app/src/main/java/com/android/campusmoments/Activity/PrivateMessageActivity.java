package com.android.campusmoments.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.campusmoments.Adapter.MessageHolder;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.PrivateMessage;
import com.android.campusmoments.Service.Services;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PrivateMessageActivity extends AppCompatActivity {
    private int selfId = Services.mySelf.id;
    private int otherId;
    private String otherUsername;
    private String otherAvatar;
    private TextView usernameTextView;
    private FirebaseRecyclerAdapter<PrivateMessage, MessageHolder> privateMessageAdapter;
    private RecyclerView privateMessageRecyclerView;
    private final String FIREBASE_DATABASE_URL = "https://campus-moments-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        usernameTextView = findViewById(R.id.username_private_msg);
        otherUsername = getIntent().getStringExtra("username");
        usernameTextView.setText(otherUsername);
        otherId = getIntent().getIntExtra("id", 0);
        otherAvatar = getIntent().getStringExtra("avatar");
        buildPrivateMessageAdapter();
        privateMessageRecyclerView = findViewById(R.id.recyclerViewMessages);
        // once privateMessageRecyclerView's height changes, scroll to the bottom
        privateMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        privateMessageRecyclerView.setAdapter(privateMessageAdapter);
    }

    public void send(View view) {
        TextView messageTextView = findViewById(R.id.editTextMessage);
        String content = messageTextView.getText().toString();
        if (!content.equals("")) {
            PrivateMessage privateMessage = new PrivateMessage(Services.mySelf.id, otherId, Services.mySelf.avatar, Services.mySelf.username, content);
            String childName = selfId < otherId ? selfId + "-" + otherId : otherId + "-" + selfId;
            firebaseDatabase.getReference("privateMessages").child(childName).push().setValue(privateMessage);
        }
        messageTextView.setText("");
    }

    private void buildPrivateMessageAdapter() {
        Query query = firebaseDatabase.getReference("privateMessages").
                child(selfId < otherId ? selfId + "-" + otherId : otherId + "-" + selfId);
        FirebaseRecyclerOptions<PrivateMessage> options = new FirebaseRecyclerOptions.Builder<PrivateMessage>()
                .setQuery(query, PrivateMessage.class)
                .build();
        privateMessageAdapter = new FirebaseRecyclerAdapter<PrivateMessage, MessageHolder>(options) {
            @NonNull
            @Override
            public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_message_item, parent, false);
                return new MessageHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull PrivateMessage model) {
                holder.timeTextView.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",
                        model.getTime()));

                holder.contentTextView.setText(model.getContent());
                Context context = holder.contentTextView.getContext();
                if (model.getSenderId() == selfId) {
                    holder.contentTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.myMessageBackground));
                    holder.contentTextView.setTextColor(ContextCompat.getColor(context, R.color.myMessageText));
                    holder.messageLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    holder.messageLinearLayout.removeView(holder.cardView);
                    holder.messageLinearLayout.addView(holder.cardView);
                    holder.textLinearLayout.setGravity(Gravity.END);
                    holder.usernameTextView.setText(Services.mySelf.username);
                    if (Services.mySelf.avatar == null || Services.mySelf.avatar.equals("")) {
                        holder.avatarImageView.setImageResource(R.drawable.avatar_1);
                    } else {
                        Picasso.get().load(Uri.parse(Services.mySelf.avatar)).into(holder.avatarImageView);
                    }
                } else {
                    holder.contentTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.otherPersonMessageBackground));
                    holder.contentTextView.setTextColor(ContextCompat.getColor(context, R.color.otherPersonMessageText));
                    holder.textLinearLayout.setGravity(Gravity.START);
                    holder.usernameTextView.setText(otherUsername);
                    if (otherAvatar == null || otherAvatar.equals("")) {
                        holder.avatarImageView.setImageResource(R.drawable.avatar_1);
                    } else {
                        Picasso.get().load(Uri.parse(otherAvatar)).into(holder.avatarImageView);
                    }
                }
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                // once privateMessageRecyclerView's height changes, scroll to the bottom
                privateMessageRecyclerView.scrollToPosition(privateMessageAdapter.getItemCount() - 1);
            }

        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        privateMessageAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("stop");
        privateMessageAdapter.stopListening();
    }
    public void cancel(View view) {
        finish();
    }
}