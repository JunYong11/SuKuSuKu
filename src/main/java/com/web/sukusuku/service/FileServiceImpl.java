package com.web.sukusuku.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.sukusuku.model.Post;
import com.web.sukusuku.model.UploadFile;
import com.web.sukusuku.repository.FileRepository;
import com.web.sukusuku.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileRepository fileRepository;

    // 파일 저장 경로 (경로는 환경에 맞게 수정!)
    private final String uploadDir = "C:/upload"; 

    @Override
    public UploadFile saveFile(MultipartFile file, Post post) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalStateException("파일이 비어 있습니다!");
        }

        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String storedFilename = uuid + "_" + originalFilename;

        File savePath = new File(uploadDir, storedFilename);
        if (!savePath.getParentFile().exists()) {
            savePath.getParentFile().mkdirs();
        }

        file.transferTo(savePath);

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(originalFilename)
                .storedFileName(storedFilename)
                .filePath(savePath.getAbsolutePath())
                .post(post)
                .build();

        return fileRepository.save(uploadFile);
    }

    @Override
    public List<UploadFile> getFilesByPost(Long postId) {
        return fileRepository.findByPostId(postId);
    }

    @Override
    public UploadFile getFile(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new RuntimeException("파일 없음"));
    }

    @Override
    public void deleteFile(Long fileId) {
        UploadFile uploadFile = getFile(fileId);
        File file = new File(uploadFile.getFilePath());
        if (file.exists()) {
            file.delete();
        }
        fileRepository.delete(uploadFile);
    }
}