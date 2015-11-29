package com.gamesparks.client.core;

import java.util.Map;

/**
 * GameSparks service API.
 * 
 * @author nick redshaw
 * 
 */
public abstract class GSApi {

    protected static ConnectorClient connectorClient;
    protected boolean paused = false;

    public GSApi() {
        connectorClient = new ConnectorClient(this);
    }

    /**
     * Extending classes should provide a platform specific WebSocket implementation of {@link WebSocketAbstractionLayer}.
     * 
     * @param url
     * @param webSocketListener
     * @return an implementation of WebSocketAbstractionLayer
     */
    public abstract WebSocketAbstractionLayer createWebSocket(String url, WebSocketListener webSocketListener);

    /**
     * Extending classes should provide a platform specific String.
     * 
     * @return e.g ANDROID or IOS
     */
    public abstract String getPlatform();

    /**
     * Generic, blocking send method that allows arbitrary data to be passed to the GameSparks API.
     * 
     * @param jsonData
     * @return a response Map
     */
    public Map<String, Object> send(Map<String, Object> jsonData) {
        return connectorClient.send(jsonData);
    }

    /**
     * Generic, non-blocking send method that allows arbitrary data to be passed to the GameSparks API.
     * 
     * @param jsonData
     * @return a response Map
     */
    public Map<String, Object> sendNoWait(Map<String, Object> jsonData) {
        return connectorClient.sendNoWait(jsonData);
    }

    /**
     * Get a {@link GSSender} object for asynchronous sending.
     * 
     * @param data
     * @return GameSparksSender
     */
    public GSSender getSender(Map<String, Object> data) {
        return connectorClient.getPlatformAbstractionLayer().getSender(connectorClient, data);
    }

    protected GSSender getSender(Map<String, Object> data, Completer completer) {
        return connectorClient.getPlatformAbstractionLayer().getSender(connectorClient, data, completer);
    }

    /**
     * Connect to the GameSparks service.
     */
    public void connect() {
        connectorClient.connect();
    }

    /**
     * Disconnect from the GameSparks service.
     */
    public void disconnect() {
        connectorClient.disconnect();
    }

    /**
     * Register to receive messages for the GameSparks platform.
     * 
     * @param messageRecievedListener
     */
    public void setMessageRecievedListener(MessageRecievedListener messageRecievedListener) {
        connectorClient.setMessageRecievedListener(messageRecievedListener);
    }

    /**
     * Call this when the hosting Android application receives the onPause call.
     */
    public void onPause() {
        if (!paused) {
            paused = true;
            endSession();
        }
    }

    protected abstract Map<String, Object> endSession();

    /**
     * Call this when the hosting Android application receives the onResume call.
     */
    public void onResume() {
        if (paused) {
            paused = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    connectorClient.connect();
                }
            }).start();
        }
    }

    /**
     * Register to receive SDK connected messages.
     * 
     * @param SdkConnectedListener
     */
    public void setSdkConnectedListener(SdkConnectedListener sdkConnectedListener) {
        connectorClient.setSdkConnectedListener(sdkConnectedListener);
    }

    /**
     * Is the SDK in an AUTHORISED state and has a valid authToken (not null or '0').
     * 
     * @return true if the SDK is authenticated
     */
    public boolean isAuthenticated() {
        return connectorClient.isAuthenticated();
    }

    /**
     * Is the SDK in the AUTHORISED state.
     * 
     * @return true if SDK is in AUTHORISED state
     */
    public boolean isAvailable() {
        return connectorClient.isAuthorised();
    }

    public static ConnectorClient getConnectorClient() {
        return connectorClient;
    }
}
