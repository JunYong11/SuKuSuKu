package com.web.sukusuku.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.web.sukusuku.dto.PostUpdateDto;

import com.web.sukusuku.model.Category;
import com.web.sukusuku.model.Post;
import com.web.sukusuku.model.UploadFile;

public interface PostService {

    Page<Post> getPosts(Category category, String keyword, String sort, int page, int size);

    Post readPost(Long postId);

    void updatePost(Long postId, PostUpdateDto request);

    void removePost(Long postId);

    Post getPostById(Long postId);

	UploadFile storeFile(MultipartFile file) throws IOException;

	Post savePost(Post post, List<MultipartFile> files) throws IOException;

	
}
