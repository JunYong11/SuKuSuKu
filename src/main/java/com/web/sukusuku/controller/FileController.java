package com.web.sukusuku.controller;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.web.sukusuku.model.Post;
import com.web.sukusuku.model.UploadFile;
import com.web.sukusuku.repository.PostRepository;
import com.web.sukusuku.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final PostRepository postRepository;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("postId") Long postId) throws IOException {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음!"));

        fileService.saveFile(file, post);

        return "redirect:/posts/view/" + postId;
    }

    @GetMapping("/list/{postId}")
    public String fileList(@PathVariable Long postId, Model model) {
        List<UploadFile> files = fileService.getFilesByPost(postId);
        model.addAttribute("files", files);
        return "posts/fileList"; // → templates/posts/fileList.html
    }
    
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) throws IOException {
        UploadFile file = fileService.getFile(fileId);
        UrlResource resource = new UrlResource("file:" + file.getFilePath());

        String encodedFileName = java.net.URLEncoder.encode(file.getOriginalFileName(), "UTF-8");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }
    
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> viewImage(@PathVariable Long fileId) throws MalformedURLException {

        UploadFile file = fileService.getFile(fileId);

        if (file == null) {
            throw new RuntimeException("파일이 존재하지 않습니다!");
        }

        Path path = Paths.get(file.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("이미지를 찾을 수 없거나 읽을 수 없습니다!");
        }

        String contentType = "image/jpeg"; // 기본값
        String filename = file.getOriginalFileName().toLowerCase();

        if (filename.endsWith(".png")) {
            contentType = "image/png";
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (filename.endsWith(".gif")) {
            contentType = "image/gif";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }


}


