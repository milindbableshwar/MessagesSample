package com.milin.messages.model;

import android.content.Context;

import com.milin.messages.model.dao.AttachmentDao;
import com.milin.messages.model.dao.MessageDao;
import com.milin.messages.model.dao.UserDao;
import com.milin.messages.model.pojo.Attachment;
import com.milin.messages.model.pojo.Message;
import com.milin.messages.model.pojo.User;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Message.class, Attachment.class}, version = 1, exportSchema = false)
abstract class MessagesDb extends RoomDatabase {

    private static MessagesDb INSTANCE;

    public static MessagesDb getMessagesDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MessagesDb.class, "messages-database")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract MessageDao messageDao();
    public abstract UserDao userDao();
    public abstract AttachmentDao attachmentDao();
}

