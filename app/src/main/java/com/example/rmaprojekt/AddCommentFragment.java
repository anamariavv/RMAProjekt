package com.example.rmaprojekt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCommentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedViewModel sharedViewModel;
    private String mParam1;
    private String mParam2;

    public AddCommentFragment() {}

    public static AddCommentFragment newInstance(String param1, String param2) {
        AddCommentFragment fragment = new AddCommentFragment();
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
        return inflater.inflate(R.layout.fragment_add_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RMA", Context.MODE_PRIVATE);

        TextView idView = view.findViewById(R.id.forum_topic_id_add_comment);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getInfObject().observe(getViewLifecycleOwner(), jsonObject -> {
            try {
                Log.d("datazz", jsonObject.toString());
                idView.setText(jsonObject.getString("forum_topic_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        EditText commentText = view.findViewById(R.id.add_comment_text);

        Button postCommentButton = view.findViewById(R.id.post_comment_button);
        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentTextString = commentText.getText().toString().trim();

                if(!commentTextString.isEmpty()) {
                    Map<String, String> commentInfo = new HashMap<String, String>();
                    commentInfo.put("forum_topic_id", idView.getText().toString().trim());
                    commentInfo.put("source", "new comment");
                    commentInfo.put("text", commentTextString);
                    commentInfo.put("author", sharedPreferences.getString("username", "Username"));

                    JSONObject requestBody = SingletonRequestSender.createJsonRequest(commentInfo);
                    SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.forum_request_url), new SingletonRequestSender.RequestResult() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            Toast toast = Toast.makeText(getContext(), "Comment posted", Toast.LENGTH_LONG);
                            toast.show();

                            FragmentManager fragmentManager = getParentFragmentManager();
                            fragmentManager.popBackStack();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Toast toast = Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(getContext(), "Comment can't be empty", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });


    }
}