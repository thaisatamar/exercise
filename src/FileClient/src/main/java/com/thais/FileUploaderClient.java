package com.thais;

import com.thais.client.FileClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class FileUploaderClient {
    @Autowired
    FileClient client;


    @PostConstruct
    void init() {
        String file = client.getUploadFileName();
        if (file.equals("none")) {
            System.out.println("Usage: file-client [full path to filename] [compress: YES | no ] \r\n " +
                    "Example - uploaf file with no pre-upload compression: \r\n " +
                    " source file-client [myfile.txt] NO");
            return;
        }
        client.upload();
    }


}
