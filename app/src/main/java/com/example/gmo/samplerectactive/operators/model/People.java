package com.example.gmo.samplerectactive.operators.model;

/**
 * Created by GMO on 4/9/2018.
 */

public class People {
    private String name;
    private String email;
    private String gender;
    private Address address;

    public People(String name, String email, String gender, Address address) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.address = address;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + ", Email: " + this.email + ", Gender: " + this.gender;
    }
}
