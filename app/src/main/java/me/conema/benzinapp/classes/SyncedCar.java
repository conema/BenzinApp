package me.conema.benzinapp.classes;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import me.conema.benzinapp.R;
import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;

public class SyncedCar extends AppCompatActivity {
    EditText carText, roadText, fuelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synced_car);
        carText = (EditText) findViewById(R.id.carEditText);
        fuelText = (EditText) findViewById(R.id.fuelEditText);
        roadText = (EditText) findViewById(R.id.roadEditText);
        final CarFactory factory = CarFactory.getInstance();
        Car car = new Car(0, "Fiat punto", 55000, 17, null, Color.parseColor("#121212"), 70);
        carText.setText(car.getName());
        fuelText.setText("5");
        roadText.setText(car.getKmDone());

    }
}
