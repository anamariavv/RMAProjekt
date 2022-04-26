package com.example.rmaprojekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout navigationDrawer;
    protected SharedPreferences sharedPref;
    protected SharedPreferences.Editor sharedPrefEditor;

   @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       sharedPref = this.getPreferences(Context.MODE_PRIVATE);
       sharedPrefEditor = sharedPref.edit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationDrawer = findViewById(R.id.navigation_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navigationDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navigationDrawer.addDrawerListener(toggle);
        toggle.syncState();

       FragmentManager fragmentManager = getSupportFragmentManager();

       NavigationView navigationView = findViewById(R.id.navigation_drawer);
       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()) {
                   case R.id.nav_home:
                       fragmentManager.beginTransaction().replace(R.id.main_fragment_frame, new HomepageFragment())
                               .setReorderingAllowed(true)
                               .addToBackStack(null)
                               .commit();
                       break;
                   case R.id.nav_profile:
                       fragmentManager.beginTransaction().replace(R.id.main_fragment_frame, new ProfileFragment())
                               .setReorderingAllowed(true)
                               .addToBackStack(null)
                               .commit();
                       break;
                   case R.id.nav_message:
                       fragmentManager.beginTransaction().replace(R.id.main_fragment_frame, new MessageFragment())
                               .setReorderingAllowed(true)
                               .addToBackStack(null)
                               .commit();
                       break;
                   case R.id.nav_forum:
                       fragmentManager.beginTransaction().replace(R.id.main_fragment_frame, new ForumFragment())
                               .setReorderingAllowed(true)
                               .addToBackStack(null)
                               .commit();
                       break;
                   case R.id.nav_search:
                       fragmentManager.beginTransaction().replace(R.id.main_fragment_frame, new SearchFragment())
                               .setReorderingAllowed(true)
                               .addToBackStack(null)
                               .commit();
                       break;
               }
               navigationDrawer.closeDrawer(GravityCompat.START);
               return true;
           }
       });

        Fragment homepageFragment = new HomepageFragment();

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