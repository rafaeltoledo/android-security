package net.rafaeltoledo.security;

import android.app.Application;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import net.sqlcipher.database.SQLiteDatabase;

public class SecurityApp extends Application {

    private static final String TAG = SecurityApp.class.getSimpleName();
    private static final String PREF_TIMES = "net.rafaeltoledo.security.PREF_TIMES";

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        SQLiteDatabase.loadLibs(this);

        int times = Hawk.get(PREF_TIMES, 0) + 1;
        Log.d(TAG, "Psss! Let me tell a secret: you opened this app " + times + " times.");
        Hawk.put(PREF_TIMES, times);
    }
}
