package com.thais.http.response;

import com.thais.utils.JsonUtils;

public abstract class DefaultHttpResponse implements HttpResponse {


    public String toString() {

        return JsonUtils.toJ(this);
    }


    @Override
    public void setCode() {

    }

    @Override
    public void setMessage() {

    }


}
