package me.conema.benzinapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import me.conema.benzinapp.classes.App;
import me.conema.benzinapp.classes.AppFactory;
import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;

public class SingleCar extends AppCompatActivity {

    TextView carName;
    TextView kmSingleCar;
    TextView kmFatti;
    TextView consumo;
    TextView kmRimanenti;
    TextView lRimanenti;
    ImageButton singleCar;
    Button deleteButton;

    int idCar = 2;
    Car car;
    float benzinaRimanente;
    CarFactory carFactory = CarFactory.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_car);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        TextView mTitle = myToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(myToolbar);
        mTitle.setText(myToolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);

        //Serve per visualizzare il tasto indietro
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        kmSingleCar = findViewById(R.id.kmSingleCar);
        kmFatti = findViewById(R.id.kmFatti);
        consumo = findViewById(R.id.consumo);
        kmRimanenti = findViewById(R.id.kmRimanenti);
        lRimanenti = findViewById(R.id.lRimanenti);
        singleCar = findViewById(R.id.singleCarImage);
        deleteButton = findViewById(R.id.deleteButton);


        Intent intent = getIntent();
        Bundle obj = intent.getExtras();

        if(obj != null)
            idCar = obj.getInt("carId");

        car = carFactory.getCarById(idCar);

        if(car != null){

            benzinaRimanente = car.getPercTank()*car.getTankCapacity()/100;
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            kmSingleCar.setText("Ultimo aggiornamento : " + df.format(car.getLastSync()));

            mTitle.setText(car.getName());
            kmFatti.setText(String.valueOf(car.getKmDone()));
            consumo.setText(String.valueOf(car.getKml()));
            kmRimanenti.setText(String.valueOf(car.getKml()*benzinaRimanente));
            lRimanenti.setText(String.valueOf(benzinaRimanente));
            singleCar.setImageDrawable(car.getPhoto());
        }


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleCar.this);
                builder.setTitle("Sei sicuro di voler cancellare l' auto?")
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                carFactory.removeCar(car);
                                Intent lista = new Intent(SingleCar.this,Home.class);
                                startActivity(lista);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent lista = new Intent(SingleCar.this, Home.class);
        startActivity(lista);
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
        if (app.getFavCar() != null && app.getFavCar().getId() == idCar) {
            icon.setIcon(R.drawable.ic_favorite_red_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favoriteStation:
                checkFavCar(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkFavCar(MenuItem item) {
        AppFactory appFactory = AppFactory.getInstance();
        App app = appFactory.getApp();
        if (app.getFavCar() == null) {
            CarFactory carFactory = CarFactory.getInstance();
            Car car = carFactory.getCarById(idCar);
            app.setFavCar(car);

            Toast.makeText(this, "Auto aggiunta alle preferite", Toast.LENGTH_SHORT).show();
            item.setIcon(R.drawable.ic_favorite_red_24dp);
        } else {
            if (app.getFavCar().getId() == idCar) {
                CarFactory carFactory = CarFactory.getInstance();
                app.setFavCar(null);
                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleCar.this);
                builder.setTitle("Hai già un'auto preferita, vuoi sostituirla con questa?")
                        .setPositiveButton("sì", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CarFactory carFactory = CarFactory.getInstance();
                                Car car = carFactory.getCarById(idCar);
                                app.setFavCar(car);
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
