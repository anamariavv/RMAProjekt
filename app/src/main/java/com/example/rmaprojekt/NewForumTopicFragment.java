package com.example.rmaprojekt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.VolleyError;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class NewForumTopicFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NewForumTopicFragment() {}

    public static NewForumTopicFragment newInstance(String param1, String param2) {
        NewForumTopicFragment fragment = new NewForumTopicFragment();
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
        return inflater.inflate(R.layout.fragment_new_forum_topic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewForumTopicFragment thisFragment = this;

        EditText topicTitle = view.findViewById(R.id.new_forum_topic_title);
        EditText topicText = view.findViewById(R.id.new_forum_topic_text);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RMA", Context.MODE_PRIVATE);

        Button postTopicButton = view.findViewById(R.id.new_forum_topic_button);
        postTopicButton.setOnClickListener(view1 -> {
            Map<String, String> topicInfo = new HashMap<>();
            topicInfo.put("source", "new post");
            topicInfo.put("title", topicTitle.getText().toString().trim());
            topicInfo.put("text", topicText.getText().toString().trim());
            topicInfo.put("author", sharedPreferences.getString("username", "Username"));

            if(validateFormInfo(topicInfo)) {
                JSONObject requestBody = SingletonRequestSender.createJsonRequest(topicInfo);

                SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.forum_request_url), new SingletonRequestSender.RequestResult() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Toast toast = Toast.makeText(getContext(), "Posted successfully", Toast.LENGTH_LONG);
                        toast.show();

                        if (savedInstanceState == null) {
                            getParentFragmentManager().beginTransaction().replace(R.id.forum_fragment_container, AllForumsFragment.class, null)
                                    .setReorderingAllowed(true)
                                    .commit();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Toast toast = Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });
    }

    public boolean validateFormInfo(Map<String, String> newInformation) {
        Toast toast;
        boolean empty = false;

        for(Map.Entry<String, String> entry : newInformation.entrySet()) {
            if(entry.getValue().isEmpty()) {
                empty = true;
            }
        }

        if(empty) {
            toast = Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;
    }
}