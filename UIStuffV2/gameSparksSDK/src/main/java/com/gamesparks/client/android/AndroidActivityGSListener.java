package com.gamesparks.client.android;

import java.util.Map;

import android.app.Activity;

import com.gamesparks.client.core.GSListener;

/**
 * A GSListener implementation used to update an Activity's UI Thread when asynchronous responses are received from the GameSparks platform.
 * 
 * @author nick redshaw
 * 
 */
public class AndroidActivityGSListener implements GSListener {

    private Activity activity;
    private GSAndroidRunnableTask gsAndroidRunnableTask;
    private Map<String, Object> responseData;

    /**
     * Constructor.
     * 
     * @param activity the Activity whose UI Thread you want to process the result
     * @param runnable A Runnable class to run on the Activity's UI Thread
     */
    public AndroidActivityGSListener(Activity activity, GSAndroidRunnableTask runnable) {
        this.activity = activity;
        this.gsAndroidRunnableTask = runnable;
        runnable.setGameSparksListener(this);
    }

    /**
     * Called by the SDK when a response is received from the GameSparks platform.
     * <p/>
     * The GameSparksRunnable will be run on the Activity's UI Tread when this method is called by the SDK.
     * 
     * @param reponseData the response data
     */
    @Override
    public void onMessage(Map<String, Object> reponseData) {
        this.responseData = reponseData;
        activity.runOnUiThread(gsAndroidRunnableTask);
    }

    /**
     * Getter for the response data.
     * <p/>
     * Call this from within the run method of your implementation of the GameSparksRunnable class.
     * 
     * @return the response data.
     */
    public Map<String, Object> getResponseData() {
        return responseData;
    }
}
