package com.gamesparks.client.core.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Provide support for posting multipart forms and downloading files via the {@link HttpURLConnection} class.
 * 
 * @author nick redshaw
 * 
 */
public class HttpClient {

    private String delimiter = "--";
    private String boundary = "SwA" + Long.toString(System.currentTimeMillis()) + "SwA";
    private OutputStream outputStream;
    private HttpURLConnection httpURLConnection;

    public HttpClient(String url) throws Exception {
        httpURLConnection = (HttpURLConnection) (new URL(url)).openConnection();
    }

    /**
     * Sends binary data in a multiplart form.
     * 
     * @param paramName
     * @param fileName
     * @param data
     * @return
     * @throws Exception
     */
    public String sendMultipart(String paramName, String fileName, byte[] data) throws Exception {
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        httpURLConnection.connect();
        outputStream = httpURLConnection.getOutputStream();
        outputStream.write((delimiter + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
        outputStream.write(("Content-Type: application/octet-stream\r\n").getBytes());
        outputStream.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
        outputStream.write("\r\n".getBytes());
        outputStream.write(data);
        outputStream.write("\r\n".getBytes());
        outputStream.write((delimiter + boundary + delimiter + "\r\n").getBytes());

        return getResponse();
    }

    private String getResponse() throws Exception {
        InputStream inputStream = httpURLConnection.getInputStream();
        byte[] byteArray = new byte[1024];
        StringBuffer response = new StringBuffer();
        while (inputStream.read(byteArray) != -1) {
            response.append(new String(byteArray));
        }

        httpURLConnection.disconnect();

        return response.toString().trim();
    }

    public byte[] downloadFile() throws Throwable {
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();

        if (httpURLConnection.getResponseCode() >= 400) {
            throw new RuntimeException("Error response received from server: " + httpURLConnection.getResponseCode());
        }

        InputStream inputStream = httpURLConnection.getInputStream();
        byte[] byteArray = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (inputStream.read(byteArray) != -1) {
            byteArrayOutputStream.write(byteArray);
        }

        httpURLConnection.disconnect();

        return byteArrayOutputStream.toByteArray();
    }
}
