package com.thais.http;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class HttpUtils {

    public final static String headerContentDisposition = "Content-Disposition";
    public final static String headerContentDispositionFilename = "attachment; filename=";

    public static Header getContentDispositionHeader(String fileName) {
        return new BasicHeader(headerContentDisposition, getContentDispositionHeaderAttachment(fileName));
    }

    public static String getContentDispositionHeaderTitle() {
        return headerContentDisposition;
    }

    public static String getContentDispositionHeaderAttachment(String fileName) {
        return headerContentDispositionFilename + fileName;
    }
}
