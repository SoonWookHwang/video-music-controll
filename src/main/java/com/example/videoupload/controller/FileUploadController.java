package com.example.videoupload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

@RestController
public class FileUploadController {


    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if(!checkFile(file)){
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("동영상파일이 아닙니다.");
        }
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            File newFile = new File(System.getProperty("user.home") + "/Users/betweak/Downloads/myvideos/" + fileName);
            newFile.createNewFile();
            Files.write(newFile.toPath(), file.getBytes());
            return ResponseEntity.ok().body("파일 업로드를 성공했습니다");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드에 실패했습니다.");
        }
    }
    public boolean checkFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String[] fileNameParts = fileName.split("\\.");
        System.out.println(Arrays.toString(fileNameParts));
        String fileExtension = fileNameParts[fileNameParts.length - 1].toLowerCase();
        System.out.println("읽은 확장자=" + fileExtension);
        return fileExtension.equals("avi") || fileExtension.equals("mp4");
    }
}
