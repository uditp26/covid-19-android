package com.example.reach2patient;

//import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Donate {

    private Integer id;

    private int age;

    @SerializedName("recovercovid")
    private Boolean recoveryStatus;

    @SerializedName("bloodgroup")
    private String bloodGroup;

    private long phone;

    private String city, date;

    public Donate(int age, Boolean recoveryStatus, String bloodGroup, long phone, String city) {
        this.age = age;
        this.recoveryStatus = recoveryStatus;
        this.bloodGroup = bloodGroup;
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

    public Boolean getRecoveryStatus() {
        return recoveryStatus;
    }

    public String getBloodGroup() {
        return bloodGroup;
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

