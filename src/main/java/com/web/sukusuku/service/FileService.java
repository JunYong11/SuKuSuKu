package com.web.sukusuku.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.web.sukusuku.model.Post;
import com.web.sukusuku.model.UploadFile;

public interface FileService {
	   UploadFile saveFile(MultipartFile file, Post post) throws IOException;
	   List<UploadFile> getFilesByPost(Long postId);
	   UploadFile getFile(Long fileId);
	   void deleteFile(Long fileId);
	   
}
