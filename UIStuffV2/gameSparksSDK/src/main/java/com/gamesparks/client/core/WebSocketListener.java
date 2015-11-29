package com.gamesparks.client.core;

/**
 * Listener interface for WebSocket events.
 * 
 * @author nick redshaw
 * 
 */
public interface WebSocketListener {

    public void onConnect();

    public void onMessage(String message);

    public void onMessage(byte[] data);

    public void onDisconnect(int code, String reason);

    public void onError(Exception error);
}
