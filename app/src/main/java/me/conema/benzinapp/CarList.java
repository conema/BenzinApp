package me.conema.benzinapp;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;

public class CarList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        updateCarList();

        //Serve per visualizzare il tasto indietro
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateCarList();
    }


    private void updateCarList() {
        CarFactory carFactory = CarFactory.getInstance();
        ArrayList<Car> carArrayList = carFactory.getCars();
        LinearLayout container = findViewById(R.id.linearLayoutCarList);

        //Empty views (old cars)
        container.removeAllViews();

        //App app = AppFactory.getInstance().getApp();

        for (Car car : carArrayList) {
            int i = 1;
            View view = getLayoutInflater().from(this).inflate(R.layout.car_list_relative_layout, container, false);
            TextView carNome = view.findViewById(R.id.carNome);
            carNome.setText(car.getName());
            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            ImageView carImgView = view.findViewById(R.id.carImg);

            /*int carImgId = getResources().getIdentifier("R.drawable.carimg"+ i + ".jpg", "id", getResources().getResourcePackageName());
            carImgView.setImageDrawable(carImgId);
            carImgView.setImageDrawable(R.drawable.carimg1); */

            if (car.getPercTank() >= 60) {
                Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
                progressBar.setProgressDrawable(progressDrawable);
                progressBar.setProgress(car.getPercTank(), true);
            } else if (car.getPercTank() >= 30) {
                Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(Color.parseColor("#ffff99"), android.graphics.PorterDuff.Mode.SRC_IN);
                progressBar.setProgressDrawable(progressDrawable);
                progressBar.setProgress(car.getPercTank(), true);
            } else {
                Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                progressBar.setProgressDrawable(progressDrawable);
                progressBar.setProgress(car.getPercTank(), true);
            }
            TextView kmView = view.findViewById(R.id.kmTextView);
            kmView.setText("Km residui " + car.getKmDone());
            carImgView.setImageDrawable(car.getPhoto());
            container.addView(view);
            i++;
        }


    }
}
