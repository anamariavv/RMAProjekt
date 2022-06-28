package com.example.rmaprojekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private SharedViewModel sharedViewModel;
    private DrawerLayout navigationDrawer;
    private FragmentManager fragmentManager;
    protected SharedPreferences sharedPref;
    protected SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationDrawer = findViewById(R.id.navigation_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navigationDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navigationDrawer.addDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        switchFragment(HomepageFragment.class, savedInstanceState);
                        break;
                    case R.id.nav_profile:
                        switchFragment(ProfileFragment.class, savedInstanceState);
                        break;
                    case R.id.nav_message:
                        switchFragment(MessageFragment.class, savedInstanceState);
                        break;
                    case R.id.nav_forum:
                        switchFragment(ForumFragment.class, savedInstanceState);
                        break;
                    case R.id.nav_search:
                        switchFragment(SearchFragment.class, savedInstanceState);;
                        break;
                    case R.id.nav_logout:
                        createShowDialog();
                        break;
                }
                navigationDrawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        switchFragment(HomepageFragment.class, savedInstanceState);
    }


    private void switchFragment (Class fragmentClass, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.main_fragment_frame, fragmentClass, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void createShowDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setMessage(getString(R.string.logout_dialog_text)).setTitle("Log out");
        dialogBuilder.setPositiveButton(R.string.logout_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("RMA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
            }
        });

        dialogBuilder.setNegativeButton(R.string.logout_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
            navigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}