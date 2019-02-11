package com.thais.storage;

import com.thais.utils.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.TestCase.fail;


// Performs a full cycle by storing a file,
// and verifying both file's CRCs are a match

@RunWith(SpringRunner.class)

public class TestCompression {

    static String testFile = "test.file";
    static long testFileCRC;

    @BeforeClass
    public static void before() {
        try {
            FileUtils.cleanupFiles(".", "./" + StorageConstants.filePrefix);
            FileUtils.cleanupFiles(".", "./" + testFile);
            FileUtils.createFile(testFile);
            testFileCRC = FileUtils.getFileCRC(testFile);
            assert (testFileCRC > 0);
        } catch (FileNotFoundException f) {
            f.printStackTrace();
            fail();

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testFileStorage() {
        try {
            Storage storage = new DefaultStorage(new StorageProperties());

            try (InputStream is = new FileInputStream(testFile)) {
                String newFileName = storage.storePart(is, "");
                assert (testFileCRC == FileUtils.getFileCRC(newFileName));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
