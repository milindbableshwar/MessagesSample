package com.milin.messages;

import android.app.Application;

import java.util.concurrent.Executors;

public class MessagesApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void executeOnIoThread(Runnable runnable) {
        Executors.newSingleThreadExecutor().execute(runnable);
    }
}
