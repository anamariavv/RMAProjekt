package com.example.rmaprojekt;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout navigationDrawer;

   @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationDrawer = findViewById(R.id.navigation_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navigationDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navigationDrawer.addDrawerListener(toggle);
        toggle.syncState();

        Fragment homepageFragment = new HomepageFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment_frame, homepageFragment)
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    public void onBackPressed() {
       if(navigationDrawer.isDrawerOpen(GravityCompat.START)) {
           navigationDrawer.closeDrawer(GravityCompat.START);
       } else {
           super.onBackPressed();
       }
    }
}