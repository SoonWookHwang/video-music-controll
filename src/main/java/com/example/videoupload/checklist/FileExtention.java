package com.example.videoupload.checklist;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component(value = "uploadPathHandler")
public class FileExtention {
    private final String videoFilePath = "/Downloads/myvideos/";
    private final String musicFilePath = "/Downloads/mymusics/";

    private final String[] videoFileExtentions =
            {"mpeg4", "avi", "mp4", "wmv", "flv", "asf"};

    private final String[] musicFileExtentions =
            {"wav", "mp3", "aac", "flac", "m4a"};


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
}
