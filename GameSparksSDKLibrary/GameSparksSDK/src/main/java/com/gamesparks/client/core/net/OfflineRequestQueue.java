package com.gamesparks.client.core.net;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.gamesparks.client.core.ConnectorClient;

/**
 * A queue for storing request data when the network is not available.
 * 
 * @author nick redshaw
 */
public class OfflineRequestQueue {

    private ConnectorClient connectorClient;
    private Queue<Map<String, Object>> queue;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public OfflineRequestQueue(ConnectorClient connectorClient) {
        this.connectorClient = connectorClient;
        initialiseQueue();
    }

    public void add(Map<String, Object> data) {

        if (data != null) {
            queue.add(data);
            persistQueue();
            if (connectorClient.isAuthenticated()) {
                sendQueuedItems();
            }
        }
    }

    public void sendQueuedItems() {

        executorService.submit(new Runnable() {

            @Override
            public void run() {
                while (queue.size() != 0) {
                    Map<String, Object> requestData = queue.peek();
                    Map<String, Object> result = connectorClient.sendNoWait(requestData);

                    if (result.containsKey("success")) {
                        queue.remove();
                        persistQueue();
                    } else {
                        connectorClient.getPlatformAbstractionLayer().logDebug(result.toString());
                        break;
                    }
                }
            }
        });

    }

    @SuppressWarnings("unchecked")
    public void initialiseQueue() {
        queue = new ConcurrentLinkedQueue<Map<String, Object>>();
        String queueString = connectorClient.getPlatformAbstractionLayer().getProperty("OfflineRequestQueue", null);
        if (queueString != null) {
            JSONArray jsonRequestData = (JSONArray) JSONValue.parse(queueString);
            for (Object data : jsonRequestData) {
                JSONObject json = (JSONObject) data;
                queue.add(json);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void persistQueue() {

        JSONArray jsonRequestData = new JSONArray();
        for (Map<String, Object> requestData : queue) {
            jsonRequestData.add(requestData);
        }

        connectorClient.getPlatformAbstractionLayer().setProperty("OfflineRequestQueue", jsonRequestData.toJSONString());
    }
}
