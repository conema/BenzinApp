package me.conema.benzinapp;

import android.content.Intent;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.GridLayout;
import android.widget.Button;
import android.view.View;


public class Home extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, CarListFragment.OnFragmentInteractionListener {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment, false);
                    return true;
                case R.id.navigation_stations:
                    fragment = new StationsFragment();
                    loadFragment(fragment, false);
                    return true;
                case R.id.navigation_car:
                    /*Intent carList = new Intent(Home.this, CarList.class);
                    startActivity(carList);*/
                    fragment = new CarListFragment();
                    loadFragment(fragment,false);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        loadFragment(new HomeFragment(), true);
    }

    private void loadFragment(Fragment fragment, boolean isHome) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        if (!isHome) {
            transaction.addToBackStack(fragment.toString());
        }
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri){
//you can leave it empty
    }

    @Override
    public boolean onSupportNavigateUp() {
        loadFragment(new HomeFragment(), true);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            //getSupportFragmentManager().popBackStack();

            loadFragment(new HomeFragment(), true);
            BottomNavigationView bottomNavigationView;
            bottomNavigationView = findViewById(R.id.navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else {
            super.onBackPressed();
        }
    }
}
