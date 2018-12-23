package com.milin.messages.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;
import com.milin.messages.MessagesApplication;
import com.milin.messages.model.pojo.Attachment;
import com.milin.messages.model.pojo.Message;
import com.milin.messages.model.pojo.MessagesAndUsersWrapper;
import com.milin.messages.model.pojo.User;

import java.io.IOException;
import java.io.InputStream;

import androidx.paging.DataSource;

public class MessagesModel {

    private static final String PREF_NAME = "prefs";
    private static final String KEY_MESSAGES_FETCHED = "messages_fetched";

    private MessagesDb messagesDb;

    public MessagesModel(Context context) {
        messagesDb = MessagesDb.getMessagesDatabase(context);
    }

    public void fetchMessages(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean(KEY_MESSAGES_FETCHED, false)) {
            MessagesApplication.executeOnIoThread(() -> {
                MessagesAndUsersWrapper wrapper = new GsonBuilder().create().fromJson(loadJSONFromAsset(context), MessagesAndUsersWrapper.class);
                for (User user : wrapper.users) {
                    messagesDb.userDao().insert(user);
                }
                for (Message message : wrapper.messages) {
                    messagesDb.messageDao().insert(message);
                    if(message.attachments != null) {
                        for (Attachment attachment : message.attachments) {
                            attachment.messageId = message.id;
                            messagesDb.attachmentDao().insert(attachment);
                        }
                    }
                }
            });
            sharedPreferences.edit().putBoolean(KEY_MESSAGES_FETCHED, true).apply();
        }
    }

    public DataSource.Factory<Integer, CompleteMessage> getMessagesDataSource() {
        return messagesDb.messageDao().getMessages();
    }

    public void addMessage(Message message) {
        MessagesApplication.executeOnIoThread(() -> {
            messagesDb.messageDao().insert(message);
            if(message.attachments != null) {
                for(Attachment attachment: message.attachments) {
                    messagesDb.attachmentDao().insert(attachment);
                }
            }
        });
    }

    public void removeMessage(Message message) {
        MessagesApplication.executeOnIoThread(() -> {
            messagesDb.messageDao().delete(message);
            if(message.attachments != null) {
                for(Attachment attachment: message.attachments) {
                    messagesDb.attachmentDao().insert(attachment);
                }
            }
        });
    }

    public void removeAttachment(Attachment attachment) {
        MessagesApplication.executeOnIoThread(() -> messagesDb.attachmentDao().delete(attachment));
    }

    public void addAttachment(Attachment attachment) {
        MessagesApplication.executeOnIoThread(() -> messagesDb.attachmentDao().insert(attachment));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
