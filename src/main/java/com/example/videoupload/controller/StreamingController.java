package com.example.videoupload.controller;

import com.example.videoupload.checklist.FileExtention;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Controller
public class StreamingController {

    private final FileExtention fileExtention;

    /*
    @GetMapping("/streaming/{fileName}") 로 리퀘스트가 전달된 후 도착하는 url
    해당
     */
    @GetMapping("/resources/streaming/{fileName}")
    public ResponseEntity<StreamingResponseBody> streamVideo(@PathVariable @ModelAttribute("fileName") String fileName) {
        // 동영상 파일 경로 설정
        StringBuilder uploadPath = new StringBuilder();
        try {
            uploadPath.append(fileExtention.checkFile(fileName));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        Path videoPath = Paths.get(uploadPath + fileName);

        StreamingResponseBody responseBody = outputStream -> {
            try (InputStream inputStream = Files.newInputStream(videoPath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseBody);
    }

    /*
    파일이름을 param으로 보내주면 localhost:8080/resources/streaming/{fileName}이 html로 담겨 해당 경로로 api를 호출한다.
     */
    @GetMapping("/streaming/{fileName}")
    public String streamVideo(@PathVariable String fileName, Model model) {
        StringBuilder uploadPath = new StringBuilder();
        try {
            System.out.println(fileName);
            uploadPath.append(fileExtention.checkFile(fileName));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        model.addAttribute("videoSrc", "/resources/streaming/" + fileName);
        return "streaming";
    }


    @GetMapping("/video/all")
    public ResponseEntity<StreamingResponseBody> streamAllVideos() {
        Path videoDirectory = Paths.get("classpath:videos/");
        StreamingResponseBody responseBody = outputStream -> {
            try (Stream<Path> paths = Files.list(videoDirectory)) {
                paths.forEach(path -> {
                    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/videos/" + path.getFileName().toString())) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "all_videos.mp4" + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseBody);
    }
}
