package com.ulisfintech.arthavendor.communication.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ulisfintech.arthavendor.models.UserBean;

@Database(entities = {UserBean.class,}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

}