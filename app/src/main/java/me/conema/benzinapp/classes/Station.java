package me.conema.benzinapp.classes;

public class Station {
    /*
    TODO: Station logo
     */
    private int id;
    private String name;
    private String address;
    private double price;

    Station(int id, String name, String address, double price) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.price = price;
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
}
