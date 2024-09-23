package com.example.navigationdrawer;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1 --> set up Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Step 2 --> setup DrawerLayout and Toggle
        drawerLayout = findViewById(R.id.main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigationLayout);

        // Load the default Home fragment
        loadFrag(new Home());

        // Set up navigation listener for menu items
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                loadFrag(new Home());
            } else if (id == R.id.setting) {
                loadFrag(new Setting());
            } else if (id == R.id.call) {
                loadFrag(new Call());
            } else if (id == R.id.help) {
                loadFrag(new Help());
            } else {
                loadFrag(new Logout());
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void loadFrag(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (isFirst) {
            ft.add(R.id.container, fragment);
            isFirst = false;
        } else {
            ft.replace(R.id.container, fragment);
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        // Close the drawer if it's open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
