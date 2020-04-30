package com.example.reach2patient;

import com.google.gson.annotations.SerializedName;

public class Delete {

    private Integer id;

    @SerializedName("hospitalname")
    private String hospitalName;

    @SerializedName("bloodgroup")
    private String bloodGroup;

    private long phone;

    private String city, date;

    public Delete(String hospitalName, String bloodGroup, long phone, String city, String date) {
        this.hospitalName = hospitalName;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.city = city;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public String getHospitalName() {
        return hospitalName;
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
