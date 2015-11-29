package com.gamesparks.client.android;

import java.util.Map;

/**
 * Abstract Runnable class that holds a reference to an {@link AndroidActivityGSListener}.
 * 
 * @author nick redshaw
 * 
 */
public abstract class GSAndroidRunnableTask implements Runnable {

    private AndroidActivityGSListener androidActivityGSListener;

    public void setGameSparksListener(AndroidActivityGSListener androidActivityGSListener) {
        this.androidActivityGSListener = androidActivityGSListener;
    }

    public Map<String, Object> getData() {
        return this.androidActivityGSListener.getResponseData();
    }

}
