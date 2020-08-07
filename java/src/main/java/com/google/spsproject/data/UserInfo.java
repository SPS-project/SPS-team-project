package com.google.spsproject.data;

public class UserInfo {
    private String name;
    private String email;
    private int age;
    private String gender;
    private boolean exists;
    public UserInfo() {
        
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public boolean getExists() {
        return exists;
    }
    public void setExists(boolean exists) {
        this.exists = exists;
    }
}