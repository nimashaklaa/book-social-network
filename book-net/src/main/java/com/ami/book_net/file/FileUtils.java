package com.ami.book_net.file;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {

    public static byte[] readFileFromLocation(String fileUrl) {
        if(StringUtils.isEmpty(fileUrl)){
            return null;
        }
        try{
            Path path = new File(fileUrl).toPath();
            return Files.readAllBytes(path);
        }catch (IOException e){
            log.error("Failed to read file from location: {}", fileUrl, e);
        }
        return null;
    }
}
