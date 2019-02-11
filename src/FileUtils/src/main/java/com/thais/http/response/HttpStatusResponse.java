package com.thais.http.response;

import org.apache.http.StatusLine;

public class HttpStatusResponse extends DefaultHttpResponse {
    StatusLine sl;

    public HttpStatusResponse(StatusLine sl) {
        this.sl = sl;
    }

    @Override
    public int getCode() {
        return sl.getStatusCode();
    }

    @Override
    public String getMessage() {
        return sl.getReasonPhrase();
    }


}
