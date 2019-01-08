package me.conema.benzinapp.classes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Car {
    /*
    TODO: Car photo
    */
    private int id;
    private String name;
    private Date lastSync;
    private int kmDone;
    private float kml;
    private ArrayList weekHistory;

    Car(int id, String name, int kmDone, float kml, ArrayList weekHistory) {
        this.id = id;
        this.name = name;
        this.lastSync = Calendar.getInstance().getTime();
        this.kmDone = kmDone;
        this.kml = kml;
        this.weekHistory = weekHistory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastSync() {
        return lastSync;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }

    public int getKmDone() {
        return kmDone;
    }

    public void setKmDone(int kmDone) {
        this.kmDone = kmDone;
    }

    public float getKml() {
        return kml;
    }

    public void setKml(float kml) {
        this.kml = kml;
    }

    public ArrayList getWeekHistory() {
        return weekHistory;
    }

    public void setWeekHistory(ArrayList weekHistory) {
        this.weekHistory = weekHistory;
    }
}
