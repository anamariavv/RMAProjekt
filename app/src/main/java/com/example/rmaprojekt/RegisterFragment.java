package com.example.rmaprojekt;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RegisterFragment.
     */
    public static RegisterFragment newInstance(String param1) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nameView = view.findViewById(R.id.registerName);
        TextView lastNameView = view.findViewById(R.id.registerLastname);
        TextView usernameView = view.findViewById(R.id.registerUsername);
        TextView emailView = view.findViewById(R.id.registerEmailAddress);
        TextView passwordView = view.findViewById(R.id.registerPassword);

        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO form validation
                Map<String, String> registerInfo = new HashMap<>();
                registerInfo.put("name", nameView.getText().toString());
                registerInfo.put("lastname", lastNameView.getText().toString());
                registerInfo.put("username", usernameView.getText().toString());
                registerInfo.put("email", emailView.getText().toString());
                registerInfo.put("password", passwordView.getText().toString());

                JSONObject requestBody = new MyRequest(registerInfo).createJsonRequest();

                SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.database_request_url),new SingletonRequestSender.RequestResult() {
                    @Override
                    public JSONObject onSuccess(JSONObject result) {
                        Log.d("Done", "Register success");

                        Intent mainActivity = new Intent(getContext(), MainActivity.class);
                        startActivity(mainActivity);

                        return result;
                    }
                    @Override
                    public VolleyError onError(VolleyError error) {
                        //TODO display error
                        Log.d("Done", "Register failure");

                        return error;
                    }
                });

            }
        });
    }
}