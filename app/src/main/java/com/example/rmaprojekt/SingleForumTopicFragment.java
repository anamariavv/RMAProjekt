package com.example.rmaprojekt;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

//Displays a single forum and all its comments

public class SingleForumTopicFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SingleForumTopicFragment() {}

    public static SingleForumTopicFragment newInstance(String param1, String param2) {
        SingleForumTopicFragment fragment = new SingleForumTopicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_forum_topic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title = view.findViewById(R.id.forum_topic_title);
        TextView text = view.findViewById(R.id.forum_topic_text);
        TextView author = view.findViewById(R.id.forum_topic_author);
        TextView published = view.findViewById(R.id.forum_topic_published);
        TextView likeCount = view.findViewById(R.id.forum_topic_like_count);
        TextView dislikeCount = view.findViewById(R.id.forum_topic_dislike_count);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getInfObject().observe(getViewLifecycleOwner(), new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                try {
                    title.setText(jsonObject.get("title").toString());
                    text.setText(jsonObject.get("text").toString());
                    author.setText(jsonObject.get("author").toString());
                    published.setText(jsonObject.get("publish_date").toString());
                    likeCount.setText(jsonObject.get("likes").toString());
                    dislikeCount.setText(jsonObject.get("dislikes").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Dialog addCommentDialog = new Dialog(getContext());

        FloatingActionButton addCommentButton = view.findViewById(R.id.add_comment_action_button);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addCommentDialog.setContentView(R.layout.add_comment_popup);
               addCommentDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
               addCommentDialog.show();
            }
        });
    }
}