package com.example.reach2patient;

public class RecyclerItem {
    private String post;
    private String timestamp;
    private String contact;

    public RecyclerItem(String post, String timestamp, String contact) {
        this.post = post;
        this.timestamp = timestamp;
        this.contact = contact;
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
