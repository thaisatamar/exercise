package com.thais.rest;

import com.thais.client.http.DefaultHttpClient;
import com.thais.http.response.HttpResponse;
import com.thais.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import static org.junit.Assert.fail;


public class testConfiguration {
    static final String testFile = "test.file";
    static Server server;

    @Before
    public void start() {
        try {
            FileUtils.cleanupFiles(".", testFile);
            FileUtils.createFile(testFile);


            server = new Server(7080);
            server.setStopAtShutdown(true);
            WebAppContext webAppContext = new WebAppContext();
            webAppContext.setContextPath("/put");
            webAppContext.setResourceBase("src/main/webapp");
            webAppContext.setClassLoader(getClass().getClassLoader());
            server.addHandler(webAppContext);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void TestFileUpload() {
        DefaultHttpClient httpClient = new DefaultHttpClient(testFile, true);
        Connector c = server.getConnectors()[0];
        httpClient.setServerPort(c.getPort());
        httpClient.setServerAddress("localhost");
        httpClient.setScheme("http");
        httpClient.setUploadFileName(testFile);

        HttpResponse res = httpClient.upload();
        assert (res.getCode() == 302);

    }

}
