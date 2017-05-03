package net.rafaeltoledo.security.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;

public class EnvironmentChecker {

    private static final String TAG = EnvironmentChecker.class.getSimpleName();

    private static String getSystemProperty(String name) throws Exception {
        Class systemPropertyClass = Class.forName("android.os.SystemProperties");
        return (String) systemPropertyClass.getMethod("get", new Class[]{String.class})
                .invoke(systemPropertyClass, name);
    }

    public static boolean alternativeIsEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || "goldfish".equals(Build.HARDWARE);
    }

    public static boolean isEmulator() {
        try {
            boolean goldfish = getSystemProperty("ro.hardware").contains("goldfish");
            boolean emu = getSystemProperty("ro.kernel.qemu").length() > 0;
            boolean sdk = getSystemProperty("ro.product.model").equals("sdk");

            if (emu || goldfish || sdk) {
                return true;
            }

        } catch (Exception e) {
            Log.d(TAG, "Failed to check enviroment", e);
        }

        return false;
    }

    public static boolean isDebuggable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}
