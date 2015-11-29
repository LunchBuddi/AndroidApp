package com.gamesparks.client.core;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.gamesparks.client.core.ConnectorStateEngine.ConnectorState;
import com.gamesparks.client.core.ConnectorStateEngine.StateChangeEvent;
import com.gamesparks.client.core.net.OfflineRequestQueue;

/**
 * This class handles the handshake with the GameSparks server and the sending / processing of GameSparks service API requests and responses.
 * 
 * @author nick redshaw
 * 
 */
public class ConnectorClient implements ConnectivityChangedListener {

    private static final int PING_TIME_DELAY_IN_MILLIS = 30000;
    private static final int TIMEOUT_IN_SECONDS = 5;
    private static final int LATCH_COUNT = 1;

    private String serviceUrl;
    private String connectUrl;
    private String sessionId;
    private String secret;
    private String authToken = "0";
    private JSONParser jsonParser = new JSONParser();
    private Map<String, DataReceivedEvent> pendingRequests = new ConcurrentHashMap<String, DataReceivedEvent>(10, 0.75f, 1);
    private Timer pingTimer;
    private PlatformAbstractionLayer platformAbstractionLayer;
    private WebSocketAbstractionLayer webSocket;
    private GSApi gsApi;
    private ConnectorStateEngine connectorStateEngine;
    private CountDownLatch initialisationLatch;
    private MessageRecievedListener messageRecievedListener;
    private SdkConnectedListener sdkConnectedListener;
    private OfflineRequestQueue offlineRequestQueue;

    public ConnectorClient(GSApi gsApi) {
        this.gsApi = gsApi;
        this.connectorStateEngine = new ConnectorStateEngine(this);
    }

    /**
     * Initialise the SDk and connect to the GameSparks service.
     * 
     * @param secret
     * @param gameApiKey
     */
    public void initialise(String secret, String serviceUrl) {

        validateInputs(secret, serviceUrl);
        this.secret = secret;
        this.serviceUrl = serviceUrl;
        authToken = platformAbstractionLayer.getProperty(serviceUrl + ".authToken", "0");
        platformAbstractionLayer.registerForNetworkChangeEvents(this);
        offlineRequestQueue = new OfflineRequestQueue(this);
        connect();
    }

    /**
     * Connect to the GameSparks service.
     */
    public synchronized void connect() {
        platformAbstractionLayer.logDebug("connect");
        initialisationLatch = new CountDownLatch(LATCH_COUNT);
        if ((connectorStateEngine.getConnectionState().equals(ConnectorState.STOPPED)) && (platformAbstractionLayer.isNetworkAvailable())) {
            connectorStateEngine.changeState(StateChangeEvent.NETWORK_AVAILABLE);
            try {
                if (initialisationLatch.await(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)) {
                    notifySdkConnected();
                } else {
                    platformAbstractionLayer.logError("Timeout waiting to connect");
                    connectorStateEngine.changeState(StateChangeEvent.GENERAL_ERROR);
                }

            } catch (InterruptedException e) {
                platformAbstractionLayer.logError(e.getMessage(), e);
            }
        }

        startPingTimer();
    }

    private void validateInputs(String secret, String serviceUrl) {
        if ((secret == null) || ("".equals(secret))) {
            throw new RuntimeException("The game secret cannot be null or empty");
        }

        if ((serviceUrl == null) || ("".equals(serviceUrl))) {
            throw new RuntimeException("The game service URL cannot be null or empty");
        }
    }

    private void startPingTimer() {
        if (pingTimer != null) {
            pingTimer.cancel();
        }
        pingTimer = new Timer();
        pingTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (isAuthorised()) {
                    platformAbstractionLayer.logDebug("ping");
                    webSocket.send("");
                } else if (connectorStateEngine.getConnectionState().equals(ConnectorState.STOPPED) && (platformAbstractionLayer.isNetworkAvailable())) {
                    platformAbstractionLayer.logDebug("In stopped state but network is available so attempting connect...");
                    connect();
                }
            }
        }, PING_TIME_DELAY_IN_MILLIS, PING_TIME_DELAY_IN_MILLIS);

    }

    /**
     * Disconnect from the GameSpark service.
     */
    public void disconnect() {
        pingTimer.cancel();
        connectorStateEngine.enterStoppedState();
    }

    /**
     * Return the device id.
     * 
     * @return deviceId
     */
    public String getDeviceId() {
        return platformAbstractionLayer.getProperty(PlatformAbstractionLayer.DEVICE_ID, "deviceId unavailable");
    }

    /**
     * Send data over the WebSocket and wait for the response.
     * 
     * @param jsonData
     * @return the server response or an error if the SDK was not in the correct state
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> send(Map<String, Object> jsonData) {

        try {
            if (connectorStateEngine.getConnectionState().equals(ConnectorState.VALIDATION_FAILED)) {
                return buildErrorReponse("Validation with GameSpark service failed. Please check the game secret you are using is correct");
            } else if (!isAuthorised()) {
                return buildErrorReponse("GameSparks SDK not ready to send." + " SDK state: " + connectorStateEngine.getConnectionState());
            }

            String requestId = String.valueOf(System.currentTimeMillis());
            jsonData.put("requestId", requestId);
            DataReceivedEvent dataReceivedEvent = new DataReceivedEvent(requestId);
            pendingRequests.put(requestId, dataReceivedEvent);
            sendNoWait(jsonData);
            waitForResponseFromServer(dataReceivedEvent);

            return processResponse(dataReceivedEvent, requestId);
        } catch (Throwable t) {
            platformAbstractionLayer.logError(t.getStackTrace().toString(), t);
            return null;
        }
    }

    private String getOsVersion() {
        return platformAbstractionLayer.getProperty(PlatformAbstractionLayer.OS_VERSION, "OS version unavailable");
    }

    private Map<String, Object> buildErrorReponse(String message) {
        Map<String, Object> error = new HashMap<String, Object>();
        error.put("error", message);
        return error;
    }

    void createWebSocket(String url) {

        if (webSocket != null) {
            webSocket.disconnect();
        }

        webSocket = gsApi.createWebSocket(url, new WebSocketListener() {
            @Override
            public void onConnect() {
                platformAbstractionLayer.logDebug("WebSocket connected");
            }

            @Override
            public void onMessage(String message) {
                processMessage(message);
            }

            @Override
            public void onMessage(byte[] data) {
                platformAbstractionLayer.logError(String.format("Error, got binary message! %s", data));
            }

            @Override
            public void onDisconnect(int code, String reason) {
                if ("EOF".equals(reason)) {
                    connectorStateEngine.changeState(StateChangeEvent.WS_DISCONNECT);
                } else if ("SSL".equals(reason)) {
                    connectorStateEngine.changeState(StateChangeEvent.GENERAL_ERROR);
                } else {
                    platformAbstractionLayer.logDebug("disconnect with unknown reason code: " + reason);
                }
            }

            @Override
            public void onError(Exception error) {
                if (webSocket.isSocketAlive()) {
                    platformAbstractionLayer.logDebug(error.getMessage(), error);
                    connectorStateEngine.changeState(StateChangeEvent.GENERAL_ERROR);
                }

            }
        });

        webSocket.connect();
    }

    private void processMessage(String message) {

        if ((message != null) && (message.length() != 0)) {

            try {
                JSONObject jsonMessage = (JSONObject) jsonParser.parse(message);
                String clazz = (String) jsonMessage.get("@class");
                if (clazz != null) {
                    processClassMessage(jsonMessage, clazz);
                } else {
                    platformAbstractionLayer.logError("@Class field not found in message");
                }
            } catch (ParseException e) {
                platformAbstractionLayer.logError(e.getMessage(), e);
            }
        }
    }

    private void processClassMessage(JSONObject jsonMessage, String clazz) {
        if (clazz.endsWith("Response")) {
            processResponseClass(clazz, jsonMessage);
        } else {
            notifyMessageRecieved(jsonMessage);
        }
    }

    @SuppressWarnings("unchecked")
    private void notifyMessageRecieved(JSONObject jsonMessage) {
        if (messageRecievedListener != null) {
            messageRecievedListener.onMessage(jsonMessage);
        }
    }

    private void processResponseClass(String clazz, JSONObject jsonMessage) {

        setConnectUrlIfPresent(jsonMessage);
        refreshSessionIdIfPresent(jsonMessage);
        refreshAuthTokenIfPresent(jsonMessage);

        if (jsonMessage.containsKey("error") || (clazz.equals(".GameSparksErrorResponse"))) {
            processErrorResponse(jsonMessage);
        } else if (clazz.equals(".AuthenticatedConnectResponse")) {
            processAuthenticatedConnectResponse(jsonMessage);
            return;
        }

        sendReponseToMainThread(jsonMessage);
    }

    private void processErrorResponse(JSONObject jsonMessage) {
        if ((jsonMessage.get("error") instanceof String) && ("NOTAUTHORIZED".equals(((String) jsonMessage.get("error"))))) {
            authToken = "0";
        } else if ((jsonMessage.get("error") instanceof String) && ("VALIDATION FAILURE".equals(((String) jsonMessage.get("error"))))) {
            connectorStateEngine.changeState(StateChangeEvent.VALIDATION_FAILURE);
        } else {
            platformAbstractionLayer.logError("Error from server: " + jsonMessage.toString());
        }
    }

    private void setConnectUrlIfPresent(JSONObject jsonMessage) {
        if (jsonMessage.containsKey("connectUrl")) {
            connectUrl = (String) jsonMessage.get("connectUrl");
        }
    }

    private void refreshSessionIdIfPresent(JSONObject jsonMessage) {
        if (jsonMessage.containsKey("sessionId")) {
            sessionId = (String) jsonMessage.get("sessionId");
        }
    }

    private void sendReponseToMainThread(JSONObject jsonMessage) {
        String requestId = (String) jsonMessage.get("requestId");
        if (requestId == null) {
            platformAbstractionLayer.logError("Request id from server was null. " + jsonMessage.toJSONString());
            return;
        }

        DataReceivedEvent event = pendingRequests.get(requestId);
        if (event != null) {
            event.data = jsonMessage;
            event.latch.countDown();
        } else {
            platformAbstractionLayer.logError("No data found for request id: " + requestId);
        }
    }

    private void processAuthenticatedConnectResponse(JSONObject jsonMessage) {

        if (jsonMessage.containsKey("connectUrl")) {
            switchToNewWebSocket(jsonMessage);
        } else if (jsonMessage.containsKey("nonce")) {
            processNonce(jsonMessage);
        } else if (jsonMessage.containsKey("sessionId")) {
            processSessionId(jsonMessage);
        } else {
            platformAbstractionLayer.logError("Could not process AuthenticatedConnectResponse response, no connectUrl, nonce or sessionId: "
                    + jsonMessage.toJSONString());
        }
    }

    /*
     * The server has closed the initial socket. We need to reconnect on the new URL provided by the server.
     */
    private void switchToNewWebSocket(JSONObject jsonMessage) {
        connectorStateEngine.changeState(StateChangeEvent.SERVICE_URL_RECIEVED);
    }

    @SuppressWarnings("unchecked")
    private void processNonce(JSONObject jsonMessage) {

        JSONObject authenticatedConnectRequest = new JSONObject();
        authenticatedConnectRequest.put("@class", ".AuthenticatedConnectRequest");
        authenticatedConnectRequest.put("hmac", getHmac((String) jsonMessage.get("nonce")));
        authenticatedConnectRequest.put("os", getOsVersion());
        authenticatedConnectRequest.put("platform", gsApi.getPlatform());
        authenticatedConnectRequest.put("deviceId", getDeviceId());

        if (!"0".equals(authToken)) {
            authenticatedConnectRequest.put("authToken", authToken);
        }

        if (sessionId != null) {
            authenticatedConnectRequest.put("sessionId", sessionId);
        }

        if (webSocket != null) {
            webSocket.send(authenticatedConnectRequest.toJSONString());
        }

    }

    private void processSessionId(JSONObject message) {
        sessionId = (String) message.get("sessionId");
        initialisationLatch.countDown();
        connectorStateEngine.changeState(StateChangeEvent.SESSON_ID_RECEIVED);
    }

    private void refreshAuthTokenIfPresent(JSONObject jsonMessage) {
        if (jsonMessage.containsKey("authToken")) {
            if ((authToken != null) && (!authToken.equals((String) jsonMessage.get("authToken")))) {
                authToken = (String) jsonMessage.get("authToken");
                platformAbstractionLayer.setProperty(serviceUrl + ".authToken", authToken);
            }
        }
    }

    private String getHmac(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] sha = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            String hash = platformAbstractionLayer.base64EncodeToString(sha);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            platformAbstractionLayer.logError(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            platformAbstractionLayer.logError(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            platformAbstractionLayer.logError(e.getMessage(), e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> sendNoWait(Map<String, Object> jsonData) {
        if ((webSocket != null) && (isAuthorised())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putAll(jsonData);
            webSocket.send(jsonObject.toJSONString());
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("success", "message sent");
            return result;
        } else {
            return buildErrorReponse("GameSparks SDK not ready to send." + " SDK state: " + connectorStateEngine.getConnectionState());
        }
    }

    private JSONObject processResponse(DataReceivedEvent dataReceivedEvent, String requestId) {
        if (dataReceivedEvent.data == null) {
            return buildErrorResponse(requestId);
        } else {
            return dataReceivedEvent.data;
        }
    }

    private void waitForResponseFromServer(DataReceivedEvent dataReceivedEvent) {
        try {
            dataReceivedEvent.latch.await(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            platformAbstractionLayer.logError(e.getMessage(), e);
        } finally {
            pendingRequests.remove(dataReceivedEvent.requestId);
        }
    }

    @SuppressWarnings("unchecked")
    private JSONObject buildErrorResponse(String requestId) {
        platformAbstractionLayer.logError("No response within " + TIMEOUT_IN_SECONDS + " secs. SDK state: " + connectorStateEngine.getConnectionState());
        JSONObject errors = new JSONObject();
        errors.put("timeout", "No response within " + TIMEOUT_IN_SECONDS + " secs. SDK state: " + connectorStateEngine.getConnectionState());
        JSONObject error = new JSONObject();
        error.put("error", errors);
        return error;
    }

    /**
     * Used for inter-thread communication between the sending thread and the WebSocket thread that receives responses.
     */
    private class DataReceivedEvent {
        CountDownLatch latch;
        String requestId;
        JSONObject data;

        DataReceivedEvent(String requestId) {
            this.requestId = requestId;
            this.latch = new CountDownLatch(LATCH_COUNT);
        }
    }

    public void setPlatformAbstractionLayer(PlatformAbstractionLayer gsPlatform) {
        this.platformAbstractionLayer = gsPlatform;
    }

    @Override
    public void onConnectivityChange(boolean isNetworkAvailable) {
        if (isNetworkAvailable && (connectorStateEngine.getConnectionState().equals(ConnectorState.STOPPED))) {
            platformAbstractionLayer.logDebug("onConnectivityChange, network is available");
            connectorStateEngine.changeState(StateChangeEvent.NETWORK_AVAILABLE);
        }
    }

    public void endSession() {
        platformAbstractionLayer.logDebug("endSession");
        this.sessionId = null;
        disconnect();
    }

    public boolean isAuthorised() {
        return connectorStateEngine.getConnectionState().equals(ConnectorState.AUTHORISED);
    }

    public void setMessageRecievedListener(MessageRecievedListener messageRecievedListener) {
        this.messageRecievedListener = messageRecievedListener;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getSecret() {
        return secret;
    }

    WebSocketAbstractionLayer getWebSocket() {
        return webSocket;
    }

    public PlatformAbstractionLayer getPlatformAbstractionLayer() {
        return platformAbstractionLayer;
    }

    String getConnectUrl() {
        return connectUrl;
    }

    public boolean isAuthTokenValid() {
        return authToken != null && !"0".equals(authToken);
    }

    private void notifySdkConnected() {
        if (sdkConnectedListener != null) {
            sdkConnectedListener.onConnected();
        }
    }

    public void setSdkConnectedListener(SdkConnectedListener sdkConnectedListener) {
        this.sdkConnectedListener = sdkConnectedListener;
    }

    public void sendEventually(Map<String, Object> data) {
        offlineRequestQueue.add(data);
    }

    public boolean isAuthenticated() {
        return isAuthorised() && isAuthTokenValid();
    }

    public void processOfflineQueue() {
        offlineRequestQueue.sendQueuedItems();
    }

}
