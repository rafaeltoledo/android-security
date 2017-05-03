package net.rafaeltoledo.security.util;

import android.content.Context;

public class InstallationChecker {

    private static final String GOOGLE_PLAY = "com.android.vending";
    private static final String ORIGINAL_PACKAGE = "net.rafaeltoledo.security";

    public static boolean verifyInstaller(Context context) {
        final String installer = context.getPackageManager()
                .getInstallerPackageName(context.getPackageName());
        return installer != null && installer.compareTo(GOOGLE_PLAY) == 0;
    }

    public static boolean checkPackage(Context context) {
        return context.getPackageName().compareTo(ORIGINAL_PACKAGE) == 0;
    }
}
