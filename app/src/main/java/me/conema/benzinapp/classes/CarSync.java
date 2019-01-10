package me.conema.benzinapp.classes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import me.conema.benzinapp.R;

public class CarSync extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_sync);
    }

    public void bluetoothSync(View view) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.PBBluetooth);
        progressBar.setVisibility(View.VISIBLE);

        Intent syncedCar = new Intent(CarSync.this, SyncedCar.class);
        startActivity(syncedCar);
    }
}
