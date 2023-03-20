package com.example.videoupload.checklist;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component(value = "uploadPathHandler")
public class FileExtention {
    @Value("${myapp.video-path}")
    private String videoFilePath;
    @Value("${myapp.music-path}")
    private String musicFilePath;

    private final String[] videoFileExtentions =
            {"mp4", "avi", "wmv", "mov", "mkv", "flv", "m4v", "mpeg", "mpg", "3gp", "webm", "vob", "ogv"};

    private final String[] musicFileExtentions =
            {"mp3", "wav", "wma", "aac", "flac", "ogg", "m4a"};


    /**
     * 파일의 확장자를 체크해 동영상과 음악파일로 나누어 다른 경로 값을 리턴.
     *
     * @param file file
     * @return {@link String}
     * @see String
     */
    public String checkFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String[] fileNameParts = fileName.split("\\.");
        String fileExtension = fileNameParts[fileNameParts.length - 1].toLowerCase();

        for (String extentions : videoFileExtentions) {
            if (fileExtension.equals(extentions)) {
                return videoFilePath;
            }
        }
        for (String extentions : musicFileExtentions) {
            if (fileExtension.equals(extentions)) {
                return musicFilePath;
            }
        }
        throw new IllegalArgumentException("동영상 및 음원파일만 업로드 가능합니다.");
    }
    public String checkFile(String fileName) {
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String[] fileNameParts = fileName.split("\\.");
        String fileExtension = fileNameParts[fileNameParts.length - 1].toLowerCase();
        for (String extentions : videoFileExtentions) {
            if (fileExtension.equals(extentions)) {
                return videoFilePath;
            }
        }
        for (String extentions : musicFileExtentions) {
            if (fileExtension.equals(extentions)) {
                return musicFilePath;
            }
        }
        throw new IllegalArgumentException("동영상 및 음원파일만 업로드 가능합니다.");
    }
}
