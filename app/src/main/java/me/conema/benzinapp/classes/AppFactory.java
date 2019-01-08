package me.conema.benzinapp.classes;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppFactory {
    private static AppFactory singleton;
    private App app;

    public static AppFactory getInstance() {
        if (singleton == null) {
            singleton = new AppFactory();
        }
        return singleton;
    }

    private AppFactory() {
        ArrayList<Station> lastStations = new ArrayList<>();
        lastStations.add(StationFactory.getInstance().getStationById(1));
        lastStations.add(StationFactory.getInstance().getStationById(2));

        app = new App(CarFactory.getInstance().getAutoById(0), StationFactory.getInstance().getStationById(0), lastStations, new Date());
    }

    public App getApp() {
        return app;
    }
}
