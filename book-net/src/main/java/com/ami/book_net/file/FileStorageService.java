package com.ami.book_net.file;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    @Value("${application.security.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String savefile(
            @NonNull MultipartFile sourceFile,
            @NonNull Integer userId
    ){
        final String fileUploadSubPath = "users"+ File.separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(
            @NonNull MultipartFile sourceFile,
            @NonNull String fileUploadSubPath
    ) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if(!targetFolder.exists()){
            boolean folderCreated = targetFolder.mkdirs();
            if(!folderCreated){
                log.warn("Failed to create folder: {}", finalUploadPath);
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFileName = finalUploadPath+ File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Path.of(targetFileName);
        try{
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File uploaded successfully: {}", targetFileName);
            return targetFileName;
        }catch (Exception e){
            log.error("Failed to write file: {}", targetFileName, e);
        }
        return null;

    }

    private String getFileExtension(String filename) {
        if(filename == null || filename.isEmpty()){
            return "";
        }
        int lastIndexOfDot = filename.lastIndexOf(".");
        if(lastIndexOfDot == -1){
            return "";
        }
        return filename.substring(lastIndexOfDot + 1).toLowerCase();
    }
}
