package com.gamesparks.client.core;

import java.util.Map;

/**
 * Represents an action or task that needs to be executed in order to complete the GSApi method.
 * 
 * @author nick redshaw
 * 
 */
public interface Completer {

    /**
     * Execute the action or task.
     * 
     * @param response the reponse data from the previously executed GSApi method
     */
    void execute(Map<String, Object> response);

}
