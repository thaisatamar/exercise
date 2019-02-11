package com.thais.rest;

import com.thais.http.HttpUtils;
import com.thais.rest.response.PartResponse;
import com.thais.storage.DefaultStorage;
import com.thais.utils.FileUtils;
import com.thais.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)

// Creates and streams a file via MockWebContext and check responses

public class TestController extends MockWebCtxt {
    String testFile = "server.test";
    @Autowired
    DefaultStorage storage;


    @Override
    @Before
    public void setUp() {
        super.setUp();
        try {
            FileUtils.cleanupFiles(".", testFile);
            FileUtils.createFile(testFile);
        } catch (Exception e) {
            fail("Could not create test file for streaming");
        }
    }

    InputStream streamFile(String testFile) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(testFile));
    }

    @Test
    public void testPut() throws Exception {
        String uri = "/put";

        String contentTypeStream = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header(HttpUtils.getContentDispositionHeaderTitle(),
                                HttpUtils.getContentDispositionHeaderAttachment(testFile))
                        .contentType(contentTypeStream)
                        .content(streamFile(testFile).readAllBytes())).andReturn();

        MockHttpServletResponse content = mvcResult.getResponse();
        int status = mvcResult.getResponse().getStatus();
        assert (HttpStatus.OK.value() == status);
        PartResponse response = JsonUtils.fromJ(content.getContentAsString(), PartResponse.class);
        assert (response.getFileName().contains(FileUtils.normalizeFileName(testFile)));
        assert (response.getMessage().contains(ControllerConstants.uploadSuccessful));
    }

    @Test
    public void testBadContentType() throws Exception {
        String uri = "/put";


        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header(HttpUtils.getContentDispositionHeaderTitle(),
                                HttpUtils.getContentDispositionHeaderAttachment(testFile))
                        .contentType(MediaType.APPLICATION_ATOM_XML)
                        .content(streamFile(testFile).readAllBytes())).andReturn();

        MockHttpServletResponse content = mvcResult.getResponse();
        int status = mvcResult.getResponse().getStatus();
        assert (HttpStatus.UNSUPPORTED_MEDIA_TYPE.value() == status);

    }

    @Test
    public void testBadURI() throws Exception {
        String uri = "/get";


        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header(HttpUtils.getContentDispositionHeaderTitle(),
                                HttpUtils.getContentDispositionHeaderAttachment(testFile))
                        .contentType(MediaType.APPLICATION_ATOM_XML)
                        .content(streamFile(testFile).readAllBytes())).andReturn();

        MockHttpServletResponse content = mvcResult.getResponse();
        int status = mvcResult.getResponse().getStatus();
        assert (HttpStatus.NOT_FOUND.value() == status);

    }

    @Test
    public void testBadHeader() throws Exception {
        String uri = "/put";


        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header(HttpUtils.getContentDispositionHeaderTitle(),
                                HttpUtils.getContentDispositionHeaderAttachment(null))
                        .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .content(streamFile(testFile).readAllBytes())).andReturn();

        MockHttpServletResponse content = mvcResult.getResponse();
        int status = mvcResult.getResponse().getStatus();
        assert (HttpStatus.OK.value() == status);

    }


}
