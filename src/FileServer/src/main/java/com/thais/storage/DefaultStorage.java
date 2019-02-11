package com.thais.storage;

import com.thais.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class DefaultStorage implements Storage {

    static Logger logger = LoggerFactory.getLogger(DefaultStorage.class);
    private final Path fileStorageLocation;
    private final int bufferlen = 256 * 1024 * 1024;

    public DefaultStorage(StorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(StorageConstants.dirNotCreated, ex);
        }
    }

    public String storePart(InputStream is, String originalFileName) throws IOException {

        logger.info(StorageConstants.startTransfer, originalFileName);

        String target = getFileName(originalFileName);
        long c = 0;

        byte[] buffer = new byte[bufferlen];
        try (
                OutputStream os = new BufferedOutputStream(new FileOutputStream(target))
        ) {

            int read;
            long written = 0;
            while ((read = is.read(buffer, 0, bufferlen)) != -1) {
                written += read;
                c = printProgress(c, written);
                os.write(buffer, 0, read);
            }
        }
        return target;
    }

    private long printProgress(long counter, long progress) {

        long slice = bufferlen / 2;

        if (progress > counter + slice) {
            counter += slice;
            System.out.print(".");
        }
        return counter;

    }

    private String getFileName(String fileName) {

        return this.fileStorageLocation + "/"
                + StorageConstants.filePrefix + "-"
                + FileUtils.normalizeFileName(fileName)
                + "-" + UUID.randomUUID()
                + StorageConstants.fileSuffix;
    }


}
