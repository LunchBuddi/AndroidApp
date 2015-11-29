package com.gamesparks.client.android;

import android.app.Application;
import android.content.BroadcastReceiver;

import com.gamesparks.client.core.ConnectorClient;
import com.gamesparks.client.core.GSApiAdvancedMethods;
import com.gamesparks.client.core.PlatformAbstractionLayer;
import com.gamesparks.client.core.WebSocketAbstractionLayer;
import com.gamesparks.client.core.WebSocketListener;

/**
 * A type of GSApi that builds an Android specific {@link PlatformAbstractionLayer} and {@link WebSocketAbstractionLayer} and provides them to the
 * {@link ConnectorClient}.
 * 
 * @author nick redshaw
 */
public class GameSparksAndroidApi extends GSApiAdvancedMethods {

    private static GameSparksAndroidApi gsAndroidApi;

    private AndroidPlatform androidPlatform;

    /**
     * Create the Singleton instance of the GSAndroidApi.
     * 
     * @param url
     * @param apiSecret
     * @param context
     */
    public static void initialise(String url, String apiSecret, Application context) {

        if (gsAndroidApi == null) {
            gsAndroidApi = new GameSparksAndroidApi(url, apiSecret, context);
        }
    }

    /**
     * Use this method to obtain a reference to the singleton instance of the GSAndroidApi.
     * 
     * @return the GSAndroidApi instance
     */
    public static GameSparksAndroidApi getInstance() {
        if (gsAndroidApi == null) {
            throw new RuntimeException("The GameSparks API has not yet been initialised");
        }

        return gsAndroidApi;
    }

    /**
     * Private constructor as this class is a Singleton.
     */
    private GameSparksAndroidApi(String url, String apiSecret, Application context) {
        androidPlatform = new AndroidPlatform(context);
        connectorClient.setPlatformAbstractionLayer(androidPlatform);
        connectorClient.initialise(apiSecret, url);
    }

    @Override
    public WebSocketAbstractionLayer createWebSocket(String url, WebSocketListener webSocketListener) {
        return new AndroidWebSocket(url, webSocketListener);
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return androidPlatform;
    }

    @Override
    public String getPlatform() {
        return "ANDROID";
    }

}
