package com.e.demoproject;

import java.sql.Blob;

public class UserModel {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String HOBBY = "hobby";
    public static final String POST = "post";
    public static final String GENDER = "gender";
    public static final String IMAGES = "images";
    private int id;
    private String name;
    private String hobby;
    private String post;
    private String gender;
    private String images;


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + GENDER + " TEXT,"
                    + HOBBY + " TEXT,"
                    + POST + " TEXT,"
                    + IMAGES + " TEXT"
                    + ")";

    public UserModel() {
    }

    public UserModel(int id, String name, String gender, String post, String hobby, String images) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.post = post;
        this.hobby = hobby;
        this.images = images;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
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

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}