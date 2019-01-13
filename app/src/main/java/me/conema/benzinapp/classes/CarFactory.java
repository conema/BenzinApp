package me.conema.benzinapp.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import me.conema.benzinapp.R;

import static android.support.v4.content.res.ResourcesCompat.getDrawable;

public class CarFactory {
    private static CarFactory singleton;
    private ArrayList<Car> carList = new ArrayList<>();

    public static CarFactory getInstance() {
        if (singleton == null) {
            singleton = new CarFactory();
        }
        return singleton;
    }

    private CarFactory() {
        Context context = ApplicationContextProvider.getContext();
        Drawable img1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.carimg2back, null);
        Car car1 = new Car(0, "Pandy", 37, 15, null, Color.parseColor("#4d7099"), 80, img1);

        Drawable img2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.carimg2back, null);
        Car car2 = new Car(1, "Francy", 108, 18, null, Color.parseColor("#8492A6"), 40, img2);


        Drawable img3 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.carimg2back, null);
        Car car3 = new Car(2, "Sfiesta", 21, 13, null, Color.parseColor("#E0E6ED"), 77, img3);

        carList.add(car1);
        carList.add(car2);
        carList.add(car3);
    }

    public ArrayList<Car> getCars() {
        ArrayList<Car> cars = new ArrayList<>();

        for (Car car : carList) {
            cars.add(car);
        }

        return cars;
    }

    public int addCar(Car car) {
        carList.add(car);
        return car.getId();
    }

    public boolean removeCar(Car car) {
        return carList.remove(car);
    }

    Car getCarById(int id) {
        for (Car car : carList) {
            if (car.getId() == id) {
                return car;
            }
        }

        return null;
    }

    public int getTotalL() {
        int totL = 0;

        for (Car car : carList) {
            totL += car.getKmDone() * car.getKml();
        }

        return totL;
    }
}
