package com.example.thegioicongnghe.Admin.Controller;

import com.example.thegioicongnghe.Admin.Model.BlogPost;
import com.example.thegioicongnghe.Admin.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin/posts")
public class PostController {
    @Autowired
    private PostService postService;

    //Hiển thị danh sách Posts
    @GetMapping("/list")
    public String listPost(Model model) {
        List<BlogPost> posts = postService.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("content", "admin/posts/list"); // Thêm dòng này
        return "layouts/admin_layout"; // Trả về layout với nội dung
    }

    // Trang thêm posts mới
    @GetMapping("/add")
    public String showAddPostForm(Model model) {
        model.addAttribute("post", new BlogPost());
        model.addAttribute("content", "admin/posts/add"); // Thêm dòng này
        return "layouts/admin_layout"; // Trả về layout với nội dung
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute BlogPost post,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        String imageName = "default.jpg";
        if (!imageFile.isEmpty()) {
            imageName = imageFile.getOriginalFilename();
            post.setImage(imageName);

            // Thư mục bên ngoài `resources`
            File uploadsDir = new File("uploads/posts_img");

            // Tạo thư mục nếu chưa tồn tại
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
            }

            // Đường dẫn lưu file
            Path path = Paths.get(uploadsDir.getAbsolutePath() + File.separator + imageFile.getOriginalFilename());

            // Lưu file
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        // Lưu category vào database
        postService.addPost(post);
        return "redirect:/admin/posts/list";
    }

    // Trang sửa danh mục
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        BlogPost posts = postService.findById(id).orElse(null);
        model.addAttribute("posts", posts);

        model.addAttribute("content", "admin/posts/edit");
        return "layouts/admin_layout";
    }

    // Cập nhật danh mục
    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable int id,
                               @ModelAttribute BlogPost post,
                               @RequestParam("imageFile") MultipartFile imageFile ) throws IOException {

        // Tìm bài viết cần sửa
        BlogPost existingPost = postService.findById(id).orElse(null);
        if (existingPost == null) {
            return "redirect:/admin/posts/list"; // Nếu không tìm thấy, quay về danh sách
        }

        // Cập nhật thông tin
        existingPost.setTitle(post.getTitle());
        existingPost.setSlug(post.getSlug());
        existingPost.setContent(post.getContent());
        existingPost.setMetaTitle(post.getMetaTitle());
        existingPost.setMetaDescription(post.getMetaDescription());

        // Xử lý icon mới
        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();

            // Thư mục bên ngoài `resources`
            File uploadsDir = new File("uploads/posts_img");

            // Tạo thư mục nếu chưa tồn tại
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
            }

            // Đường dẫn lưu file
            Path path = Paths.get(uploadsDir.getAbsolutePath() + File.separator + imageName);

            // Lưu file
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Cập nhật tên file icon
            existingPost.setImage(imageName);
        }

        post.setPostId(id); // Đảm bảo cập nhật đúng ID
        // Lưu danh mục vào database
        postService.addPost(existingPost);

        return "redirect:/admin/posts/list";
    }

}
