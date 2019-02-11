package com.thais.storage;


import java.io.IOException;
import java.io.InputStream;


public interface Storage {
    String storePart(InputStream is, String originalFileName) throws IOException;

}
