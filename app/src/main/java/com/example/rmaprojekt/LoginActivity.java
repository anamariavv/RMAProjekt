package com.example.rmaprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SingletonRequestSender.getInstance(getApplicationContext());

        //if username and password exist in shared preferences, skip login page
        SharedPreferences sharedPreferences = getSharedPreferences("RMA", MODE_PRIVATE);

        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);

        if(username == null|| password == null) {
            Fragment loginFragment = new LoginFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(new RegisterFragment(), "registerFrag").commitNow();
            fragmentManager.beginTransaction().replace(R.id.fragmentFrame, loginFragment)
                    .setReorderingAllowed(true)
                    .commit();
        } else {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
        }

    }
}