package com.bookingapptim24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bookingapptim24.databinding.ActivityHomeScreenNavigationBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeScreen extends AppCompatActivity {

    private @NonNull ActivityHomeScreenNavigationBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Set<Integer> topLevelDestinations = new HashSet<>();
  
    public static String user = "host";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("OpenDoors", "HomeScreen onCreate()");

        binding = ActivityHomeScreenNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpMenu();

        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        toolbar = binding.activityHomeScreen.toolbar;
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            //when we add the logo
            // actionBar.setHomeAsUpIndicator();
            actionBar.setHomeButtonEnabled(true);
        }


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        topLevelDestinations.add(R.id.nav_settings);

        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);

        mAppBarConfiguration = new AppBarConfiguration
                .Builder(R.id.nav_show_all, R.id.nav_profile, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();


        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    @Override
    protected void onStart(){

        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void setUpMenu() {
        //temporary solution without server
        if (user == null || user.isEmpty())
            binding.navView.inflateMenu(R.menu.nav_menu_unrecognised);
        else if (user.startsWith("g"))
            binding.navView.inflateMenu(R.menu.nav_menu_guest);
        else if (user.startsWith("a"))
            binding.navView.inflateMenu(R.menu.nav_menu_admin);
        else
            binding.navView.inflateMenu(R.menu.nav_menu_host);
    }

    public void clearBackStack(MenuItem item) {
        Intent intent = new Intent(this, LoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

