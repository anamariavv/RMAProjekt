package com.example.rmaprojekt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RMA", Context.MODE_PRIVATE);

        class MyTextWatcher implements TextWatcher {
            String inputField;
            Map<String, String> params;
            EditText editText;

            public MyTextWatcher(EditText editText, String inputField, Map<String, String> params) {
                this.editText = editText;
                this.inputField = inputField;
                this.params = params;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String oldValue = sharedPreferences.getString(inputField, "/");
                String newValue = editText.getText().toString().trim();

                if (oldValue.equals(newValue) == false) {
                    params.put(inputField, newValue);
                }
            }
        }

        EditText usernameView = view.findViewById(R.id.profile_username);
        EditText nameView = view.findViewById(R.id.profile_name);
        EditText lastnameView = view.findViewById(R.id.profile_lastname);
        EditText emailView = view.findViewById(R.id.profile_email);
        EditText passwordView = view.findViewById(R.id.profile_password);

        usernameView.setText(sharedPreferences.getString(getString(R.string.pref_username_key), "Username"));

        Map<String, String> newInformation = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("source", "display");
        params.put("username", sharedPreferences.getString("username", "Username"));

        JSONObject requestBody = SingletonRequestSender.createJsonRequest(params);
        SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.profile_request_url), new SingletonRequestSender.RequestResult() {
            @Override
            public void onSuccess(JSONObject result) {
                try {

                    nameView.setText(result.getJSONObject("response").get("name").toString());
                    lastnameView.setText(result.getJSONObject("response").get("lastname").toString());
                    emailView.setText(result.getJSONObject("response").get("email").toString());
                    passwordView.setText(sharedPreferences.getString("password", "Password"));

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.pref_lastname_key), result.getJSONObject("response").get("lastname").toString());
                    editor.putString(getString(R.string.pref_name_key), result.getJSONObject("response").get("name").toString());
                    editor.putString(getString(R.string.pref_lastname_key), result.getJSONObject("response").get("lastname").toString());
                    editor.putString(getString(R.string.pref_email_key), result.getJSONObject("response").get("email").toString());
                    editor.apply();

                    //set the textwatcher listeners after the page form has been initialized
                    usernameView.addTextChangedListener(new MyTextWatcher(usernameView, "username", newInformation));
                    nameView.addTextChangedListener(new MyTextWatcher(nameView, "name", newInformation));
                    lastnameView.addTextChangedListener(new MyTextWatcher(lastnameView, "lastname", newInformation));
                    emailView.addTextChangedListener(new MyTextWatcher(emailView, "email", newInformation));
                    passwordView.addTextChangedListener(new MyTextWatcher(passwordView, "password", newInformation));

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

        Button editInfoButton = view.findViewById(R.id.profile_edit_info_button);
        editInfoButton.setOnClickListener(view1 -> {
            newInformation.put("old_username", sharedPreferences.getString("username", "Username"));
            newInformation.put("source", "edit");

            if (validateFormInfo(newInformation)) {
                JSONObject requestBody1 = SingletonRequestSender.createJsonRequest(newInformation);
                SingletonRequestSender.sendRequest(requestBody1, getResources().getString(R.string.profile_request_url), new SingletonRequestSender.RequestResult() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Toast toast;
                        try {
                            if (result.get("response").equals("EMAIL_TAKEN")) {
                                toast = Toast.makeText(getContext(), "Email already in use", Toast.LENGTH_LONG);
                                toast.show();
                            } else if (result.get("response").equals("USERNAME_TAKEN")) {
                                toast = Toast.makeText(getContext(), "Username already in use", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("RMA", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences1.edit();

                                if (newInformation.containsKey("username")) {
                                    editor.putString(getString(R.string.pref_username_key), newInformation.get("username"));
                                }
                                if (newInformation.containsKey("lastname")) {
                                    editor.putString(getString(R.string.pref_lastname_key), newInformation.get("lastname"));
                                }
                                if (newInformation.containsKey("lastname")) {
                                    editor.putString(getString(R.string.pref_name_key), newInformation.get("name"));
                                }
                                if (newInformation.containsKey("email")) {
                                    editor.putString(getString(R.string.pref_email_key), newInformation.get("email"));
                                }
                                if (newInformation.containsKey("password")) {
                                    editor.putString(getString(R.string.pref_password_key), newInformation.get("password"));
                                }
                                editor.apply();

                                toast = Toast.makeText(getContext(), "Information updated", Toast.LENGTH_LONG);
                                toast.show();

                                newInformation.clear();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Toast toast = Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG);
                        toast.show();

                        newInformation.clear();
                    }
                });
            }
        });
    }

    public boolean validateFormInfo(Map<String, String> newInformation) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
        Toast toast;
        boolean empty = false;

        for (Map.Entry<String, String> entry : newInformation.entrySet()) {
            if (entry.getValue().isEmpty()) {
                empty = true;
            }
        }

        if (empty) {
            toast = Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if (newInformation.containsKey("email") && !Patterns.EMAIL_ADDRESS.matcher(newInformation.get("email")).matches()) {
            toast = Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if (newInformation.containsKey("password") && !passwordPattern.matcher(newInformation.get("password")).matches()) {
            toast = Toast.makeText(getContext(),
                    "Password must be atleast 8 characters long and contain atleast one number and special character", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        return true;
    }
}