package com.thais.http.response;

public interface HttpResponse {
    String toString();

    default int getCode() {
        return 0;
    }

    String getMessage();

    void setCode();

    void setMessage();

}
