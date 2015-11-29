package com.gamesparks.client.core.net;

import java.util.Map;

import com.gamesparks.client.core.Completer;
import com.gamesparks.client.core.PlatformAbstractionLayer;

/**
 * A type of {@link Completer} that can download binary data from the server.
 * 
 * @author nick redshaw
 * 
 */
public class FileDownloader implements Completer {

    private PlatformAbstractionLayer platformAbstractionLayer;

    public FileDownloader(PlatformAbstractionLayer platformAbstractionLayer) {
        this.platformAbstractionLayer = platformAbstractionLayer;
    }

    @Override
    public void execute(Map<String, Object> response) {
        if (response.containsKey("url")) {
            platformAbstractionLayer.logDebug("URL: " + (String) response.get("url"));
            byte[] postResponse = downloadData((String) response.get("url"));
            response.put("bytes", postResponse);
        }
    }

    private byte[] downloadData(String url) {

        try {
            HttpClient client = new HttpClient(url);
            byte[] response = client.downloadFile();

            return response;
        } catch (Throwable t) {
            platformAbstractionLayer.logError(t.getMessage(), t);
        }

        return null;
    }

}
