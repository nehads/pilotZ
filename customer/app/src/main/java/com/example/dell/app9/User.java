package com.example.dell.app9;

/**
 * Created by DELL on 28-11-2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

   // public String requests;
   // public int OTP;
    public int otp;
    public boolean reply;
    public String pilot_id;
    String rID,lat2,lon2;
    double lat1,lon1;
    String strAdd,strAddor;
    Float fare;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(int otp,boolean reply,String pilot_id,double lat1, String lat2, double lon1, String lon2,String strAdd,String strAddor, Float fare) {

        this.otp=otp;
        this.reply=reply;
        this.pilot_id=pilot_id;
        this.lat1 = lat1;
        this.lat2 = lat2;
        this.lon1 = lon1;
        this.lon2 = lon2;
        this.strAdd=strAdd;
        this.strAddor=strAddor;
        this.fare=fare;

    }


    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }

    public String getPilot_id() {
        return pilot_id;
    }

    public void setPilot_id(String pilot_id) {
        this.pilot_id = pilot_id;
    }

    public String getrID() {
        return rID;
    }

    public void setrID(String rID) {
        this.rID = rID;
    }

    public double getLat1() {
        return lat1;
    }

    public void setLat1(double lat1) {
        this.lat1 = lat1;
    }

    public String getLat2() {
        return lat2;
    }

    public void setLat2(String lat2) {
        this.lat2 = lat2;
    }

    public double getLon1() {
        return lon1;
    }

    public void setLon1(double lon1) {
        this.lon1 = lon1;
    }

    public String getLon2() {
        return lon2;
    }

    public void setLon2(String lon2) {
        this.lon2 = lon2;
    }

    public String getStrAdd() {
        return strAdd;
    }

    public void setStrAdd(String strAdd) {
        this.strAdd = strAdd;
    }

    public String getStrAddor() {
        return strAddor;
    }

    public void setStrAddor(String strAddor) {
        this.strAddor = strAddor;
    }

    public Float getFare() {
        return fare;
    }

    public void setFare(Float fare) {
        this.fare = fare;
    }
}