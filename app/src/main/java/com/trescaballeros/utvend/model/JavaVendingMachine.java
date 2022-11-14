package com.trescaballeros.utvend.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.sql.Time;

public class JavaVendingMachine {
    private String extra_notes;
    private String geo_notes;
    private String image;
    private GeoPoint location;
    private Timestamp timestamp;

    public JavaVendingMachine() {}

    public JavaVendingMachine(String extra_notes, String geo_notes, String image, GeoPoint location, Timestamp timestamp) {
        this.extra_notes = extra_notes;
        this.geo_notes = geo_notes;
        this.image = image;
        this.location = location;
        this.timestamp = timestamp;
    }

    public String getExtra_notes() {
        return extra_notes;
    }

    public void setExtra_notes(String extra_notes) {
        this.extra_notes = extra_notes;
    }

    public String getGeo_notes() {
        return geo_notes;
    }

    public void setGeo_notes(String geo_notes) {
        this.geo_notes = geo_notes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
