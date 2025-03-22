package com.web.sukusuku.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.web.sukusuku.dto.PageNavigationDto;
import com.web.sukusuku.dto.PostCreateDto;
import com.web.sukusuku.dto.PostUpdateDto;
import com.web.sukusuku.dto.PostViewDto;
import com.web.sukusuku.model.Category;
import com.web.sukusuku.model.Comment;
import com.web.sukusuku.model.Post;
import com.web.sukusuku.model.UploadFile;
import com.web.sukusuku.model.User;
import com.web.sukusuku.service.CommentService;
import com.web.sukusuku.service.FileService;
import com.web.sukusuku.service.PostService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final FileService fileService;
    private final CommentService commentService;
    @GetMapping("/list")
    public String getPostList(@RequestParam(name = "category", required = false) Category category,
                              @RequestParam(name = "keyword", required = false) String keyword,
                              @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size,
                              Model model,
                              HttpSession session) {

        // ✅ category가 null일 때 기본값을 자유게시판으로 설정
        if (category == null) {
            category = Category.FREE;
        }

        // ✅ 게시글 가져오기
        Page<Post> posts = postService.getPosts(category, keyword, sort, page, size);

        // ✅ 페이징 계산
        int totalPageCount = posts.getTotalPages();
        int pagePerGroup = 5;
        int currentPage = posts.getNumber();

        int startPageGroup = 0;
        int endPageGroup = 0;

        // ✅ 게시글이 있을 때만 페이지네이션 계산
        if (totalPageCount > 0) {
            startPageGroup = (currentPage / pagePerGroup) * pagePerGroup;
            endPageGroup = Math.min(startPageGroup + pagePerGroup - 1, totalPageCount - 1);

            if (page < 0) page = 0;
            if (page >= totalPageCount) page = totalPageCount - 1;
        }

        PageNavigationDto navi = PageNavigationDto.builder()
                .currentPage(currentPage)
                .pagePerGroup(pagePerGroup)
                .startPageGroup(startPageGroup)
                .endPageGroup(endPageGroup)
                .totalPageCount(totalPageCount)
                .build();

        // ✅ 카테고리 이름 (한글 표시용)
        String categoryName = "";
        switch (category) {
            case FREE: categoryName = "자유 게시판"; break;
            case QUESTION: categoryName = "질문 게시판"; break;
            case DATA: categoryName = "자료 게시판"; break;
        }

        // ✅ 모델에 값 넣기
        model.addAttribute("posts", posts);
        model.addAttribute("navi", navi);
        model.addAttribute("category", category); // 선택한 카테고리 값
        model.addAttribute("categoryName", categoryName); // 제목에 표시할 이름
        model.addAttribute("sort", sort);

        log.info(categoryName + " 리스트 출력 성공!");

        // ✅ templates/posts/list.html 로 연결
        return "posts/list";
    }



    
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Post>> getPostList(
        @PathVariable Category category,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false, defaultValue = "recent") String sort,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(postService.getPosts(category, keyword, sort, page, size));
    }
	
    @PostMapping("/create")
    public String createPost(
    		 @ModelAttribute @Valid PostCreateDto request,
    		 @RequestParam(value = "files", required = false) List<MultipartFile> files,
    		 HttpSession session) throws IOException{

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/users/login"; // 로그인 안 했으면 튕기기!
        }

        Post post = request.toEntity(loginUser.getUsername());
        
        post.setUser(loginUser);
        
        postService.savePost(post, files);

        return "redirect:/posts/list"; // 글 작성 성공 시 리스트로 이동
    }

    // 글쓰기 폼
    @GetMapping("/create")
    public String createForm() {
        return "posts/create";  // → templates/posts/createForm.html
    }
    
    
    @GetMapping("/view/{postId}")
    public String viewPost(@PathVariable Long postId, Model model, HttpSession session) {

        Post post = postService.readPost(postId);

        
        if (post == null) {
            return "redirect:/posts/list";
        }

        User loginUser = (User) session.getAttribute("loginUser");
        boolean isAuthor = loginUser != null && post.getAuthor().equals(loginUser.getUsername());
        
        List<Comment> comments = commentService.getCommentsByPostId(postId);

        // ✅ 비밀글일 경우 접근 제어
        if (post.isSecret() && !isAuthor) {
            return "redirect:/posts/list?error=permissionDenied";
        }

        // ✅ 조회수 증가 (옵션)
        postService.increaseViews(post);

        // ✅ DTO 변환
        PostViewDto response = PostViewDto.builder()
            .id(post.getId())
            .category(post.getCategory())
            .title(post.getTitle())
            .content(post.getContent())
            .author(post.getAuthor())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .views(post.getViews())
            .files(post.getFiles())
            .build();

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("post", response);
        model.addAttribute("comments", comments);
        model.addAttribute("isAuthor", isAuthor);

        return "posts/view";
    }



    // 수정 폼 보여주기
    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable Long postId, Model model, HttpSession session) {

        // 로그인 안 했으면 로그인 페이지로
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/users/login";
        }

        // 작성자 본인만 수정 가능
        Post post = postService.getPostById(postId);
        
        if (!post.getAuthor().equals(loginUser.getUsername())) {
            return "redirect:/posts/list";
        }

        // 수정 폼에 기본값으로 채울 데이터
        model.addAttribute("post", post);
        return "posts/edit"; // → templates/posts/edit.html
    }
    	
    @PostMapping("/{postId}/edit")
    public String updatePost(@PathVariable Long postId,
                             @ModelAttribute PostUpdateDto request,
                             HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/users/login";
        }

        Post post = postService.getPostById(postId);

        if (!post.getAuthor().equals(loginUser.getUsername())) {
            return "redirect:/posts/list?error=permissionDenied";
        }

        postService.updatePost(postId, request); // 수정 처리

        return "redirect:/posts/view/" + postId;
    }



    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable Long postId, HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/users/login";
        }

        Post post = postService.getPostById(postId);

        if (!post.getAuthor().equals(loginUser.getUsername())) {
            return "redirect:/posts/view/" + postId; // 삭제 권한 없을 경우 다시 상세보기로!
        }

        postService.removePost(postId);

        return "redirect:/posts/list"; // 삭제 성공하면 리스트로 이동
    }
    
    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> showImage(@PathVariable Long fileId) throws MalformedURLException {
        UploadFile file = fileService.getFile(fileId);

        Path path = Paths.get(file.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        String contentType = "image/jpeg"; // 기본값
        String filename = file.getOriginalFileName().toLowerCase();
        if (filename.endsWith(".png")) contentType = "image/png";
        if (filename.endsWith(".gif")) contentType = "image/gif";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }




}