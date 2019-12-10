package com.sociality.Models;

public class Events {

    String event_key, description, incharge_person, incharge_email_id, website, contact_number, donation_web_page, volunteer_members, join_hands_members, user_id, image_one, image_two, image_three, event_name, event_date;
    int image_count;
    String timestamp;

    public Events() {
    }

    public Events(String event_key, String description, String incharge_person, String incharge_email_id, String website, String contact_number, String donation_web_page, String volunteer_members, String join_hands_members, String user_id, String image_one, String image_two, String image_three, String event_name, String event_date, int image_count, String timestamp) {
        this.event_key = event_key;
        this.description = description;
        this.incharge_person = incharge_person;
        this.incharge_email_id = incharge_email_id;
        this.website = website;
        this.contact_number = contact_number;
        this.donation_web_page = donation_web_page;
        this.volunteer_members = volunteer_members;
        this.join_hands_members = join_hands_members;
        this.user_id = user_id;
        this.image_one = image_one;
        this.image_two = image_two;
        this.image_three = image_three;
        this.event_name = event_name;
        this.event_date = event_date;
        this.image_count = image_count;
        this.timestamp = timestamp;
    }

    public String getEvent_key() {
        return event_key;
    }

    public void setEvent_key(String event_key) {
        this.event_key = event_key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIncharge_person() {
        return incharge_person;
    }

    public void setIncharge_person(String incharge_person) {
        this.incharge_person = incharge_person;
    }

    public String getIncharge_email_id() {
        return incharge_email_id;
    }

    public void setIncharge_email_id(String incharge_email_id) {
        this.incharge_email_id = incharge_email_id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getDonation_web_page() {
        return donation_web_page;
    }

    public void setDonation_web_page(String donation_web_page) {
        this.donation_web_page = donation_web_page;
    }

    public String getVolunteer_members() {
        return volunteer_members;
    }

    public void setVolunteer_members(String volunteer_members) {
        this.volunteer_members = volunteer_members;
    }

    public String getJoin_hands_members() {
        return join_hands_members;
    }

    public void setJoin_hands_members(String join_hands_members) {
        this.join_hands_members = join_hands_members;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_one() {
        return image_one;
    }

    public void setImage_one(String image_one) {
        this.image_one = image_one;
    }

    public String getImage_two() {
        return image_two;
    }

    public void setImage_two(String image_two) {
        this.image_two = image_two;
    }

    public String getImage_three() {
        return image_three;
    }

    public void setImage_three(String image_three) {
        this.image_three = image_three;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public int getImage_count() {
        return image_count;
    }

    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}