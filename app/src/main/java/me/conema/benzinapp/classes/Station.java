package me.conema.benzinapp.classes;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Station {
    private int id;
    private String name;
    private String address;
    private double price;
    private double mark;
    private int img;

    Station(int id, String name, String address, double price, int img, double mark) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.img = img;
        this.mark = mark;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public double getMark() {
        ArrayList<Review> reviews = ReviewFactory.getInstance().getReviewByStation(id);
        double mark = 0;

        for (Review review : reviews) {
            mark += review.getVote();
        }

        if (reviews.size() > 0) {
            return mark / reviews.size();
        } else {
            return 0;
        }
    }

    public void setMark(double mark) {
        this.mark = mark;
    }
}
