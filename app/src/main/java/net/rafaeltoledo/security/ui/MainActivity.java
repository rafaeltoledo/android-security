package net.rafaeltoledo.security.ui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;
import com.scottyab.rootbeer.RootBeer;

import net.rafaeltoledo.security.R;
import net.rafaeltoledo.security.databinding.ActivityMainBinding;
import net.rafaeltoledo.security.http.HttpClientProvider;
import net.rafaeltoledo.security.http.Tls12SslSocketFactory;
import net.rafaeltoledo.security.util.EnvironmentChecker;
import net.rafaeltoledo.security.util.InstallationChecker;
import net.rafaeltoledo.security.util.SignatureUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Random;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, MainController {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleApiClient client;
    private final Random random = new SecureRandom();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        client = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .enableAutoManage(this, this)
                .build();

        binding.root.setText(new RootBeer(this).isRooted() ? "Device is rooted" : "Device isn't rooted");
        binding.installation.setText(InstallationChecker.verifyInstaller(this) ? "Installed from Play Store" : "Installed from unknown source");

        binding.enviroment.setText((EnvironmentChecker.alternativeIsEmulator() ? "Running on an emulator" : "Running on a device")
                + (EnvironmentChecker.isDebuggable(this) ? " with debugger" : ""));

        binding.tampering.setText((InstallationChecker.checkPackage(this) ?
                "The package is consistent" : "The package was modified")
                + (SignatureUtils.checkSignature(this) ? " and the signature is ok" : " and the signature was changed!"));

        binding.setController(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Failed to connect to Google Client");
    }

    @Override
    public void requestSafetyNetCheck() {
        byte[] nonce = getRequestNonce();
        SafetyNet.SafetyNetApi.attest(client, nonce)
                .setResultCallback(result -> {
                    if (result.getStatus().isSuccess()) {
                        showSafetyNetResult(result.getJwsResult());
                    } else {
                        Log.e(TAG, "Error on SafetyNet request - Code ("
                                + result.getStatus().getStatusCode() + "): " +
                                "" + result.getStatus().getStatusMessage());
                    }
                });
    }

    @Override
    public void performOkHttpRequest() {
        OkHttpClient client = HttpClientProvider.getClient();

        Request request = new Request.Builder()
                .url("https://github.com/robots.txt")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to perform request", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().string());
            }
        });
    }

    private void showSafetyNetResult(String result) {
        /*
           Forward this result to your server together with the nonce for verification.
           You can also parse the JwsResult locally to confirm that the API
           returned a response by checking for an 'error' field first and before
           retrying the request with an exponential backoff.
           NOTE: Do NOT rely on a local, client-side only check for security, you
           must verify the response on a remote server!
        */
        Log.d(TAG, "Success! SafetyNet result:\n" + result + "\n");
    }

    // Ideally, the Nonce should be generated in your server
    private byte[] getRequestNonce() {
        String nonceData = "SafetyNet Sample: " + System.currentTimeMillis();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        try {
            byteStream.write(bytes);
            byteStream.write(nonceData.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "Failed to generate nonce", e);
            return null;
        }
        return byteStream.toByteArray();
    }
}
