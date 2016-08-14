package com.example.rotem.beats.Model.ModelSql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rotem.beats.Model.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rotem on 09/08/2016.
 */

public class UsersSql {
    final static String USER_TABLE = "users";
    final static String USER_TABLE_ID = "_id";
    final static String USER_TABLE_NAME = "name";
    final static String USER_TABLE_EMAIL = "email";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE + " (" +
                USER_TABLE_ID + " TEXT PRIMARY KEY," +
                USER_TABLE_NAME + " TEXT," +
                USER_TABLE_EMAIL + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + USER_TABLE + ";");
    }

    public static List<User> getAllUsers(SQLiteDatabase db) {
        Cursor cursor = db.query(USER_TABLE, null, null , null, null, null, null);
        List<User> users = new LinkedList<User>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(USER_TABLE_ID);
            int nameIndex = cursor.getColumnIndex(USER_TABLE_NAME);
            int emailIndex = cursor.getColumnIndex(USER_TABLE_EMAIL);
            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String email = cursor.getString(emailIndex);
                User user = new User(id,name,email);
                users.add(user);
            } while (cursor.moveToNext());
        }
        return users;
    }

    public static User getUserById(SQLiteDatabase db, String id) {
        String where = USER_TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(USER_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(USER_TABLE_ID);
            int nameIndex = cursor.getColumnIndex(USER_TABLE_NAME);
            int emailIndex = cursor.getColumnIndex(USER_TABLE_EMAIL);
            String _id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String email = cursor.getString(emailIndex);
            User user = new User(_id,name,email);
            return user;
        }
        return null;
    }

    public static void add(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();
        values.put(USER_TABLE_ID, user.getId());
        values.put(USER_TABLE_NAME, user.getName());
        values.put(USER_TABLE_EMAIL, user.getEmail());
        db.insertWithOnConflict(USER_TABLE, USER_TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static String getLastUpdateDate(SQLiteDatabase db) {
        return LastUpdateSql.getLastUpdate(db, USER_TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db, String date) {
        LastUpdateSql.setLastUpdate(db, USER_TABLE, date);
    }
}
