package com.gamesparks.client.core;

import java.util.HashMap;
import java.util.Map;

import com.gamesparks.client.core.net.FileDownloader;
import com.gamesparks.client.core.net.FileUploader;

public abstract class GSApiAdvancedMethods extends GSApiConvenienceMethods {

    /**
     * Records the end of the current users active session. The SDK will automatically create a new session ID when the app is started, this method can be
     * useful to call when the app goes into the background to allow reporting on player session length.
     * 
     * @return a response Map
     */
    public Map<String, Object> endSession() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("@class", ".EndSessionRequest");
        Map<String, Object> response = connectorClient.send(data);
        connectorClient.endSession();
        return response;
    }

    /**
     * Allows a device id to be used to create an anonymous profile in the game. This allows the player to be tracked and have data stored against them before
     * using FacebookConnectRequest to create a full profile. DeviceAuthenticationRequest should not be used in conjunction with RegistrationRequest as the two
     * accounts will not be merged.
     * 
     * @return a {@link GSSender} object
     */
    public GSSender deviceAuthenticationRequest() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("@class", ".DeviceAuthenticationRequest");
        data.put("deviceId", connectorClient.getDeviceId());
        data.put("deviceOS", getPlatform());
        return getSender(data);
    }

    /**
     * Starts a timed analyser. Only one instance of a key can exists in a given session. If the same key is started twice without a endTimer call being made
     * the first one is deemed to be abandoned.
     * 
     * @param key
     * @param data
     * @return a {@link GSSender} object
     */
    public GSSender startTimer(String key, Map<String, Object> data) {
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("@class", ".AnalyticsRequest");
        requestData.put("key", key);
        requestData.put("start", true);
        requestData.put("data", data);
        return getSender(requestData);
    }

    /**
     * Ends a timed analyser. If no analyser has been started with this key the method returns without failing.
     * 
     * @param key
     * @param data
     * @return a {@link GSSender} object
     */
    public GSSender endTimer(String key, Map<String, Object> data) {
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("@class", ".AnalyticsRequest");
        requestData.put("key", key);
        requestData.put("end", true);
        requestData.put("data", data);
        return getSender(requestData);
    }

    /**
     * Download a file via its uploadId.
     * <p/>
     * The response will contain a byte array against a key called 'bytes'.
     * 
     * @param uploadId
     * @return a {@link GSSender} object
     */
    public GSSender getUploadedFile(String uploadId) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("@class", ".GetUploadedRequest");
        data.put("uploadId", uploadId);
        FileDownloader downloader = new FileDownloader(connectorClient.getPlatformAbstractionLayer());
        return getSender(data, downloader);
    }

    /**
     * Upload binary player data to the GameSparks platform.
     * 
     * @param file the binary data
     * @param fileName a name
     * @param metadata any metadata you wish to be stored with the request
     * @return
     */
    public GSSender uploadFile(byte[] file, String fileName, Map<String, Object> metadata) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("@class", ".GetUploadUrlRequest");
        if ((metadata != null) && (!metadata.isEmpty())) {
            data.put("uploadData", metadata);
        }
        FileUploader uploader = new FileUploader(file, fileName, connectorClient.getPlatformAbstractionLayer());
        return getSender(data, uploader);
    }

    /**
     * Download a file via its short code.
     * <p/>
     * The response will contain a byte array against a key called 'bytes'.
     * 
     * @param uploadId
     * @return a {@link GSSender} object
     */
    public GSSender getDownloadableFile(String shortCode) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("@class", ".GetDownloadableRequest");
        data.put("shortCode", shortCode);
        FileDownloader downloader = new FileDownloader(connectorClient.getPlatformAbstractionLayer());
        return getSender(data, downloader);
    }

}
