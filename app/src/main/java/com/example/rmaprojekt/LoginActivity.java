package com.example.rmaprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SingletonRequestSender.getInstance(getApplicationContext());

        Fragment loginFragment = new LoginFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(new RegisterFragment(), "registerFrag").commitNow();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, loginFragment)
                .setReorderingAllowed(true)
                .commit();

    }
}