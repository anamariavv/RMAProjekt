package com.example.rmaprojekt;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.Patterns;
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
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public RegisterFragment() {
        // Required empty public constructor
    }

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

        EditText nameView = view.findViewById(R.id.registerName);
        EditText lastNameView = view.findViewById(R.id.registerLastname);
        EditText usernameView = view.findViewById(R.id.registerUsername);
        EditText emailView = view.findViewById(R.id.registerEmailAddress);
        EditText passwordView = view.findViewById(R.id.registerPassword);
        EditText confirmPassView = view.findViewById(R.id.registerConfirmPassword);

        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(view12 -> {
            String name = nameView.getText().toString().trim();
            String lastname = lastNameView.getText().toString().trim();
            String username = usernameView.getText().toString().trim();
            String email = emailView.getText().toString().trim();
            String password = passwordView.getText().toString().trim();
            String passwordConfirmed = confirmPassView.getText().toString().trim();

            if(validate(name, lastname, username, email, password, passwordConfirmed)) {
                Map<String, String> registerInfo = new HashMap<>();
                registerInfo.put("source", "register");
                registerInfo.put("name", name);
                registerInfo.put("lastname", lastname);
                registerInfo.put("username", username);
                registerInfo.put("email", email);
                registerInfo.put("password", password);
                JSONObject requestBody = SingletonRequestSender.createJsonRequest(registerInfo);

                SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.database_request_url),new SingletonRequestSender.RequestResult() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.d("Done", result.toString());
                        //TODO check responses
                        Intent mainActivity = new Intent(getContext(), MainActivity.class);
                        startActivity(mainActivity);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        //TODO display error
                        Log.d("Done", "Register failure "+ error.getMessage());
                    }
                });
            }
        });
    }

    private boolean validate(String name, String lastname, String username, String email, String password, String passwordConfirmed) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
        Toast toast;

        if(name.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            toast = Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast = Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if(!passwordPattern.matcher(password).matches()) {
            toast = Toast.makeText(getContext(),
                    "Password must be atleast 8 characters long and contain atleast one number and special character", Toast.LENGTH_LONG);
            toast.show();
            return false;
        } else if(!passwordConfirmed.equals(password)) {
            toast = Toast.makeText(getContext(), "Passwords must match", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;
    }
}