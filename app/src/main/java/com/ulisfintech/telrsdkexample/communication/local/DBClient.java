package com.ulisfintech.telrsdkexample.communication.local;

import androidx.room.Room;

import com.ulisfintech.telrsdkexample.TelrPayApp;

public class DBClient {

    private static DBClient client = null;
    private static final String DATABASE_NAME = "ulisdb";
    public static AppDatabase db;

    public static DBClient newInstance() {
        if (client == null) client = new DBClient();
        return client;
    }

    public DBClient() {
        db = Room.databaseBuilder(TelrPayApp.context, AppDatabase.class, DATABASE_NAME).build();
    }

    public AppDatabase db() {
        return  db;
    }
}
