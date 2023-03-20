package com.example.videoupload.controller;

import com.example.videoupload.checklist.FileExtention;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequiredArgsConstructor
public class FileUploadController {
    private final FileExtention fileExtention;

    @GetMapping("/")
    public String homepage(){
        return "index";
    }
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String myFileName) {
        StringBuilder uploadPath = new StringBuilder();
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

        try {
            uploadPath.append(fileExtention.checkFile(file));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        try {
//            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            File newFile = new File(uploadPath + myFileName+"."+ext.toLowerCase());
            newFile.createNewFile();
            Files.write(newFile.toPath(), file.getBytes());
            return ResponseEntity.ok().body("파일 업로드를 성공했습니다");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드에 실패했습니다.");
        }
    }

}
