package me.conema.benzinapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.conema.benzinapp.classes.App;
import me.conema.benzinapp.classes.AppFactory;
import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;
import me.conema.benzinapp.classes.Review;
import me.conema.benzinapp.classes.ReviewFactory;
import me.conema.benzinapp.classes.Station;
import me.conema.benzinapp.classes.StationFactory;

public class SingleStation extends AppCompatActivity {
    ImageView imgStazione;
    TextView viaStazione, prezzoStazione, votoStazione;
    ImageButton buttonStazione;
    int idStation = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_station);
        imgStazione = findViewById(R.id.imgStazione);
        viaStazione = findViewById(R.id.viaStatione);
        prezzoStazione = findViewById(R.id.prezzoStazione);
        votoStazione = findViewById(R.id.votoStazione);
        buttonStazione = findViewById(R.id.buttonStazione);
        Toolbar myToolbar = findViewById(R.id.single_car_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        StationFactory stationFactory = StationFactory.getInstance();

        Intent intent = getIntent();
        Bundle obj = intent.getExtras();

        if(obj != null)
            idStation = obj.getInt("stationId");

        Station station = stationFactory.getStationById(idStation);

        imgStazione.setImageResource(station.getImg());
        viaStazione.setText(station.getAddress());
        prezzoStazione.setText(Double.toString(station.getPrice()));
        votoStazione.setText(Double.toString(station.getMark()));
        getSupportActionBar().setTitle(station.getAddress());
        updateReviewList(idStation);

        buttonStazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = 37.7749;
                double longit = -122.4194;
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat + "," + longit);

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateReviewList(idStation);
    }

    private void updateReviewList(int idStazione){
        ReviewFactory reviewFactory = ReviewFactory.getInstance();
        ArrayList<Review> reviewArrayList = reviewFactory.getReviewByStation(idStazione);
        LinearLayout reviewContainer = findViewById(R.id.container);

        reviewContainer.removeAllViews();

        for(Review review : reviewArrayList){
            View view = getLayoutInflater().from(this).inflate(R.layout.review_linear_layout, reviewContainer, false);
            TextView nomeReview = view.findViewById(R.id.nomeReview);
            nomeReview.setText(review.getName());
            TextView votoReview = view.findViewById(R.id.votoReview);
            votoReview.setText(Double.toString(review.getVote()));
            TextView testoReview = view.findViewById(R.id.testoReview);
            testoReview.setText(review.getDescription());
            reviewContainer.addView(view);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        MenuItem icon = menu.getItem(0);
        AppFactory appFactory = AppFactory.getInstance();
        App app = appFactory.getApp();
        if(app.getFavStation() != null && app.getFavStation().getId() == idStation){
            icon.setIcon(R.drawable.ic_favorite_red_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favoriteStation:
                checkFavStation(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkFavStation(MenuItem item){
        AppFactory appFactory = AppFactory.getInstance();
        App app = appFactory.getApp();
        if(app.getFavStation() == null) {
            StationFactory stationFactory = StationFactory.getInstance();
            Station station = stationFactory.getStationById(idStation);
            app.setFavStation(station);

            Toast.makeText(this, "Stazione aggiunta alle preferite", Toast.LENGTH_SHORT).show();
            item.setIcon(R.drawable.ic_favorite_red_24dp);
        }
        else {
            if (app.getFavStation().getId() == idStation) {
                StationFactory stationFactory = StationFactory.getInstance();
                Station station = stationFactory.getStationById(idStation);
                app.setFavStation(null);

                Toast.makeText(this, "Stazione rimossa dalle preferite", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleStation.this);
                builder.setTitle("Hai già una stazione preferita, vuoi sostituirla con questa?")
                        .setPositiveButton("sì", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                StationFactory stationFactory = StationFactory.getInstance();
                                Station station = stationFactory.getStationById(idStation);
                                app.setFavStation(station);
                                item.setIcon(R.drawable.ic_favorite_red_24dp);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

}
