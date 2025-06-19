package com.example.thegioicongnghe.Admin.Model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "BlogPosts")
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;  // Khóa chính cho bài viết

    @Column(nullable = false)
    private String title;  // Tiêu đề bài viết

    @Column(unique = true, nullable = false)
    private String slug;  // URL thân thiện cho bài viết

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;  // Nội dung bài viết

    @Column
    private String metaTitle;  // Tiêu đề SEO

    @Column(columnDefinition = "TEXT")
    private String metaDescription;  // Mô tả SEO

    @Column(updatable = false)
    private Timestamp createdAt;  // Thời gian tạo bài viết

    @Column
    private Timestamp updatedAt;  // Thời gian cập nhật bài viết
    @Column // Cột mới cho ảnh
    private String image;

    // Constructor
    public BlogPost() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters và Setters
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Cập nhật thời gian khi bài viết được chỉnh sửa
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
