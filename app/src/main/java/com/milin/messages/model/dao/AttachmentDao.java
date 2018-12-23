package com.milin.messages.model.dao;

import com.milin.messages.model.pojo.Attachment;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface AttachmentDao {

    @Insert
    void insert(Attachment attachment);

    @Delete
    void delete(Attachment attachment);

}
