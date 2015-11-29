package com.gamesparks.client.android;

import static android.util.Base64.NO_WRAP;

import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import com.gamesparks.client.core.Completer;
import com.gamesparks.client.core.ConnectivityChangedListener;
import com.gamesparks.client.core.ConnectorClient;
import com.gamesparks.client.core.GSListener;
import com.gamesparks.client.core.GSSender;
import com.gamesparks.client.core.PlatformAbstractionLayer;

/**
 * Android implementation of {@link PlatformAbstractionLayer}.
 * 
 * @author nick redshaw
 * 
 */
public class AndroidPlatform extends BroadcastReceiver implements PlatformAbstractionLayer {

    private static final String TAG = "GameSparks SDK";

    private Application context;
    private ConnectivityChangedListener connectivityChangedListener;

    public AndroidPlatform(Application context) {
        this.context = context;
        context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        if (PlatformAbstractionLayer.DEVICE_ID.equals(key)) {
            return getDeviceId();
        } else if (PlatformAbstractionLayer.OS_VERSION.equals(key)) {
            return getOsVersion();
        }

        return context.getSharedPreferences("GS_API_PREFS", Activity.MODE_PRIVATE).getString(key, defaultValue);
    }

    @Override
    public void setProperty(String key, String value) {
        Editor editor = context.getSharedPreferences("GS_API_PREFS", Activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        boolean success = editor.commit();
        if (!success) {
            Log.e(TAG, "Could not save authToken to shared preferences");
        }
    }

    private String getDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private String getOsVersion() {
        return "Android OS " + android.os.Build.VERSION.RELEASE + " / API-" + String.valueOf(android.os.Build.VERSION.SDK_INT);
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        connectivityChangedListener.onConnectivityChange(isNetworkAvailable());
    }

    @Override
    public void registerForNetworkChangeEvents(ConnectivityChangedListener connectivityChangedListener) {
        this.connectivityChangedListener = connectivityChangedListener;
    }

    @Override
    public void logError(String message) {
        Log.e(TAG, message);
    }

    @Override
    public void logError(String message, Throwable e) {
        Log.e(TAG, message, e);
    }

    @Override
    public void logDebug(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void logDebug(String message, Exception error) {
        Log.d(TAG, message, error);
    }

    @Override
    public String base64EncodeToString(byte[] sha) {
        return Base64.encodeToString(sha, NO_WRAP);
    }

    @Override
    public void handleAsyncResult(Map<String, Object> result, GSListener gsListener) {
        AndroidActivityGSListener androidActivityGSListener = (AndroidActivityGSListener) gsListener;
        androidActivityGSListener.onMessage(result);
    }

    @Override
    public GSSender getSender(ConnectorClient connectorClient, Map<String, Object> data) {
        return new GSAndroidSender(connectorClient, data);
    }

    @Override
    public GSSender getSender(ConnectorClient connectorClient, Map<String, Object> data, Completer completer) {
        return new GSAndroidSender(connectorClient, data, completer);
    }
}
