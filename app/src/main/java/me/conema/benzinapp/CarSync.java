package me.conema.benzinapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CarSync extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_sync);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        TextView mTitle = myToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Aggiungi auto");
        mTitle.setText(myToolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);

        //Serve per visualizzare il tasto indietro
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void bluetoothSync(View view) {
        ProgressBar progressBar = findViewById(R.id.PBBluetooth);
        progressBar.setVisibility(View.VISIBLE);

        (new Handler()).postDelayed(this::changeActivity, 2000);
    }

    public void changeActivity() {
        Intent syncedCar = new Intent(CarSync.this, SyncedCar.class);
        startActivity(syncedCar);
        finish();
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


