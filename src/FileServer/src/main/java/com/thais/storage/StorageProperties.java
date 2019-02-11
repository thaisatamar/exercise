package com.thais.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class StorageProperties {

    public String getUploadDir() {
        return StorageConstants.uploadDir;
    }
}
