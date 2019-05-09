package com.e.demoproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "notes_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(UserModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserModel.TABLE_NAME);

        onCreate(db);
    }

    public long insertNote(String name, String mGender, String post, String hobby, String images) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserModel.COLUMN_NAME, name);
        values.put(UserModel.GENDER, mGender);
        values.put(UserModel.POST, post);
        values.put(UserModel.HOBBY, hobby);
        values.put(UserModel.IMAGES, images);

        long id = db.insert(UserModel.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public UserModel getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(UserModel.TABLE_NAME,
                new String[]{UserModel.COLUMN_ID, UserModel.COLUMN_NAME, UserModel.GENDER, UserModel.POST, UserModel.HOBBY,UserModel.IMAGES},
                UserModel.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        UserModel userModel = new UserModel(
                cursor.getInt(cursor.getColumnIndex(UserModel.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(UserModel.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(UserModel.GENDER)),
                cursor.getString(cursor.getColumnIndex(UserModel.POST)),
                cursor.getString(cursor.getColumnIndex(UserModel.HOBBY)),
                cursor.getString(cursor.getColumnIndex(UserModel.IMAGES)));

        cursor.close();

        return userModel;
    }

    public List<UserModel> getAllNotes() {
        List<UserModel> userModels = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + UserModel.TABLE_NAME + " ORDER BY " +
                UserModel.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setId(cursor.getInt(cursor.getColumnIndex(UserModel.COLUMN_ID)));
                userModel.setName(cursor.getString(cursor.getColumnIndex(UserModel.COLUMN_NAME)));
                userModel.setGender(cursor.getString(cursor.getColumnIndex(UserModel.GENDER)));
                userModel.setPost(cursor.getString(cursor.getColumnIndex(UserModel.POST)));
                userModel.setHobby(cursor.getString(cursor.getColumnIndex(UserModel.HOBBY)));
                userModel.setImages(cursor.getString(cursor.getColumnIndex(UserModel.IMAGES)));

                userModels.add(userModel);
            } while (cursor.moveToNext());
        }

        db.close();

        return userModels;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + UserModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }


}