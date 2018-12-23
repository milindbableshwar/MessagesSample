package com.milin.messages.model.pojo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index("messageId"))
public class Attachment {
    @PrimaryKey
    @NonNull public String id;
    public long messageId;
    public String title;
    public String url;
    public String thumbnailUrl;
}
