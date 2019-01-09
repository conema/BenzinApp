package me.conema.benzinapp;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import me.conema.benzinapp.classes.App;
import me.conema.benzinapp.classes.AppFactory;
import me.conema.benzinapp.classes.Station;

public class Home extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_stations:
                    //Open activity
                    return true;
                case R.id.navigation_car:
                    //open car
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


        Button addCar = findViewById(R.id.add_car);
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add car
            }
        });
    }

    //Fav and last stations may change
    @Override
    protected void onResume() {
        super.onResume();

        Context context = getApplicationContext();

        Button favCar = findViewById(R.id.fav_car);
        Button favStation = findViewById(R.id.fav_station);

        //Favourites
        favCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: fav car
            }
        });

        favStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: fav station
            }
        });

        updateLastStations();
    }


    private void updateLastStations() {
        //Last stations
        GridLayout lastStations = findViewById(R.id.last_stations_grid);

        //TODO: Delete view

        App app = AppFactory.getInstance().getApp();

        for (Station station : app.getLastStations()) {
            View view = getLayoutInflater().from(this).inflate(R.layout.last_station, lastStations, false);

            TextView stationName = view.findViewById(R.id.last_station_name);
            stationName.setText(station.getName());

            TextView stationPrice = view.findViewById(R.id.last_station_price);
            stationPrice.setText(station.getPrice() + "");

            //TODO: station img

            lastStations.addView(view);
        }
    }
}
