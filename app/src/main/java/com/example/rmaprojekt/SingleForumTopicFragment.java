package com.example.rmaprojekt;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
        return inflater.inflate(R.layout.fragment_single_forum_topic, container, false);
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

        ScrollView commentSection = singleTopicView.findViewById(R.id.comment_section);
        LinearLayout scrollChild = new LinearLayout(getContext());
        scrollChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollChild.setOrientation(LinearLayout.VERTICAL);
        scrollChild.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        scrollChild.setDividerDrawable(getResources().getDrawable(R.drawable.empty_divider));

        JSONObject requestBody = SingletonRequestSender.createJsonRequest(allComments);
        SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.forum_request_url), new SingletonRequestSender.RequestResult() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray allCommentsArray = result.getJSONArray("rows");

                    for(int i = 0; i < allCommentsArray.length(); i++) {
                        JSONObject currentObject = allCommentsArray.getJSONObject(i);

                        RelativeLayout commentLayout = new RelativeLayout(getContext());
                        TextView text = new TextView(getContext());
                        TextView id = new TextView(getContext());
                        TextView author = new TextView(getContext());
                        TextView published = new TextView(getContext());

                        commentLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        RelativeLayout.LayoutParams idParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        id.setLayoutParams(idParams);
                        id.setId(View.generateViewId());
                        id.setText(currentObject.getString("id"));
                        id.setVisibility(View.GONE);

                        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        textParams.addRule(RelativeLayout.BELOW, id.getId());
                        text.setLayoutParams(textParams);
                        text.setPadding(15,0,0,50);
                        text.setMaxLines(5);
                        text.setTextSize(15);
                        text.setEllipsize(TextUtils.TruncateAt.END);
                        text.setId(View.generateViewId());
                        text.setText(currentObject.getString("text"));

                        LinearLayout interactionLayout = new LinearLayout(getContext());
                        interactionLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        interactionLayout.setOrientation(LinearLayout.HORIZONTAL);
                        RelativeLayout.LayoutParams interactionLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        interactionLayoutParams.addRule(RelativeLayout.BELOW, text.getId());
                        interactionLayout.setLayoutParams(interactionLayoutParams);

                        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(90,90);

                        ImageButton likeButton = new ImageButton(getContext());
                        likeButton.setLayoutParams(buttonParams);
                        likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                        likeButton.setId(View.generateViewId());

                        TextView likeCount = new TextView(getContext());
                        likeCount.setText(currentObject.getString("likes"));
                        likeCount.setPadding(0,0,30,0);
                        likeCount.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        likeCount.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        likeCount.setId(View.generateViewId());


                        ImageButton dislikeButton = new ImageButton(getContext());
                        dislikeButton.setLayoutParams(buttonParams);
                        dislikeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_dislike));
                        dislikeButton.setId(View.generateViewId());

                        TextView dislikeCount = new TextView(getContext());
                        dislikeCount.setText(currentObject.getString("dislikes"));
                        dislikeCount.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        dislikeCount.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        dislikeCount.setId(View.generateViewId());

                        LinearLayout.LayoutParams authorParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        authorParams.setMargins(20,0,20,0);
                        author.setLayoutParams(authorParams);
                        author.setId(View.generateViewId());
                        author.setText(currentObject.getString("author"));

                        published.setId(View.generateViewId());
                        published.setPadding(0,0,10,0);
                        published.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                        published.setText(currentObject.getString("publish_date"));

                        interactionLayout.addView(likeButton);
                        interactionLayout.addView(likeCount);
                        interactionLayout.addView(dislikeButton);
                        interactionLayout.addView(dislikeCount);
                        interactionLayout.addView(author);
                        interactionLayout.addView(published);

                        commentLayout.addView(id);
                        commentLayout.addView(text);
                        commentLayout.addView(interactionLayout);
                        scrollChild.addView(commentLayout);
                    }
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
        commentSection.addView(scrollChild);
    }

}