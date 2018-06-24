package com.example.dell.app5;

import java.io.Serializable;

/**
 * Created by DELL on 04-01-2018.
 */

public class send implements Serializable {
    private String Tlatitude, Tlongitude;

    public send(String lat, String lon) {
        this.Tlatitude = lat;
        this.Tlongitude = lon;
    }

    public String getTlatitude() {
        return Tlatitude;
    }

    public String getTlongitude() {
        return Tlongitude;
    }
}