package com.gamesparks.client.core;

/**
 * Base class for the WebSocket support required by the core GameSparks client classes.
 * 
 * @author nick redshaw
 * 
 */
public abstract class WebSocketAbstractionLayer {

    protected WebSocketListener webSocketListener;
    protected String url;

    public WebSocketAbstractionLayer(String url, WebSocketListener webSocketListener) {
        this.url = url;
        this.webSocketListener = webSocketListener;
    }

    /**
     * Connect to the URL.
     */
    public abstract void connect();

    /**
     * Disconnect form the WebSocket.
     */
    public abstract void disconnect();

    /**
     * Send the data to the WebSocket.
     * 
     * @param string
     */
    public abstract void send(String data);

    public abstract boolean isSocketAlive();

}
