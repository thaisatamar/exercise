package com.thais.client;

import com.thais.http.response.HttpResponse;

public interface FileClient {
    HttpResponse upload();

    String getUploadFileName();

    void setUploadFileName(String name);
}
