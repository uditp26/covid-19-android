package com.example.reach2patient;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Post {

    private Integer id;

    @SerializedName("content")
    private String body;

    private String email;

    private long phone;

    private String city;

    private String state;

    private String country;

    private String time;

    public Post(String body, String email, long phone, String city, String state, String country) {
        this.body = body;
//        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.country = country;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        this.time = formatter.format(date);

    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getEmail() {
        return email;
    }

    public long getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getTime() {
        return time;
    }
}
