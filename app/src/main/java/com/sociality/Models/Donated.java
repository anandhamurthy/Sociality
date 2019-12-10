package com.sociality.Models;

public class Donated {

    String donation_key, user_id, event_user_id, event_id;
    boolean accept;

    public Donated() {
    }

    public Donated(String donation_key, String user_id, String event_user_id, String event_id, boolean accept) {
        this.donation_key = donation_key;
        this.user_id = user_id;
        this.event_user_id = event_user_id;
        this.event_id = event_id;
        this.accept = accept;
    }

    public String getDonation_key() {
        return donation_key;
    }

    public void setDonation_key(String donation_key) {
        this.donation_key = donation_key;
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
