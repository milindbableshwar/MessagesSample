package com.milin.messages.model;

import com.milin.messages.model.pojo.Attachment;
import com.milin.messages.model.pojo.Message;
import com.milin.messages.model.pojo.User;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class CompleteMessage {
    @Embedded
    public Message message;

    @Relation(parentColumn =  "userId", entityColumn = "id")
    public List<User> users;

    @Relation(parentColumn = "id", entityColumn = "messageId")
    public List<Attachment> attachments;
}
