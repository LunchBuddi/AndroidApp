package com.gamesparks.client.core.net;

import java.util.Map;

import com.gamesparks.client.core.Completer;
import com.gamesparks.client.core.PlatformAbstractionLayer;

/**
 * A type of {@link Completer} that can upload binary data to the server via a multipart form.
 * 
 * @author nick redshaw
 * 
 */
public class FileUploader implements Completer {

    private byte[] file;
    private String fileName;
    private PlatformAbstractionLayer platformAbstractionLayer;

    public FileUploader(byte[] file, String fileName, PlatformAbstractionLayer platformAbstractionLayer) {
        this.file = file;
        this.fileName = fileName;
        this.platformAbstractionLayer = platformAbstractionLayer;
    }

    @Override
    public void execute(Map<String, Object> response) {

        if (response.containsKey("url")) {
            String postResponse = postMultipartFormData((String) response.get("url"), file, fileName);
            response.put("uploadResponse", postResponse);
        }
    }

    private String postMultipartFormData(String url, byte[] file, String fileName) {

        try {
            HttpClient client = new HttpClient(url);
            String response = client.sendMultipart("file", fileName, file);
            return response;
        } catch (Throwable t) {
            platformAbstractionLayer.logError(t.getMessage(), t);
        }

        return null;
    }

}
