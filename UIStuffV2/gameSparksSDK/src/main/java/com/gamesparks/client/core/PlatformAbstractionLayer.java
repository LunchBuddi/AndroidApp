package com.gamesparks.client.core;

import java.util.Map;

/**
 * Describes the platform support required by the core GameSparks client classes.
 * 
 * @author nick redshaw
 * 
 */
public interface PlatformAbstractionLayer {

    String DEVICE_ID = "PlatformAbstractionLayerDeviceId";
    String OS_VERSION = "PlatformAbstractionLayerOsVersion";

    String getProperty(String key, String defaultValue);

    void setProperty(String key, String value);

    boolean isNetworkAvailable();

    void registerForNetworkChangeEvents(ConnectivityChangedListener connectivityChangedListener);

    void logError(String message);

    void logError(String message, Throwable e);

    void logDebug(String message);

    void logDebug(String message, Exception error);

    String base64EncodeToString(byte[] sha);

    void handleAsyncResult(Map<String, Object> result, GSListener gameSparksReceiver);

    GSSender getSender(ConnectorClient connectorClient, Map<String, Object> data);

    GSSender getSender(ConnectorClient connectorClient, Map<String, Object> data, Completer completer);

}
