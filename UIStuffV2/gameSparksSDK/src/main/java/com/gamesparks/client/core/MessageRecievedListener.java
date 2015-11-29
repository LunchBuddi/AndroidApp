package com.gamesparks.client.core;

import java.util.Map;

/**
 * Listener interface used to receive server messages.
 * 
 * @author nick redshaw
 * 
 */
public interface MessageRecievedListener {

    void onMessage(Map<String, Object> message);

}
