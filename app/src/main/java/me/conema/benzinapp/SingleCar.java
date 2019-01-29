package me.conema.benzinapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

    int id = 2;
    Car car;
    CarFactory carFactory = CarFactory.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_car);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

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
            id = obj.getInt("carId");

        car = carFactory.getCarById(id);

        if(car != null){
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            kmSingleCar.setText("Ultimo aggiornamento : " + df.format(car.getLastSync()));

            myToolbar.setTitle(String.valueOf((car.getName())));
            kmFatti.setText(String.valueOf(car.getKmDone()));
            consumo.setText(String.valueOf(car.getKml()));
            kmRimanenti.setText(String.valueOf(car.getPercTank()));
            lRimanenti.setText(String.valueOf(car.getPercTank()));
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
}
