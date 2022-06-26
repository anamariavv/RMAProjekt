package com.example.rmaprojekt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Displays a single forum and all its comments

public class SingleForumTopicFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private androidx.appcompat.app.AlertDialog.Builder dialogBuilder;
    private View singleTopicView;
    private SharedPreferences sharedPreferences;
    private TextView id;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> comments;

    private String mParam1;
    private String mParam2;

    public SingleForumTopicFragment() {
    }

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
        View view = inflater.inflate(R.layout.fragment_single_forum_topic, container, false);

        commentRecyclerView = view.findViewById(R.id.comment_section_recycler_view);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        comments = new ArrayList<>();
        commentRecyclerView.setAdapter(new CommentAdapter(getContext(), comments));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        singleTopicView = view;
        sharedPreferences = getActivity().getSharedPreferences("RMA", Context.MODE_PRIVATE);

        TextView title = view.findViewById(R.id.forum_topic_title);
        id = view.findViewById(R.id.forum_topic_id);
        TextView text = view.findViewById(R.id.forum_topic_text);
        TextView author = view.findViewById(R.id.forum_topic_author);
        TextView published = view.findViewById(R.id.forum_topic_published);
        TextView likeCount = view.findViewById(R.id.forum_topic_like_count);
        TextView dislikeCount = view.findViewById(R.id.forum_topic_dislike_count);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getInfObject().observe(getViewLifecycleOwner(), jsonObject -> {
            try {
                id.setText(jsonObject.get("id").toString());
                title.setText(jsonObject.get("title").toString());
                text.setText(jsonObject.get("text").toString());
                author.setText(jsonObject.get("author").toString());
                published.setText(jsonObject.get("publish_date").toString());
                likeCount.setText(jsonObject.get("likes").toString());
                dislikeCount.setText(jsonObject.get("dislikes").toString());

                displayComments();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        FloatingActionButton addCommentActionButton = view.findViewById(R.id.add_comment_action_button);
        addCommentActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savedInstanceState == null) {
                    getParentFragmentManager().beginTransaction().replace(R.id.forum_fragment_container, AddCommentFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }


    public void displayComments() {
        Map<String, String> allComments = new HashMap<>();
        allComments.put("source", "display comments");
        allComments.put("forum_topic_id", id.getText().toString().trim());

        JSONObject requestBody = SingletonRequestSender.createJsonRequest(allComments);
        SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.forum_request_url), new SingletonRequestSender.RequestResult() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray allCommentsArray = result.getJSONArray("rows");

                    for(int i = 0; i < allCommentsArray.length(); i++) {
                        JSONObject commentObject = allCommentsArray.getJSONObject(i);
                        Comment comment = createComment(commentObject);
                        comments.add(comment);
                    }

                    commentAdapter = new CommentAdapter(getContext(), comments);
                    commentRecyclerView.setAdapter(commentAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {
                Toast toast = Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private Comment createComment(JSONObject commentObject) throws JSONException {
        String text = commentObject.getString("text");
        String likeCount = commentObject.getString("likes");
        String dislikeCount = commentObject.getString("dislikes");
        String author = commentObject.getString("author");
        String published = commentObject.getString("publish_date");

        return new Comment(text, likeCount, dislikeCount, author, published);
    }

}