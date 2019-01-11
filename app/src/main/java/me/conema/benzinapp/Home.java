package me.conema.benzinapp;

import android.content.Intent;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.GridLayout;
import android.widget.Button;
import android.view.View;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import me.conema.benzinapp.R;
import me.conema.benzinapp.classes.App;
import me.conema.benzinapp.classes.AppFactory;
import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;
import me.conema.benzinapp.classes.Station;

public class Home extends AppCompatActivity {

    private App app = AppFactory.getInstance().getApp();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_stations:
                    // va usato Fragment
                    return true;
                case R.id.navigation_car:
                    Intent carList = new Intent(Home.this, CarList.class);
                    startActivity(carList);
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

        TextView statsFromDate = findViewById(R.id.stats_from_date);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        statsFromDate.setText(df.format(app.getLastSync()));


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
        updateCarStats();
    }


    private void updateLastStations() {
        //Last stations
        GridLayout lastStations = findViewById(R.id.last_stations_grid);

        //Delete child view (old station)
        lastStations.removeAllViews();

        App app = AppFactory.getInstance().getApp();

        for (Station station : app.getLastStations()) {
            View view = getLayoutInflater().from(this).inflate(R.layout.last_station, lastStations, false);

            ImageView stationImg = view.findViewById(R.id.last_station_img);
            stationImg.setImageResource(station.getImg());

            TextView stationName = view.findViewById(R.id.last_station_name);
            stationName.setText(station.getName());

            TextView stationPrice = view.findViewById(R.id.last_station_price);
            stationPrice.setText(station.getPrice() + "");

            //TODO: station img

            lastStations.addView(view);
        }
    }

    private void updateCarStats() {
        ArrayList<Car> cars = CarFactory.getInstance().getCars();

        LinearLayout statsList = findViewById(R.id.stats_list);
        LinearLayout statsCircles = findViewById(R.id.stats_circles);

        //Delete child view (old line and circle stats)
        statsList.removeAllViews();
        statsCircles.removeAllViews();

        //Circle total
        View view = getLayoutInflater().from(this).inflate(R.layout.car_circle, statsCircles, false);

        TextView circleName = view.findViewById(R.id.stats_circle_name);
        circleName.setText("Totale");

        ImageView circle = view.findViewById(R.id.stats_circle);
        circle.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);

        statsCircles.addView(view);

        //Line total
        view = getLayoutInflater().from(this).inflate(R.layout.car_stats, statsList, false);
        ProgressBar progressBar = view.findViewById(R.id.stats_progressbar);
        progressBar.setProgress(100);           //total is at 100%
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLACK));

        int totalL = CarFactory.getInstance().getTotalL();

        TextView statsL = view.findViewById(R.id.stats_l);
        statsL.setText(totalL + "L");

        statsList.addView(view);

        for (Car car : cars) {
            //Circle
            view = getLayoutInflater().from(this).inflate(R.layout.car_circle, statsCircles, false);

            circleName = view.findViewById(R.id.stats_circle_name);
            circleName.setText(car.getName());

            circle = view.findViewById(R.id.stats_circle);
            circle.setColorFilter(car.getColor(), PorterDuff.Mode.SRC_ATOP);

            statsCircles.addView(view);

            //Line
            view = getLayoutInflater().from(this).inflate(R.layout.car_stats, statsList, false);

            progressBar = view.findViewById(R.id.stats_progressbar);
            progressBar.setProgress((int) (100 * car.getKmDone() * car.getKml() / totalL));

            progressBar.setProgressTintList(ColorStateList.valueOf(car.getColor()));

            statsL = view.findViewById(R.id.stats_l);
            statsL.setText(car.getKmDone() * car.getKml() + "L");

            statsList.addView(view);
        }
    }
}
