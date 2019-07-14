package com.lco.instagram;

public class User {
    public String id;
    public String name;
    public String email;
    public  String url;
    public String username;
    public  String password;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }






    public User(String id, String name, String email,String url,String password,String userName) {
        this.id = id;
        this.name = name;
        this.email=email;
        this.url=url;
        this.username=userName;
        this.password=password;
    }
}
