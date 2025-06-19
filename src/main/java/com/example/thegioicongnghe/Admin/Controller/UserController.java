package com.example.thegioicongnghe.Admin.Controller;

import com.example.thegioicongnghe.Admin.Model.UserDtls;
import com.example.thegioicongnghe.Admin.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
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
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String getListUserForm(Model model) {
        List<UserDtls> user = userService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("content", "admin/users/list"); // Thêm dòng này
        return "layouts/admin_layout"; // Trả về layout với nội dung
    }
    @GetMapping("/add")
    public String getFormAdd(Model model) {
        model.addAttribute("user", new UserDtls());
        model.addAttribute("content", "admin/users/add"); // Thêm dòng này
        return "layouts/admin_layout"; // Trả về layout với nội dung
    }
    @PostMapping("/add")
    public String addUser(@ModelAttribute UserDtls userDtls,
                          @RequestParam("Image")MultipartFile Image,
                          HttpSession session) throws IOException {
        // Kiểm tra email đã tồn tại
        if (userService.existsEmail(userDtls.getEmail())) {
            session.setAttribute("errorMsg", "Email already exists");
            return "redirect:/admin/add";
        } else {
            String imageName = "default.jpg";
            if (!Image.isEmpty()) {
                imageName = Image.getOriginalFilename();
                userDtls.setProfileImage(imageName);

                // Thư mục bên ngoài `resources`
                File uploadsDir = new File("uploads/profile_img");

                // Tạo thư mục nếu chưa tồn tại
                if (!uploadsDir.exists()) {
                    uploadsDir.mkdirs();
                }

                // Đường dẫn lưu file
                Path path = Paths.get(uploadsDir.getAbsolutePath() + File.separator + Image.getOriginalFilename());

                // Lưu file
                Files.copy(Image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            userService.saveUser(userDtls);
        }
        return "redirect:/admin/user/list";
    }
    @GetMapping("/edit/{id}")
    public String getEditForm(@PathVariable int id, Model model) {
        UserDtls userDtls = (UserDtls) userService.findUserById(id).orElse(null);
        model.addAttribute("userDtls", userDtls);
        model.addAttribute("content", "admin/users/edit");
        return "layouts/admin_layout";
    }
    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable int id,
                           @ModelAttribute UserDtls userDtls,
                           @RequestParam("Image") MultipartFile Image) throws IOException {
        UserDtls user = (UserDtls) userService.findUserById(id).orElse(null);
        if (user == null) {
            return "redirect:/admin/user/list";
        }
        user.setName(userDtls.getName());
        user.setEmail(userDtls.getEmail());
        user.setAddress(userDtls.getAddress());
        user.setRole(userDtls.getRole());

        if (!Image.isEmpty()) {
            String mainImageName = Image.getOriginalFilename();
            Path mainImagePath = Paths.get("uploads/profile_img/" + mainImageName);
            Files.copy(Image.getInputStream(), mainImagePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfileImage(mainImageName); // Cập nhật hình ảnh chính
        }
            user.setId(id);
            userService.update(user);
            return "redirect:/admin/user/list";
        }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteById(id);
        return "redirect:/admin/user/list";
    }
}
