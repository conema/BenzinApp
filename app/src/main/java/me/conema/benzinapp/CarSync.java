package me.conema.benzinapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

public class CarSync extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_sync);

        Toolbar myToolbar = findViewById(R.id.single_car_toolbar);
        myToolbar.setTitle("Aggiungi auto");
        setSupportActionBar(myToolbar);

        //Serve per visualizzare il tasto indietro
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void bluetoothSync(View view) {
        ProgressBar progressBar = findViewById(R.id.PBBluetooth);
        progressBar.setVisibility(View.VISIBLE);

        Intent syncedCar = new Intent(CarSync.this, SyncedCar.class);
        startActivity(syncedCar);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ProgressBar progressBar = findViewById(R.id.PBBluetooth);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


