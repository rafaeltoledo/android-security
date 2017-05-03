package net.rafaeltoledo.security.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

public class SignatureUtils {

    private static final String TAG = SignatureUtils.class.getSimpleName();

    // Not a good choice to hardcode this
    private static final String SIGNATURE = "???";

    public static boolean checkSignature(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest sha = MessageDigest.getInstance("SHA");
                sha.update(signature.toByteArray());
                final String currentSignature = Base64.encodeToString(sha.digest(), Base64.DEFAULT);
                if (SIGNATURE.equals(currentSignature)) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to check signature", e);
        }

        return false;
    }
}
