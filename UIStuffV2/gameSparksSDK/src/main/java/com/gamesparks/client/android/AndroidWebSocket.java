package com.gamesparks.client.android;

import java.net.URI;

import com.gamesparks.client.android.websocket.WebSocketClient;
import com.gamesparks.client.core.WebSocketAbstractionLayer;
import com.gamesparks.client.core.WebSocketListener;

/**
 * Wraps the Android WebSocket library.
 * 
 * @author nick redshaw
 * 
 */
public class AndroidWebSocket extends WebSocketAbstractionLayer {

    private WebSocketClient webSocketClient;

    public AndroidWebSocket(String url, WebSocketListener webSocketListener) {
        super(url, webSocketListener);
    }

    @Override
    public void connect() {
        webSocketClient = new WebSocketClient(URI.create(url), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                webSocketListener.onConnect();
            }

            @Override
            public void onMessage(String message) {
                webSocketListener.onMessage(message);
            }

            @Override
            public void onMessage(byte[] data) {
                webSocketListener.onMessage(data);
            }

            @Override
            public void onDisconnect(int code, String reason) {
                webSocketListener.onDisconnect(code, reason);
            }

            @Override
            public void onError(Exception error) {
                webSocketListener.onError(error);
            }
        }, null);

        webSocketClient.connect();

    }

    @Override
    public void disconnect() {
        webSocketClient.disconnect();
    }

    @Override
    public void send(String data) {
        webSocketClient.send(data);
    }

    @Override
    public boolean isSocketAlive() {
        return webSocketClient.isSocketAlive();
    }

}
