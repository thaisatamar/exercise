package com.thais;

public class FileClientParameters {
    public static final String temporaryFile =
            "${temporary.file:tmp.gzip}";
    public static final String uploadFile =
            "${upload.file:none}";
    public static final String temporaryDirectory =
            "${temporary.dir:./tmp}";
    public static final String useCompression =
            "${use.compression:false}";
    public static final String serverAddress =
            "${server.address:127.0.0.1}";
    public static final String serverPort =
            "${server.port:8080}";
    public static final String serverScheme =
            "${server.scheme:http}";
}
