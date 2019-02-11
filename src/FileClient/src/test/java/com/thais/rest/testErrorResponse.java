package com.thais.rest;

import com.thais.client.http.DefaultHttpClient;
import com.thais.http.response.HttpResponse;
import com.thais.utils.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import static org.junit.Assert.fail;

public class testErrorResponse {

    static final String testFile = "test.file";
    static Server server;

    @Test
    public void TestFileUploadRed() {
        DefaultHttpClient httpClient = new DefaultHttpClient(testFile, true);
        Connector c = server.getConnectors()[0];
        httpClient.setServerAddress("localhost");
        httpClient.setServerPort(c.getPort());
        httpClient.setScheme("http");
        httpClient.setUploadFileName(testFile);
        HttpResponse res = httpClient.upload();
        assert (res.getCode() == 404);

    }

    @Test
    public void TestFileUploadWrongPort() {
        DefaultHttpClient httpClient = new DefaultHttpClient(testFile, true);
        Connector c = server.getConnectors()[0];
        httpClient.setServerAddress("localhost");
        httpClient.setServerPort(89);
        httpClient.setScheme("http");
        httpClient.setUploadFileName(testFile);
        HttpResponse res = httpClient.upload();
        assert (res.getMessage().contains("Connection refused"));

    }

    @Before
    public void start() {
        try {
            FileUtils.cleanupFiles(".", "./" + testFile);
            FileUtils.createFile(testFile);


            server = new Server(7082);
            server.setStopAtShutdown(true);
            WebAppContext webAppContext = new WebAppContext();
            webAppContext.setResourceBase("src/main/webapp");
            webAppContext.setClassLoader(getClass().getClassLoader());
            server.addHandler(webAppContext);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @After
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {

        }
    }
}
