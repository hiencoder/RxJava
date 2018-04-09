package com.example.gmo.samplerectactive.operators;

/**
 * Created by GMO on 4/6/2018.
 */

public class User {
    private int id;
    private String name;
    private String gender;

    public User(int id, String name, String gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Id: " + this.id + "\nName: " + this.name + "\n" + "Gender: " + this.gender;
    }

    /*Để sử dụng distinct() cần override các phương thức*/

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }
        if (!(obj instanceof User)){
            return false;
        }

        return name.equalsIgnoreCase(((User)obj).getName());
    }
}
