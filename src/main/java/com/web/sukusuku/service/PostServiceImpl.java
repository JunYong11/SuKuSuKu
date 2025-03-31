package com.web.sukusuku.service;

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

import com.web.sukusuku.dto.PostCreateDto;
import com.web.sukusuku.dto.PostUpdateDto;
import com.web.sukusuku.model.Category;
import com.web.sukusuku.model.Post;
import com.web.sukusuku.model.UploadFile;
import com.web.sukusuku.model.User;
import com.web.sukusuku.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final FileService fileService;
    private final PostRepository postRepository;

    @Override
    public Page<Post> getPosts(Category category, String keyword, String searchType, String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, getSort(sort));

        if (keyword != null && !keyword.isEmpty()) {
            switch (searchType) {
                case "content":
                    return postRepository.findByCategoryAndContentContaining(category, keyword, pageable);
                case "author":
                    return postRepository.findByCategoryAndAuthorContaining(category, keyword, pageable);
                case "title":
                default:
                    return postRepository.findByCategoryAndTitleContaining(category, keyword, pageable);
            }
        }

        return postRepository.findByCategory(category, pageable);
    }

    	
    private Sort getSort(String sort) {
        if (sort == null) sort = "recent"; // 기본값 세팅

        switch (sort.toLowerCase()) {
            case "views":
                return Sort.by(Sort.Direction.DESC, "views");	
            case "oldest":
                return Sort.by(Sort.Direction.ASC, "createdAt");
            case "recent":
            default:
                return Sort.by(Sort.Direction.DESC, "createdAt");
        }
    }



    @Override
    @Transactional
    public Post savePost(Post post, List<MultipartFile> files) throws IOException {

        List<UploadFile> uploadFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                UploadFile uploadFile = fileService.saveFile(file, post);

                if (uploadFile == null) {
                    throw new RuntimeException("파일 업로드에 실패했습니다.");
                }

                uploadFile.setPost(post);
                uploadFiles.add(uploadFile);
            }
        }
        
        post.setFiles(uploadFiles);

        return postRepository.save(post);
    }

    
    @Override
    public Post readPost(Long postId) {
        System.out.println("읽어오는 postId: " + postId);

        return postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }


    @Override
    @Transactional
    public void updatePost(Long postId, PostUpdateDto request, List<MultipartFile> files) throws IOException{
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        // 1) 기본 수정
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());

        // 비밀글 설정
        post.setSecret(request.isSecret());

        // 비밀번호 처리
        if (request.isSecret()) {
            if (request.getSecretPassword() != null && !request.getSecretPassword().isEmpty()) {
                post.setSecretPassword(request.getSecretPassword());
            }
        } else {
            post.setSecretPassword(null);
        }

        // 2) 새로운 파일 업로드 처리
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // 예: fileService.saveFile(file, post) 로 실제 파일 저장 + UploadFile 엔티티 생성
                    UploadFile uploadFile = fileService.saveFile(file, post);

                    // Post 엔티티와 파일 연관관계 설정 (예: post.addFile(uploadFile))
                    post.addFile(uploadFile);
                }
            }
        }

        // 3) @Transactional로 인해 DB 반영 (Dirty Checking)
        // postRepository.save(post); // 호출해도 상관없지만, 생략 가능
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

    @Override
    @Transactional
    public void increaseViews(Post post) {
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }
    @Override
    @Transactional
    public void createPost(PostCreateDto postForm, List<MultipartFile> files, User user) throws IOException {
    	 Post post = postForm.toEntity(user.getUsername());
    	 post.setUser(user);
        // 첨부파일 처리
        List<UploadFile> uploadFiles = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    UploadFile uploadFile = fileService.saveFile(file, post);
                    uploadFile.setPost(post);  // 연관관계 주입
                    uploadFiles.add(uploadFile);
                }
            }
        }

        post.setFiles(uploadFiles);

        postRepository.save(post);
    }



}
	