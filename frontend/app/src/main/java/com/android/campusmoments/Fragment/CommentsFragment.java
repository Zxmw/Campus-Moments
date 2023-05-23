package com.android.campusmoments.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.campusmoments.Adapter.CommentAdapter;
import com.android.campusmoments.R;
import com.android.campusmoments.Service.Comment;
import com.android.campusmoments.Service.Services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentsFragment extends Fragment {

    private static final String TAG = "CommentsFragment";
    private List<Integer> commentIdList;
    private List<Comment> mCommentList;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private final Handler getCommentsHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                try {
                    JSONObject obj = new JSONObject(msg.obj.toString());
                    Comment comment = new Comment(obj);
                    Log.d(TAG, "handleMessage: " + comment.getId() + " " + comment.getUsername() + " " + comment.getContent() + " " + comment.getTime());
                    mCommentList.add(comment);
                    if (mCommentList.size() == commentIdList.size()) {
                        // 按照 评论时间（id） 排序
                        Collections.sort(mCommentList);
                        commentAdapter.setComments(mCommentList);
                        commentAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public CommentsFragment(List<Integer> commentIdList) {
        this.commentIdList = commentIdList;
        getComments();
    }
    public CommentsFragment() {
        this.commentIdList = new ArrayList<>();
    }

    public void setCommentIdList(List<Integer> commentIdList) {
        this.commentIdList = commentIdList;
        getComments();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        commentsRecyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        commentsRecyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(mCommentList);
        commentsRecyclerView.setAdapter(commentAdapter);

        return view;
    }
    private void getComments() {
        mCommentList = new ArrayList<>();
        for(int i = 0; i < commentIdList.size(); i++) {
            Services.getCommentById(getCommentsHandler, commentIdList.get(i));
        }
    }
}