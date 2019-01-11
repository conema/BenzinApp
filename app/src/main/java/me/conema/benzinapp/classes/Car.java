package me.conema.benzinapp.classes;

import android.graphics.Color;

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
    private int percTank;
    private ArrayList weekHistory;
    private int color;

    public Car(int id, String name, int kmDone, float kml, ArrayList weekHistory, int color, int percTank) {
        this.id = id;
        this.name = name;
        this.lastSync = Calendar.getInstance().getTime();
        this.kmDone = kmDone;
        this.kml = kml;
        this.setPercTank(percTank);
        this.weekHistory = weekHistory;
        this.color = color;
        this.percTank = percTank;
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


    public int getPercTank() {
        return percTank;
    }

    public void setPercTank(int percTank) {
        this.percTank = percTank;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
