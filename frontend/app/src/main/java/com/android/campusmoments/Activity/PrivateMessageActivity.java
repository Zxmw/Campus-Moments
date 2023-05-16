package com.android.campusmoments.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.campusmoments.Adapter.PrivateMessageAdapter;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.PrivateMessage;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PrivateMessageActivity extends AppCompatActivity {
    private List<PrivateMessage> privateMessageList;
    private TextView usernameTextView;
    private PrivateMessageAdapter privateMessageAdapter;
    private RecyclerView privateMessageRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        usernameTextView = findViewById(R.id.username_private_msg);
        usernameTextView.setText(getIntent().getStringExtra("username"));
        privateMessageList = new ArrayList<>();
        privateMessageAdapter = new PrivateMessageAdapter(privateMessageList, this);
        privateMessageRecyclerView = findViewById(R.id.recyclerViewMessages);
        // once privateMessageRecyclerView's height changes, scroll to the bottom
        privateMessageRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> privateMessageRecyclerView.scrollToPosition(privateMessageList.size() - 1));
        privateMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        privateMessageRecyclerView.setAdapter(privateMessageAdapter);

        // fake data
        PrivateMessage message1 = new PrivateMessage(1, "android.resource://" + getPackageName() + "/" + R.drawable.avatar_1, "10:00 AM", "User 1", "Hello", true);
        PrivateMessage message2 = new PrivateMessage(2, "android.resource://" + getPackageName() + "/" + R.drawable.avatar_default, "10:05 AM", "User 2", "Hi", false);
        PrivateMessage message3 = new PrivateMessage(3, "android.resource://" + getPackageName() + "/" + R.drawable.avatar_1, "10:10 AM", "User 1", "How are you?", true);
        PrivateMessage message4 = new PrivateMessage(4, "android.resource://" + getPackageName() + "/" + R.drawable.avatar_default, "10:15 AM", "User 2", "I'm fine, thank you.", false);
        privateMessageList.add(message1);
        privateMessageList.add(message2);
        privateMessageList.add(message3);
        privateMessageList.add(message4);
        privateMessageAdapter.notifyDataSetChanged();
    }

    public void cancel(View view) {
        finish();
    }
}