package com.sociality.Models;

public class Notifications {

    String user_id, message, event_id, event_user_id, noti_key;
    boolean is_event, is_tie_up, is_volunteer, is_donation, is_read, accept;

    public Notifications() {
    }

    public Notifications(String user_id, String message, String event_id, String event_user_id, String noti_key, boolean is_event, boolean is_tie_up, boolean is_volunteer, boolean is_donation, boolean is_read, boolean accept) {
        this.user_id = user_id;
        this.message = message;
        this.event_id = event_id;
        this.event_user_id = event_user_id;
        this.noti_key = noti_key;
        this.is_event = is_event;
        this.is_tie_up = is_tie_up;
        this.is_volunteer = is_volunteer;
        this.is_donation = is_donation;
        this.is_read = is_read;
        this.accept = accept;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_user_id() {
        return event_user_id;
    }

    public void setEvent_user_id(String event_user_id) {
        this.event_user_id = event_user_id;
    }

    public String getNoti_key() {
        return noti_key;
    }

    public void setNoti_key(String noti_key) {
        this.noti_key = noti_key;
    }

    public boolean isIs_event() {
        return is_event;
    }

    public void setIs_event(boolean is_event) {
        this.is_event = is_event;
    }

    public boolean isIs_tie_up() {
        return is_tie_up;
    }

    public void setIs_tie_up(boolean is_tie_up) {
        this.is_tie_up = is_tie_up;
    }

    public boolean isIs_volunteer() {
        return is_volunteer;
    }

    public void setIs_volunteer(boolean is_volunteer) {
        this.is_volunteer = is_volunteer;
    }

    public boolean isIs_donation() {
        return is_donation;
    }

    public void setIs_donation(boolean is_donation) {
        this.is_donation = is_donation;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
