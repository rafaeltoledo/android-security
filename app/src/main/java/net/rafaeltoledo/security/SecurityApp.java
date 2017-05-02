package net.rafaeltoledo.security;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import net.sqlcipher.database.SQLiteDatabase;

public class SecurityApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        SQLiteDatabase.loadLibs(this);
    }
}
