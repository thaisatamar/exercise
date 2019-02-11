package com.thais.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileUtils {
    private static final int bufferlen = 128 * 1024 * 1024;
    private static final Logger logger = Logger.getLogger(FileUtils.class.getName());

    public static File createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists() == true) {
            return file;
        }
        try (OutputStream o = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] b = new byte[bufferlen];
            o.write(b, 0, bufferlen);
        }
        return file;
    }

    public static long getFileCRC(String fileName) throws IOException {

        long res = 0;

        try (CheckedInputStream cis = new CheckedInputStream(
                new FileInputStream(fileName), new CRC32())) {

            byte[] buffer = new byte[bufferlen];
            while ((cis.read(buffer, 0, bufferlen)) != -1) ;
            res = cis.getChecksum().getValue();
        }
        return res;
    }

    public static String decompress(String source, String target) {
        byte[] buffer = new byte[bufferlen];
        File uploadFile = new File(source);
        if (!uploadFile.exists()) {
            return source;
        }
        logger.fine("Initial file: ["
                + source
                + "] size: ["
                + uploadFile.length()
                + "]");
        logger.fine("Decompressing: ");
        try (
                InputStream fis = new FileInputStream(source);
                InputStream is = new GZIPInputStream(fis);
                OutputStream os = new BufferedOutputStream(new FileOutputStream(target))
        ) {

            writeWithProgress(uploadFile.length(), is, os);

            File temporaryFile = new File(target);
            if (!temporaryFile.exists()) {
                logger.info("Unable to locate compressed file.");
                return source;
            }
            logger.fine("Decompressed file "
                    + target
                    + "] size: ["
                    + temporaryFile.length()
                    + "]");
        } catch (IOException ioe) {
            System.out.println("Could not compress. ");
            return source;
        }
        return target;
    }

    public static String compress(String source, String target) {


        File uploadFile = new File(source);
        if (!uploadFile.exists()) {
            return source;
        }
        logger.fine("Initial file: ["
                + source
                + "] size: ["
                + uploadFile.length()
                + "]");
        logger.fine("Compressing: ");
        try (
                InputStream is = new FileInputStream(source);
                OutputStream os = new FileOutputStream(target);
                OutputStream gos = new GZIPOutputStream(os)
        ) {
            writeWithProgress(uploadFile.length(), is, gos);

            File temporaryFile = new File(target);
            if (!temporaryFile.exists()) {
                logger.info("Unable to locate compressed file.");
                return source;
            }
            logger.fine("Compressed file ["
                    + target
                    + "] size: ["
                    + temporaryFile.length()
                    + "]");
        } catch (IOException ioe) {
            logger.warning("Could not compress. ");
            logger.severe("Error " + ioe.getMessage());
            return source;
        }
        return target;
    }

    private static void writeWithProgress(long fileSize, InputStream is, OutputStream os) throws IOException {
        long len;
        long c = 0;
        long progress = 0;
        byte[] buffer = new byte[bufferlen];
        while ((len = is.read(buffer, 0, bufferlen)) != -1) {
            os.write(buffer, 0, (int) len);
            progress = printProgress(fileSize, c += len, progress);
        }
        logger.info(". DONE.");
    }

    private static long printProgress(long fileSize, long len, long lastProgress) {
        long slice = fileSize / 10;
        long nextProgress = lastProgress + slice;
        if (len > lastProgress) {
            System.out.println(nextProgress + " bytes processed. ");
            return nextProgress;
        }
        return lastProgress;
    }

    public static void cleanupFiles(String dir, String prefix) throws IOException {

        if ((prefix == null) || (prefix.isEmpty()))
            return;

        Function<Path, Stream<Path>> walk = p -> {
            try {
                return Files.walk(p);
            } catch (IOException e) {
                return Stream.empty();
            }
        };

        Consumer<Path> delete = p -> {
            try {
                logger.fine("Cleaning out" + p);
                Files.delete(p);
            } catch (IOException e) {
                logger.warning("Could not delete: " + e);
            }
        };
        Files.list(Paths.get(dir))
                .flatMap(walk)
                .filter(p -> p.startsWith(prefix))
                .sorted(Comparator.reverseOrder())
                .forEach(delete);
    }

    public static final String normalizeFileName(String fileName) {
        return fileName.replace(".", "");
    }
}
