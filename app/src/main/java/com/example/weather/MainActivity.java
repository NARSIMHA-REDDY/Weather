package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavController navController;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityFragment(3534);
        setUpNavigation();
    }

    public void setUpNavigation(){
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigationView);
        navController= Navigation.findNavController(this,R.id.defaultfrag);
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);
        NavigationUI.setupWithNavController(navigationView,navController);

        getSupportActionBar().setTitle("Montreal");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setCheckable(true);

                switch (item.getItemId()) {
                    default:
                        cityFragment(3534);
                        getSupportActionBar().setTitle("Montreal");
                        break;
                    case R.id.second:
                        cityFragment(8775);
                        getSupportActionBar().setTitle("Calgary");
                        break;
                    case R.id.third:
                        cityFragment(4118);
                        getSupportActionBar().setTitle("Toronto");
                        break;
                    case R.id.fourth:
                        cityFragment(8676);
                        getSupportActionBar().setTitle("Edmonton");
                        break;
                    case R.id.fifth:
                        cityFragment(9807);
                        getSupportActionBar().setTitle("Vancouver");
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    public void cityFragment(int id) {

        Bundle bundle = new Bundle();
        bundle.putInt("cityid", id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DefaultFragment defaultFragment = new DefaultFragment();

        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
        fragmentTransaction.add(R.id.host_fragment, defaultFragment);
        fragmentTransaction.addToBackStack(null);
        defaultFragment.setArguments(bundle);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.defaultfrag),drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else

            super.onBackPressed();
    }
}
