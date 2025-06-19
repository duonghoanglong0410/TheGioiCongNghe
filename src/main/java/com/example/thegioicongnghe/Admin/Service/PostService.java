package com.example.thegioicongnghe.Admin.Service;

import com.example.thegioicongnghe.Admin.Model.BlogPost;
import com.example.thegioicongnghe.Admin.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    // Lấy ra 3 posts mới nhất
    public List<BlogPost> getLatestBlogPosts() {
        return postRepository.findTop3ByOrderByCreatedAtDesc();
    }

    public List<BlogPost> findAll() {
        return postRepository.findAll();
    }

    public Optional<BlogPost> findById(int id) {
        return postRepository.findById(id);
    }

    public BlogPost findPostBySlug(String slug) {
        return postRepository.findBySlug(slug).orElse(null);
    }



    public String generateUniqueSlug(String title) {
        String baseSlug = SlugUtils.toSlug(title); // Tạo slug cơ bản từ tiêu đề
        String slug = baseSlug;
        int count = 1;

        // Kiểm tra xem slug đã tồn tại trong cơ sở dữ liệu chưa
        while (postRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + count; // Thêm số đằng sau
            count++;
        }

        return slug;
    }

    public BlogPost addPost(BlogPost blogPost) {
        // Tạo slug duy nhất
        String uniqueSlug = generateUniqueSlug(blogPost.getTitle());
        blogPost.setSlug(uniqueSlug);
        return postRepository.save(blogPost);
    }

    public void deleteById(int id) {
        postRepository.deleteById(id);
    }
}
