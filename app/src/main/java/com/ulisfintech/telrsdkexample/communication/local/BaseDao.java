package com.ulisfintech.telrsdkexample.communication.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import io.reactivex.rxjava3.annotations.NonNull;

@Dao
public interface BaseDao<T> {


    @Insert
    void insert(T t);

    @Insert
    void insertAll(T... t);

    @Delete
    @NonNull
    void delete(T t);

    @Update
    void update(T t);

}
