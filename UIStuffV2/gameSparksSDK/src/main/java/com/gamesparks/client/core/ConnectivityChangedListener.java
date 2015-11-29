package com.gamesparks.client.core;

/**
 * Listener interface used to indicate that network connectivity has changed.
 * 
 * @author nick redshaw
 * 
 */
public interface ConnectivityChangedListener {

    void onConnectivityChange(boolean isNetworkAvailable);
}
