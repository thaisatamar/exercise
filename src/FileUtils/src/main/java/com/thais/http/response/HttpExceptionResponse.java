package com.thais.http.response;

public class HttpExceptionResponse extends DefaultHttpResponse {

    private String message;


    public HttpExceptionResponse(Exception e) {
        this.message = e.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }


    @Override
    public void setMessage() {
        this.message = message;
    }
}
