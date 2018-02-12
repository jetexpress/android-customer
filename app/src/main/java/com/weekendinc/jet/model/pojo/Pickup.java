package com.weekendinc.jet.model.pojo;

import org.parceler.Parcel;

/**
 * Created by Fadhlan on 5/27/15.
 */
@Parcel
public class Pickup {
    long id;
    String title;
    String name;
    String image;
    int imageResId;
    int numberOfItem;
    String location;
    double weight;
    double volume;
    String address_pickup;
    String address_destination;
    int status;
    String status_desc;

    public Pickup() {
    }

    public Pickup(long id, String title, String name, String image, int imageResId, String
            location, double weight, double volume, String address_pickup, String
                          address_destination) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.image = image;
        this.imageResId = imageResId;
        this.location = location;
        this.weight = weight;
        this.volume = volume;
        this.address_pickup = address_pickup;
        this.address_destination = address_destination;
        status = 0;
        this.numberOfItem = 1;
    }

    public Pickup(long id, String title, String name, String image, int imageResId, String
            location, double weight, double volume, String address_pickup, String
                          address_destination, int status, String status_desc) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.image = image;
        this.imageResId = imageResId;
        this.location = location;
        this.weight = weight;
        this.volume = volume;
        this.address_pickup = address_pickup;
        this.address_destination = address_destination;
        this.status = status;
        this.status_desc = status_desc;
        this.numberOfItem = 1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public String getAddress_destination() {
        return address_destination;
    }

    public void setAddress_destination(String address_destination) {
        this.address_destination = address_destination;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getAddress_pickup() {
        return address_pickup;
    }

    public void setAddress_pickup(String address_pickup) {
        this.address_pickup = address_pickup;
    }


    public int getNumberOfItem() {
        return numberOfItem;
    }

    public void setNumberOfItem(int numberOfItem) {
        this.numberOfItem = numberOfItem;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pickup) {
            Pickup obj = (Pickup) o;
            if (obj.getId() == id)
                return true;
        }
        return false;
    }
}
