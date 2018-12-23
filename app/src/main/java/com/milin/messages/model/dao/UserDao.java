package com.milin.messages.model.dao;

import com.milin.messages.model.pojo.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}
