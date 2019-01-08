package me.conema.benzinapp.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CarFactory {
    private static CarFactory singleton;
    private ArrayList<Car> carList = new ArrayList<>();

    static CarFactory getInstance() {
        if (singleton == null) {
            singleton = new CarFactory();
        }
        return singleton;
    }

    private CarFactory() {
        Car car1 = new Car(0, "Pandy", 0, 0, null);
        Car car2 = new Car(1, "Francy", 0, 0, null);
        Car car3 = new Car(2, "Sfiesta", 0, 0, null);

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

    Car getAutoById(int id) {
        for (Car car : carList) {
            if (car.getId() == id) {
                return car;
            }
        }

        return null;
    }
}
