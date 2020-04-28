package com.example.tigeritemployee;

import android.view.Display;

public class Model {

    private int id;
    private String name;
    private String gender;
    private String age;
    private byte[] image;

    public Model(int id, String name, String gender, String age, byte[] image){
        this.id=id;
        this.name=name;
        this.gender=gender;
        this.age=age;
        this.image=image;

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}