package com.milin.messages.model.pojo;

import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages",
        indices = {@Index("id"), @Index("userId")}
)
public class Message {
    @PrimaryKey
    public long id;
    public long userId;
    public String content;
    @Ignore
    public List<Attachment> attachments;
}
