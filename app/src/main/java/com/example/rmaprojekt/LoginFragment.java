package com.example.rmaprojekt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

public class LoginFragment extends Fragment implements IValidate {

    private static final String ARG_PARAM1 = "param1";

    private String name;

    public LoginFragment() {}

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
                if (savedInstanceState == null) {
                    parentFragmentManager.beginTransaction().replace(R.id.fragmentFrame, RegisterFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });


        Button loginButton = view.findViewById(R.id.loginButton);
        EditText usernameView = view.findViewById(R.id.loginUsername);
        EditText passwordVIew = view.findViewById(R.id.loginPassword);

        loginButton.setOnClickListener(view1 -> {
            String username = usernameView.getText().toString().trim();
            String password = passwordVIew.getText().toString().trim();

            Map<String, String> loginInfo = new HashMap<>();
            loginInfo.put("source", "login");
            loginInfo.put("username", username);
            loginInfo.put("password", password);

             if(validateFormInfo(loginInfo)) {
                JSONObject requestBody = SingletonRequestSender.createJsonRequest(loginInfo);

                SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.database_request_url), new SingletonRequestSender.RequestResult() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            if(result.get("response").equals("200")) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RMA", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(getString(R.string.pref_username_key), username);
                                editor.putString("password", password);
                                editor.apply();

                                Intent mainActivity = new Intent(getContext(), MainActivity.class);
                                startActivity(mainActivity);
                            } else if(result.get("response").equals("WRONG_PASSWORD")) {
                                Toast toast = Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(getContext(), "User doesn't exist", Toast.LENGTH_LONG);
                                toast.show();
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
            }
        });
    }

    @Override
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