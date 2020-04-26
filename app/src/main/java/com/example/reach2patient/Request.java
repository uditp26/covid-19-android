package com.example.reach2patient;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {

    private Integer id;

    private int age;

    @SerializedName("recovercovid")
    private Boolean recoveryStatus;

    @SerializedName("bloodgroup")
    private String bloodGroup;

    private long phone;

    @SerializedName("hospitalphone")
    private long hospitalPhone;

    @SerializedName("email")
    private String hospitalEmail;

    private String city, date;

    public Request(int age, Boolean recoveryStatus, String bloodGroup, long phone, long hospitalPhone, String hospitalEmail, String city) {
        this.age = age;
        this.recoveryStatus = recoveryStatus;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.hospitalPhone = hospitalPhone;
        this.hospitalEmail = hospitalEmail;
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

    public long getHospitalPhone() {
        return hospitalPhone;
    }

    public String getHospitalEmail() {
        return hospitalEmail;
    }

    public String getCity() {
        return city;
    }
}
