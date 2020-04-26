package com.example.reach2patient;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    private Integer id;

    private int age;

    private long phone;

    private String city, date;

    public Test(int age, long phone, String city) {
        this.age = age;
        this.phone = phone;
        this.city = city;

        Date dateObj = new Date();
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(dateObj);
    }

    public Integer getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public long getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }
}
