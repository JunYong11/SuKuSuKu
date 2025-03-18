package com.web.sukusuku.files;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.sukusuku.posts.Post;

import lombok.RequiredArgsConstructor;

public interface FileService {
	  UploadFile storeFile(MultipartFile multipartFile) throws IOException;

	  UploadFile getFileById(Long id);

	  void deleteFile(Long id);

}
