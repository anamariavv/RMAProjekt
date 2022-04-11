package com.example.rmaprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SingletonRequestSender.getInstance(getApplicationContext());
        //create login fragment
        Fragment loginFragment = new LoginFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(new RegisterFragment(), "registerFrag").commitNow();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, loginFragment)
                .setReorderingAllowed(true)
                .commit();

    }
}