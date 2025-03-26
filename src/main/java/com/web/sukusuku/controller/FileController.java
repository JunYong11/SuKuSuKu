package com.web.sukusuku.controller;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.sukusuku.model.UploadFile;
import com.web.sukusuku.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws IOException {

        UploadFile file = fileService.getFileById(fileId);

        // 파일 경로 확인!
        System.out.println("file.getFilePath() = " + file.getFilePath());

        Path path = Paths.get(file.getFilePath());

        try {
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("파일이 존재하지 않거나 읽을 수 없습니다!");
            }

            String encodedFilename = URLEncoder.encode(file.getOriginalFileName(), StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new RuntimeException("잘못된 파일 경로입니다.", e);
        }
    }

//    @GetMapping("/download/{fileId}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws IOException {
//
//        UploadFile file = fileService.getFileById(fileId);
//
//        // 절대경로 확인 (디버깅)
//        String filePath = file.getFilePath();
//        System.out.println("file.getFilePath() = " + filePath);
//
//        // 절대경로일 것!
//        Path path = Paths.get(filePath);
//
//        // path가 어떤 타입인지 확인
//        System.out.println("Path 객체 타입 = " + path.getClass());
//
//        try {
//            Resource resource = new UrlResource(path.toUri());
//
//            if (!resource.exists() || !resource.isReadable()) {
//                throw new RuntimeException("파일이 존재하지 않거나 읽을 수 없습니다!");
//            }
//
//            String encodedFilename = URLEncoder.encode(file.getOriginalFileName(), StandardCharsets.UTF_8);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
//                    .body(resource);
//
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("잘못된 파일 경로입니다.", e);
//        }
//    }
}

