package com.example.thegioicongnghe.Admin.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")  // Chỉ định HomeController là Controller
public class HomeController {
    // Khi user truy cập vào endpoint / thì homepage() được gọi
    @GetMapping("")
    public String homepage(Authentication authentication) {
        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated()) {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
            return "redirect:/signin";  // Chuyển hướng đến trang login
        }
        return "layouts/admin_layout";  // Trả về trang index.html
    }

    // Có thể mapping thêm các endpoint khác nữa...
}