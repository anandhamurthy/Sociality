package com.sociality.Models;

public class Joined {

    String  join_key, user_id, event_user_id, event_id;
    boolean accept;

    public Joined() {
    }

    public Joined(String join_key, String user_id, String event_user_id, String event_id, boolean accept) {
        this.join_key = join_key;
        this.user_id = user_id;
        this.event_user_id = event_user_id;
        this.event_id = event_id;
        this.accept = accept;
    }

    public String getJoin_key() {
        return join_key;
    }

    public void setJoin_key(String join_key) {
        this.join_key = join_key;
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

    public boolean getAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
