package com.gamesparks.client.core;

import java.util.Map;

/**
 * This class facilitates making synchronous and asynchronous API calls to the GameSparks platform.
 * 
 * @author nick redshaw
 */
public class GSSender {

    protected Map<String, Object> data;
    protected ConnectorClient connectorClient;
    protected Completer completer;

    public GSSender(ConnectorClient connectorClient, Map<String, Object> data) {
        this.data = data;
        this.connectorClient = connectorClient;
    }

    public GSSender(ConnectorClient connectorClient, Map<String, Object> data, Completer completer) {
        this(connectorClient, data);
        this.completer = completer;
    }

    /**
     * Synchronous send method.
     * 
     * @return the GameSparks server response
     */
    public Map<String, Object> send() {
        Map<String, Object> response = connectorClient.send(data);
        completeResponse(response);
        return response;
    }

    /**
     * Asynchronous send method whose response is sent to a simple listener object.
     * 
     * @param gameSparksListener
     */
    public void sendBackground(final GSListener gameSparksListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> response = connectorClient.send(data);
                completeResponse(response);
                gameSparksListener.onMessage(response);
            }
        }).start();

    }

    public void sendEventually() {
        connectorClient.sendEventually(data);
    }

    protected void completeResponse(Map<String, Object> response) {
        if (completer != null) {
            completer.execute(response);
        }
    }
}
