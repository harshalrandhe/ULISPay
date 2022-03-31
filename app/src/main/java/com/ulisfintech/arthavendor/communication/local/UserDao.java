package com.ulisfintech.arthavendor.communication.local;

import androidx.room.Dao;
import androidx.room.Query;

import com.ulisfintech.arthavendor.models.UserBean;

import java.util.List;

@Dao
public interface UserDao extends BaseDao<UserBean>{

    @Query("SELECT * FROM " + AppConstants.TABLE_USER)
    List<UserBean> getAll();

}
