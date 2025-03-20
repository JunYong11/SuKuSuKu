package com.web.sukusuku.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.web.sukusuku.model.UploadFile;

public interface FileService {
	  UploadFile storeFile(MultipartFile multipartFile) throws IOException;

	  UploadFile getFileById(Long id);

	  void deleteFile(Long id);

}
