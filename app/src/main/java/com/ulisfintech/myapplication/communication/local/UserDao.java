package com.ulisfintech.myapplication.communication.local;

import androidx.room.Dao;
import androidx.room.Query;

import com.ulisfintech.myapplication.models.UserBean;

import java.util.List;

@Dao
public interface UserDao extends BaseDao<UserBean>{

    @Query("SELECT * FROM " + AppConstants.TABLE_USER)
    List<UserBean> getAll();

}
