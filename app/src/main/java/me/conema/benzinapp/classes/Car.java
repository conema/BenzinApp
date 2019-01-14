package me.conema.benzinapp.classes;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Car {
    /*
    TODO: Car photo
    */
    private static int contId;
    private int id;
    private String name;
    private Date lastSync;
    private int kmDone;
    private float kml;
    private int percTank;
    private ArrayList weekHistory;
    private int color;
    private Drawable photo;

    public Car(String name, int kmDone, float kml, ArrayList weekHistory, int color, int percTank, Drawable photo) {
        this.id = contId;
        this.name = name;
        this.lastSync = Calendar.getInstance().getTime();
        this.kmDone = kmDone;
        this.kml = kml;
        this.setPercTank(percTank);
        this.weekHistory = weekHistory;
        this.color = color;
        this.percTank = percTank;
        this.setPhoto(photo);
        contId++;
    }

    public static int getContId() {
        return contId;
    }

    public static void setContId(int contId) {
        Car.contId = contId;
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

    public Drawable getPhoto() {
        return photo;
    }

    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }
}
