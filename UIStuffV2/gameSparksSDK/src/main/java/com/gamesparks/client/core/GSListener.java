package com.gamesparks.client.core;

import java.util.Map;

/**
 * Listener interface used to receive asynchronous responses.
 * 
 * @author nick redshaw
 */
public interface GSListener {

    /**
     * Called by the SDK when a response is received from the GameSparks platform.
     * 
     * @param responseData the response data
     */
    void onMessage(Map<String, Object> responseData);

}
