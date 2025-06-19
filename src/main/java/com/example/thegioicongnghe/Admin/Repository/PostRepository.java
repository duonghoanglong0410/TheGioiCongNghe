package com.example.thegioicongnghe.Admin.Repository;

import com.example.thegioicongnghe.Admin.Model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<BlogPost, Integer> {
    Optional<BlogPost> findBySlug(String slug);

    boolean existsBySlug(String slug);
    // Lấy 3 bài viết mới nhất dựa trên createdAt
    @Query("SELECT b FROM BlogPost b ORDER BY b.createdAt DESC")
    List<BlogPost> findTop3ByOrderByCreatedAtDesc();
}
