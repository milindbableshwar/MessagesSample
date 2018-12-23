package com.milin.messages.model.dao;

import com.milin.messages.model.CompleteMessage;
import com.milin.messages.model.pojo.Message;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);

    @Delete
    void delete(Message message);

    @Transaction
    @Query("SELECT * FROM messages")
    DataSource.Factory<Integer, CompleteMessage> getMessages();
}
