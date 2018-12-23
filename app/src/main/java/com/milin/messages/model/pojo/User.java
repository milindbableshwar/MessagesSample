package com.milin.messages.model.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    public long id;
    public String name;
    public String avatarId;
}
