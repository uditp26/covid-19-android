package com.example.reach2patient;

public class RecyclerItem {
    private int id;
    private String post;
    private String timestamp;
    private String contact;

    public RecyclerItem(int id, String post, String timestamp, String contact) {
        this.id = id;
        this.post = post;
        this.timestamp = timestamp;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public String getPost() {
        return post;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getContact() {
        return contact;
    }
}
