package com.web.sukusuku.posts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.web.sukusuku.files.UploadFile;
import com.web.sukusuku.users.User;

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

    @GetMapping("/list")
    public String getPostList(@RequestParam(name = "category", required = false) Category category,
    		@RequestParam(name = "keyword", required = false) String keyword,
                              @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size,
                              Model model,
                              HttpSession session) {
    	// 테스트용임시로그인 
    	User tempUser = new User();
    	    tempUser.setUsername("테스트유저");
    	    session.setAttribute("loginUser", tempUser);
        
    	    
    	Page<Post> posts = postService.getPosts(category, keyword, sort, page, size);

        int totalPageCount = posts.getTotalPages();
        int pagePerGroup = 5; // 몇 개의 페이지를 그룹으로 묶을지
        int currentPage = posts.getNumber();
        int startPageGroup = (currentPage / pagePerGroup) * pagePerGroup;
        int endPageGroup = Math.min(startPageGroup + pagePerGroup - 1, totalPageCount - 1);

        PageNavigationDto navi = PageNavigationDto.builder()
                .currentPage(currentPage)
                .pagePerGroup(pagePerGroup)
                .startPageGroup(startPageGroup)
                .endPageGroup(endPageGroup)
                .totalPageCount(totalPageCount)
                .build();

        model.addAttribute("posts", posts);
        model.addAttribute("navi", navi);

        log.info("게시판 리스트 출력 성공!");

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

        PostViewDto response = PostViewDto.builder()
            .id(post.getId())
            .category(post.getCategory())
            .title(post.getTitle())
            .content(post.getContent())
            .author(post.getAuthor())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .views(post.getViews())
            .isAuthor(isAuthor)
            .build();

        model.addAttribute("post", response);

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
                             @Valid @RequestBody PostUpdateDto request,
                             HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/users/login";
        }

        Post post = postService.getPostById(postId);

        if (!post.getAuthor().equals(loginUser.getUsername())) {
            return "redirect:/posts/list";
        }

        postService.updatePost(postId, request);

        return "redirect:/posts/view/" + postId;
    }


    @PostMapping("/{postId}/delete")
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

}