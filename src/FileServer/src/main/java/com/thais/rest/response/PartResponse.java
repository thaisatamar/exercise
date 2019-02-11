package com.thais.rest.response;

import com.thais.http.response.DefaultHttpResponse;

public class PartResponse extends DefaultHttpResponse {
    private String fileName;
    private String contentType;
    private long size;
    private long code;
    private String message;

    public PartResponse() {

    }

    public PartResponse(String fileName, String contentType, long size) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
