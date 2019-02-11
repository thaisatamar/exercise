package com.thais.client.http;

import com.thais.client.FileClient;
import com.thais.http.HttpUtils;
import com.thais.http.response.HttpExceptionResponse;
import com.thais.http.response.HttpResponse;
import com.thais.http.response.HttpStatusResponse;
import com.thais.utils.FileUtils;
import com.thais.utils.ProgressCounter;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DefaultHttpClient implements FileClient {

    static Logger logger = LoggerFactory.getLogger(DefaultHttpClient.class);
    @Value(DefaultHttpClientParameters.useCompression)
    String paramCompress;
    @Value(DefaultHttpClientParameters.temporaryFile)
    private String temporaryFileName;
    @Value(DefaultHttpClientParameters.uploadFile)
    private String uploadFileName;
    @Value(DefaultHttpClientParameters.serverAddress)
    private String serverAddress;
    @Value(DefaultHttpClientParameters.serverPort)
    private String serverPort;
    @Value(DefaultHttpClientParameters.serverScheme)
    private String serverScheme;
    private boolean useCompression = true;


    public DefaultHttpClient() {

    }

    public DefaultHttpClient(String temporaryFileName, boolean compress) {
        this.temporaryFileName = temporaryFileName;
        this.useCompression = compress;
    }

    @PostConstruct
    void init() {
        useCompression = paramCompress.toLowerCase().startsWith("t") || paramCompress.toLowerCase().startsWith("y");
        System.out.println("Compression is " + (useCompression ? "on." : "off."));
    }

    @PreDestroy
    void cleanup() {
        try {
            FileUtils.cleanupFiles(".", "./" + temporaryFileName);
        } catch (IOException e) {
        }
    }

    @Override
    public HttpResponse upload() {
        String fileName = compress(uploadFileName);
        return send(fileName);
    }

    @Override
    public String getUploadFileName() {
        return uploadFileName;
    }

    @Override
    public void setUploadFileName(String name) {
        this.uploadFileName = name;
    }

    private String compress(String uploadFileName) {
        if (useCompression == false) {
            return uploadFileName;
        }
        return FileUtils.compress(uploadFileName, temporaryFileName);
    }


    HttpResponse send(String fileName) {
        try (InputStream is =
                     new BufferedInputStream(
                             new FileInputStream(fileName));
             CloseableHttpClient client =
                     HttpClients.createDefault()
        ) {
            String serverUrl = getServerUrl();
            System.out.println("Calling:  " + serverUrl);
            ProgressCounter tracker = new ProgressCounter();
            HttpPost request = createPostRequest(serverUrl, fileName, is);
            return getResponse(client, request);
        } catch (Exception e) {
            HandleException(e);
            return new HttpExceptionResponse(e);

        }
    }

    private void HandleException(Exception e) {
        System.out.println(".");
        if (e instanceof ClientProtocolException) {
            System.out.println(DefaultHttClientConstants.serverConnectionInterrupted);
        } else if (e instanceof HttpHostConnectException) {
            System.out.println(DefaultHttClientConstants.serverNotResponding);
        } else {
            System.out.println(DefaultHttClientConstants.serverException + e);
        }
    }

    private HttpResponse getResponse(CloseableHttpClient client, HttpPost httpPost) throws IOException {
        try (
                CloseableHttpResponse response = client.execute(httpPost)) {
            StatusLine sl = response.getStatusLine();
            System.out.println(DefaultHttClientConstants.serverResponse + sl.getReasonPhrase());
            if (sl.getStatusCode() == HttpStatus.SC_OK) {
                System.out.println(DefaultHttClientConstants.transferOK);
            } else {
                System.out.println(DefaultHttClientConstants.transferFailed + "["
                        + sl.getStatusCode() + " - "
                        + sl.getReasonPhrase() + "]"
                );
            }
            return new HttpStatusResponse(sl);
        }
    }

    private HttpPost createPostRequest(String serverUrl, String fileName, InputStream is) throws IOException {
        HttpPost httpPost = new HttpPost(serverUrl + "/put");
        httpPost.addHeader(HttpUtils.getContentDispositionHeader(fileName));

        InputStreamEntity entity = new InputStreamEntity(is, -1);
        entity.setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
        entity.setChunked(true);
        httpPost.setEntity(entity);
        return httpPost;

    }


    private String getServerUrl() {
        return serverScheme + "://" + serverAddress + ":" + serverPort;
    }


    public void setServerAddress(String hostName) {
        this.serverAddress = hostName;
    }

    public void setServerPort(int port) {
        if (port < 0) {
            port = 8080;
        }
        this.serverPort = String.valueOf(port);
    }

    public void setScheme(String http) {
        this.serverScheme = http;
    }
}
