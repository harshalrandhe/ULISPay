package com.ulisfintech.myapplication.communication.local;

import androidx.room.Room;

import com.ulisfintech.myapplication.AurthaApp;

public class DBClient {

    private static DBClient client = null;
    private static final String DATABASE_NAME = "ulisdb";
    public static AppDatabase db;

    public static DBClient newInstance() {
        if (client == null) client = new DBClient();
        return client;
    }

    public DBClient() {
        db = Room.databaseBuilder(AurthaApp.context, AppDatabase.class, DATABASE_NAME).build();
    }

    public AppDatabase db() {
        return  db;
    }
}
