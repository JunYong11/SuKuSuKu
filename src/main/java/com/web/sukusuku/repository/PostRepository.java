package com.web.sukusuku.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.sukusuku.model.Category;
import com.web.sukusuku.model.Post;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long> {
   @Query("select p from Post p order by p.createdAt desc")
   List<Post> findAllPosts();

   @Query("SELECT p FROM Post p WHERE p.category = :category OR p.user.username = 'admin' " +
             "ORDER BY CASE WHEN p.user.username = 'admin' THEN 0 ELSE 1 END, p.createdAt DESC")
      Page<Post> findWithAdminPinnedByRecent(@Param("category") Category category, Pageable pageable);

      @Query("SELECT p FROM Post p WHERE p.category = :category OR p.user.username = 'admin' " +
             "ORDER BY CASE WHEN p.user.username = 'admin' THEN 0 ELSE 1 END, p.createdAt ASC")
      Page<Post> findWithAdminPinnedByOldest(@Param("category") Category category, Pageable pageable);

      @Query("SELECT p FROM Post p WHERE p.category = :category OR p.user.username = 'admin' " +
             "ORDER BY CASE WHEN p.user.username = 'admin' THEN 0 ELSE 1 END, p.views DESC")
      Page<Post> findWithAdminPinnedByViews(@Param("category") Category category, Pageable pageable);

    Page<Post> findByCategoryAndTitleContainingOrCategoryAndContentContaining(
            Category category1, String keyword1,
            Category category2, String keyword2,
            Pageable pageable
        );
    Page<Post> findByCategoryAndContentContaining(
          Category category, String keyword, Pageable pageable);
    Page<Post> findByCategoryAndAuthorContaining(
          Category category, String keyword, Pageable pageable);
    // 카테고리별 게시글 목록 가져오기
    Page<Post> findByCategory(Category category, Pageable pageable);

   Page<Post> findByCategoryAndTitleContaining(Category category, String keyword, Pageable pageable);


}
