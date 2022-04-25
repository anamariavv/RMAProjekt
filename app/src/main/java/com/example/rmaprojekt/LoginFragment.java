package com.example.rmaprojekt;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String name;

    public LoginFragment() {
        // Required empty public constructor
    }
    public static LoginFragment newInstance(String param1) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView registerLink = view.findViewById(R.id.registerLink);
        FragmentManager parentFragmentManager = getActivity().getSupportFragmentManager();

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.beginTransaction().replace(R.id.fragmentFrame, new RegisterFragment())
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button loginButton = view.findViewById(R.id.loginButton);
        EditText usernameView = view.findViewById(R.id.loginUsername);
        EditText passwordVIew = view.findViewById(R.id.loginPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameView.getText().toString().trim();
                String password = passwordVIew.getText().toString().trim();

                if(username.isEmpty() || password.isEmpty()) {
                    Toast toast = Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Map<String, String> loginInfo = new HashMap<>();
                    loginInfo.put("source", "login");
                    loginInfo.put("username", username);
                    loginInfo.put("password", password);
                    JSONObject requestBody = SingletonRequestSender.createJsonRequest(loginInfo);

                    SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.database_request_url), new SingletonRequestSender.RequestResult() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            Log.d("Login success", result.toString());
                            //TODO check responses
                            Intent mainActivity = new Intent(getContext(), MainActivity.class);
                            startActivity(mainActivity);
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.d("Login error: ", error.toString());
                        }
                    });
                }
            }
        });
    }
}