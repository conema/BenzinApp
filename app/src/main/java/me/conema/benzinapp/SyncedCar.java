package me.conema.benzinapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;

public class SyncedCar extends AppCompatActivity {
    EditText carText, roadText, fuelText;
    ImageButton imgPicker;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synced_car);
        carText = (EditText) findViewById(R.id.carEditText);
        fuelText = (EditText) findViewById(R.id.fuelEditText);
        roadText = (EditText) findViewById(R.id.roadEditText);
        imgPicker = findViewById(R.id.imgButton);
        saveButton = findViewById(R.id.saveButton);

        final CarFactory factory = CarFactory.getInstance();
        Car car = new Car(0, "Fiat punto", 55000, 17, null, Color.parseColor("#121212"), 70);
        carText.setText(car.getName());
        fuelText.setText("5");
        roadText.setText(car.getKmDone());

        imgPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO prendere l'immagine
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carName = carText.getText().toString();
                int kmDone = Integer.parseInt(roadText.getText().toString());
                Car car = new Car(0, carName, kmDone, 23, null, 123, 80);
                factory.addCar(car);
            }
        });



    }
}
