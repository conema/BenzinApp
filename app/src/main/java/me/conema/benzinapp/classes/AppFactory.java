package me.conema.benzinapp.classes;


import java.util.Date;
import java.util.Stack;

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
        Stack<Station> lastStations = new Stack<>();
        lastStations.push(StationFactory.getInstance().getStationById(24));
        lastStations.push(StationFactory.getInstance().getStationById(0));

        app = new App(null, null, lastStations, new Date());
    }

    public App getApp() {
        return app;
    }
}
