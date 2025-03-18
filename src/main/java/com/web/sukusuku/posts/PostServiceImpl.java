package com.web.sukusuku.posts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.web.sukusuku.files.FileRepository;
import com.web.sukusuku.files.FileService;
import com.web.sukusuku.files.UploadFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final FileService fileService;
    private final PostRepository postRepository;

    @Override
    public Page<Post> getPosts(Category category, String keyword, String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, getSort(sort));

        if (keyword != null && !keyword.isEmpty()) {
            return postRepository.findByCategoryAndTitleContainingOrCategoryAndContentContaining(
                category, keyword, category, keyword, pageable);
        }

        return postRepository.findByCategory(category, pageable);
    }

    private Sort getSort(String sort) {
        return switch (sort) {
            case "old" -> Sort.by("createdAt").ascending();
            case "popular" -> Sort.by("views").descending();
            default -> Sort.by("createdAt").descending();
        };
    }

    @Override
    public Post savePost(Post post, List<MultipartFile> files) throws IOException {


        List<UploadFile> uploadFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                UploadFile uploadFile = fileService.storeFile(file);
                uploadFile.setPost(post); // 연관 관계 세팅
                uploadFiles.add(uploadFile);
            }
        }

        post.setFiles(uploadFiles);
        return postRepository.save(post);
    }
    
    @Override
    public Post readPost(Long postId) {
        System.out.println("읽어오는 postId: " + postId);

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        post.setViews(post.getViews() + 1);
        return postRepository.save(post);
    }

    @Override
    public void updatePost(Long postId, PostUpdateDto request) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        post.setCategory(request.getCategory());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSecret(request.isSecret());

        postRepository.save(post);
    }

    @Override
    public void removePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        
        // 파일명 중복 방지를 위해 UUID 사용
        String storedFilename = UUID.randomUUID() + "_" + originalFilename;

        // 저장 경로 세팅 (네가 원하는 폴더로!)
        String uploadPath = "/upload-dir/" + storedFilename;

        // 실제 파일 저장
        multipartFile.transferTo(new File(uploadPath));

        return UploadFile.builder()
                .originalFileName(originalFilename)
                .storedFileName(storedFilename)
                .filePath(uploadPath)
                .build();
    }

}
