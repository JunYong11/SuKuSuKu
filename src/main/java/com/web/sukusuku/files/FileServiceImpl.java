package com.web.sukusuku.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.sukusuku.files.UploadFile;
import com.web.sukusuku.posts.Post;
import com.web.sukusuku.posts.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	
	private final FileRepository fileRepository;
	private final PostRepository postRepository;
	
	private final String uploadDir = "/upload-dir";  // 실제 서버의 폴더 경로 (수정 가능)
	
	
	@Override
	public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
	    String originalFilename = multipartFile.getOriginalFilename();
	    String storeFileName = UUID.randomUUID() + "_" + originalFilename;

	    String filePath = uploadDir + "/" + storeFileName;
	    
	    // 로그 확인!
	    System.out.println("파일 저장 경로: " + filePath);

	    File dir = new File(uploadDir);
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }

	    multipartFile.transferTo(new File(filePath));

	    UploadFile uploadFile = UploadFile.builder()
	            .originalFileName(originalFilename)
	            .storedFileName(storeFileName)
	            .filePath(filePath)
	            .build();

	    return fileRepository.save(uploadFile);
	}


    @Override
    public UploadFile getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("파일이 존재하지 않습니다."));
    }

    @Override
    public void deleteFile(Long id) {
        UploadFile file = getFileById(id);
        File physicalFile = new File(file.getFilePath());

        if (physicalFile.exists()) physicalFile.delete();

        fileRepository.delete(file);
    }
    
}

