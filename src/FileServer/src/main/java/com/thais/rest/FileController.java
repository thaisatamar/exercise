package com.thais.rest;


import com.thais.http.response.HttpExceptionResponse;
import com.thais.rest.response.PartResponse;
import com.thais.storage.DefaultStorage;
import com.thais.storage.StorageConstants;
import com.thais.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
public class FileController {
    final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    DefaultStorage storage;


    @PostMapping(path = "/put", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String uploadFile(InputStream stream, @RequestHeader HttpHeaders headers) {
        String fileName = "ERROR";
        logger.info(StorageConstants.requestedTransferFrom, headers.getHost());
        String originalFile = headers.getContentDisposition().getFilename();
        try {
            fileName = storage.storePart(stream, getOriginalFile(originalFile));
        } catch (Exception e) {
            return JsonUtils.toJ(new HttpExceptionResponse(e));
        }

        File file = new File(fileName);
        PartResponse response = new PartResponse(fileName, headers.getContentType().getType(), file.length());
        response.setMessage(ControllerConstants.uploadSuccessful);
        String sResponse = JsonUtils.toJ(response);
        logger.info(StorageConstants.clientInfo, JsonUtils.toJ(headers));
        logger.info(StorageConstants.transferCompleted, sResponse);
        return sResponse;
    }

    private String getOriginalFile(String originalFile) {
        if (originalFile.contains("\"")) {
            originalFile = originalFile.substring(originalFile.indexOf("\""));
        }
        Path orig = Paths.get(originalFile);
        if (orig == null) {
            return "";
        }
        return orig.getFileName().toString();
    }


}
