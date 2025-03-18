package com.web.sukusuku.posts;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long> {
	@Query("select p from Post p order by p.createdAt desc")
	List<Post> findAllPosts();
	
    Page<Post> findByCategoryAndTitleContainingOrCategoryAndContentContaining(
            Category category1, String keyword1,
            Category category2, String keyword2,
            Pageable pageable
        );

    // 카테고리별 게시글 목록 가져오기
    Page<Post> findByCategory(Category category, Pageable pageable);
}
