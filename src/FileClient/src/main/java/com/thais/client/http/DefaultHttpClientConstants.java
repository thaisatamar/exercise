package com.thais.client.http;

public class DefaultHttpClientConstants {
    public static final String ifIssuePersists =
            "If the issue persists after retrying, please contact technical support. ";
    public static final String serverConnectionInterrupted =
            "ERROR - Connection with server has broken. " + ifIssuePersists;
    public static final String serverException =
            "ERROR - Server has returned an exception. " + ifIssuePersists;
    public static final String serverNotResponding = "" +
            "FATAL - Server is not responding. Please check the address and port configured on etc/client.yml and correct if necessary. \r\n" +
            "If details are correct, the server may not be accessible. " + ifIssuePersists;
    public static final String serverResponse =
            "Server response : ";
    public static final String transferOK =
            "Transfer completed successfully.";
    public static final String transferFailed =
            "Server responded with error, transfer not completed. " +
                    "Please contact technical support. " +
                    "Details: ";
    public static String serverConnectionInteruupted =
            "Server connection broken. Details: ";
}
