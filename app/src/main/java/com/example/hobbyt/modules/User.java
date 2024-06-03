package com.example.hobbyt.modules;


import java.io.Serializable;

public class User implements Serializable {


    private int user_id;
    private String first_name;
    private String last_name;
    private String email;

    public User(int user_id, String first_name, String last_name, String email) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }
}
