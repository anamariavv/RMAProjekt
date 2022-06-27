package com.example.rmaprojekt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

/**
 * Displays all possible forums (forum topics). Click on a certain forum to view
 * all its comments and interact with it
 */

public class AllForumsFragment extends Fragment {
    private SharedViewModel viewModel;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AllForumsFragment() {}

    public static AllForumsFragment newInstance(String param1, String param2) {
        AllForumsFragment fragment = new AllForumsFragment();
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
        return inflater.inflate(R.layout.fragment_all_forums, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final float TITLE_TEXT_SIZE = 22;
        final float TEXT_SIZE = 17;

        super.onViewCreated(view, savedInstanceState);

        Map<String, String> forumInfo = new HashMap<>();
        forumInfo.put("source", "display");

        ScrollView forumScrollview = view.findViewById(R.id.all_forums_scrollview);
        LinearLayout scrollChild = new LinearLayout(getContext());
        scrollChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollChild.setOrientation(LinearLayout.VERTICAL);
        scrollChild.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        scrollChild.setDividerDrawable(getResources().getDrawable(R.drawable.empty_divider));

        JSONObject requestBody = SingletonRequestSender.createJsonRequest(forumInfo);
        SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.forum_request_url), new SingletonRequestSender.RequestResult() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray allForums = result.getJSONArray("rows");
                    for(int i = 0; i < allForums.length(); i++) {
                        JSONObject currentObject = allForums.getJSONObject(i);

                        RelativeLayout forumLayout = new RelativeLayout(getContext());
                        TextView title = new TextView(getContext());
                        TextView text = new TextView(getContext());
                        TextView author= new TextView(getContext());
                        TextView id = new TextView(getContext());
                        TextView published = new TextView(getContext());

                        forumLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        forumLayout.setBackground(getResources().getDrawable(R.drawable.grey_rectangle));
                        forumLayout.setPadding(10,20,0,20);
                        forumLayout.setClickable(true);

                        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                        forumLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewModel.setInfoObject(currentObject);

                                if (savedInstanceState == null) {
                                    getParentFragmentManager().beginTransaction().replace(R.id.forum_fragment_container, SingleForumTopicFragment.class, null)
                                            .setReorderingAllowed(true)
                                            .addToBackStack(null)
                                            .commit();
                                }

                            }
                        });

                        RelativeLayout.LayoutParams idParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        id.setLayoutParams(idParams);
                        id.setId(View.generateViewId());
                        id.setText(currentObject.getString("id"));
                        id.setVisibility(View.GONE);

                        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        titleParams.addRule(RelativeLayout.BELOW, id.getId());
                        title.setLayoutParams(titleParams);
                        title.setId(View.generateViewId());
                        title.setTextSize(TITLE_TEXT_SIZE);
                        title.setText(currentObject.getString("title"));

                        RelativeLayout.LayoutParams publishedParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        publishedParams.addRule(RelativeLayout.BELOW, id.getId());
                        publishedParams.addRule(RelativeLayout.END_OF, title.getId());
                        publishedParams.addRule(RelativeLayout.ALIGN_BASELINE, title.getId());
                        publishedParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                        published.setLayoutParams(publishedParams);
                        published.setId(View.generateViewId());
                        published.setPadding(0,0,10,0);
                        published.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                        published.setText(currentObject.getString("publish_date"));

                        RelativeLayout.LayoutParams authorParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        authorParams.addRule(RelativeLayout.BELOW, title.getId());
                        author.setPadding(0,0,0,15);
                        author.setLayoutParams(authorParams);
                        author.setId(View.generateViewId());
                        author.setText(currentObject.getString("author"));

                        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        textParams.addRule(RelativeLayout.BELOW, author.getId());
                        text.setLayoutParams(textParams);
                        text.setPadding(0,0,0,50);
                        text.setMaxLines(5);
                        text.setTextSize(TEXT_SIZE);
                        text.setEllipsize(TextUtils.TruncateAt.END);
                        text.setId(View.generateViewId());
                        text.setText(currentObject.getString("text"));

                        LinearLayout interactionLayout = new LinearLayout(getContext());
                        interactionLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        interactionLayout.setOrientation(LinearLayout.HORIZONTAL);
                        RelativeLayout.LayoutParams interactionLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        interactionLayoutParams.addRule(RelativeLayout.BELOW, text.getId());
                        interactionLayout.setLayoutParams(interactionLayoutParams);

                        ImageView likeView = new ImageView(getContext());
                        likeView.setImageResource(R.drawable.ic_like);
                        likeView.setPadding(10,0,5,0);
                        likeView.setId(View.generateViewId());

                        TextView likeCount = new TextView(getContext());
                        likeCount.setText(currentObject.getString("likes"));
                        likeCount.setPadding(0,0,30,0);
                        likeCount.setId(View.generateViewId());

                        ImageView dislikeView = new ImageView(getContext());
                        dislikeView.setImageResource(R.drawable.ic_dislike);
                        dislikeView.setPadding(10,0,5,0);
                        dislikeView.setId(View.generateViewId());

                        TextView dislikeCount = new TextView(getContext());
                        dislikeCount.setText(currentObject.getString("dislikes"));
                        dislikeCount.setId(View.generateViewId());

                        interactionLayout.addView(likeView);
                        interactionLayout.addView(likeCount);
                        interactionLayout.addView(dislikeView);
                        interactionLayout.addView(dislikeCount);

                        forumLayout.addView(title);
                        forumLayout.addView(author);
                        forumLayout.addView(id);
                        forumLayout.addView(published);
                        forumLayout.addView(text);
                        forumLayout.addView(interactionLayout);
                        scrollChild.addView(forumLayout);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast toast = Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        forumScrollview.addView(scrollChild);

        FloatingActionButton fab = view.findViewById(R.id.forum_topics_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                NewForumTopicFragment newForumTopicFragment = new NewForumTopicFragment();

                fragmentTransaction.replace(R.id.forum_fragment_container, newForumTopicFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

}