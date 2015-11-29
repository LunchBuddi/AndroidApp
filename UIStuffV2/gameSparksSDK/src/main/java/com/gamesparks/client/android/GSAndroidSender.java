package com.gamesparks.client.android;

import java.util.Map;

import android.app.Activity;

import com.gamesparks.client.core.Completer;
import com.gamesparks.client.core.ConnectorClient;
import com.gamesparks.client.core.GSSender;

/**
 * An Android specific version of a GSSender that can update n Activity's UI Thread with a response to an asynchronous message.
 * 
 * @author nick redshaw
 */
public class GSAndroidSender extends GSSender {

    public GSAndroidSender(ConnectorClient connectorClient, Map<String, Object> data) {
        super(connectorClient, data);
    }

    public GSAndroidSender(ConnectorClient connectorClient, Map<String, Object> data, Completer completer) {
        super(connectorClient, data, completer);
    }

    /**
     * Asynchronous send method whose response can interact with the calling application's UI.
     * 
     * @param gameSparksListener
     */
    public void sendAsync(Activity activity, GSAndroidRunnableTask gsAndroidRunnableTask) {

        final AndroidActivityGSListener androidActivityGSListener = new AndroidActivityGSListener(activity, gsAndroidRunnableTask);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> response = connectorClient.send(data);
                completeResponse(response);
                connectorClient.getPlatformAbstractionLayer().handleAsyncResult(response, androidActivityGSListener);
            }
        }).start();
    }
}
