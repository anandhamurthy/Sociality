package com.sociality.Models;

public class Volunteer {

    String vol_key, user_id, event_user_id, event_id;
    boolean accept;


    public Volunteer() {
    }

    public Volunteer(String vol_key, String user_id, String event_user_id, String event_id, boolean accept) {
        this.vol_key = vol_key;
        this.user_id = user_id;
        this.event_user_id = event_user_id;
        this.event_id = event_id;
        this.accept = accept;
    }

    public String getVol_key() {
        return vol_key;
    }

    public void setVol_key(String vol_key) {
        this.vol_key = vol_key;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEvent_user_id() {
        return event_user_id;
    }

    public void setEvent_user_id(String event_user_id) {
        this.event_user_id = event_user_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
