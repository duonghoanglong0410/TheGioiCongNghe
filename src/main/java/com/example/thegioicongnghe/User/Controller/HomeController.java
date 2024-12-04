package com.example.thegioicongnghe.User.Controller;

import com.example.thegioicongnghe.Admin.Model.*;
import com.example.thegioicongnghe.Admin.Service.*;
import com.example.thegioicongnghe.Admin.Service.Impl.CartServiceImpl;
import com.example.thegioicongnghe.config.CustomUserDetails;
import com.example.thegioicongnghe.config.SecurityConfig;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller("userHomeController")  // Chỉ định HomeController là Controller
@RequestMapping("/")  // Chỉ định HomeController là Controller
public class HomeController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PostService postService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductReviewService productReviewService;
    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Khi user truy cập vào endpoint / thì homepage() được gọi
    @GetMapping("/")
    public String homepage(Model model, Principal p) {

        List<ProductCategory> categories = productCategoryService.findAll();

        List<Product> products = productService.findAll();

        List<Product> latestProducts = productService.getLatestProducts(); // lấy ra 10 sản phẩm mới nhất
        model.addAttribute("latestProducts", latestProducts);

        model.addAttribute("products", products); // hiển thị sản phẩm
        model.addAttribute("categories", categories); // hiển thị danh sách danh mục

        // Dropdown giỏ hàng
        UserDtls user = getLoggedInUserDetails(p);
        List<CartItem> carts = cartService.getCartsByUser(user.getId());
        model.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalPrice();
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        model.addAttribute("content", "user/fragments/cart_dropdown"); // Thêm dòng này

        model.addAttribute("content", "user/fragments/explore_product"); // truyền dữ liệu vào view
        model.addAttribute("content", "user/fragments/categories"); // Thêm dòng này
        return "user/index"; // Trả về layout với nội dung
    }


    @GetMapping("/product-detail/{productSlug}") // Trang chi tiết sản phẩm
    public String showProductDetail(@PathVariable String productSlug ,Principal p, Model model) {
        Product product = productService.findProductBySlug(productSlug);
        model.addAttribute("product", product);

        List<ProductCategory> categories = productCategoryService.findAll();
        model.addAttribute("categories", categories); // hiển thị danh sách danh mục

        // Lấy danh sách review từ cơ sở dữ liệu
        List<ProductReview> reviews = productReviewService.getReviewsByProductId(product.getProductId());
        // Đếm số lượng review
        int reviewCount = reviews.size();

        // Lấy danh sách User
        List<UserDtls> user = userService.findAll();
        model.addAttribute("user", user);

        // Tính trung bình rating (nếu có đánh giá)
        double averageRating = 0;
        if (!reviews.isEmpty()) {
            double totalRating = reviews.stream().mapToInt(ProductReview::getRating).sum();
            averageRating = totalRating / reviews.size();
        }

        // Tìm đánh giá của người dùng hiện tại
        ProductReview userReview = reviews.stream()
                .filter(review -> review.getUser().equals(user))
                .findFirst()
                .orElse(null);

        // Thêm vào model để truyền sang view
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewCount", reviewCount); // Thêm số lượng review vào model
        model.addAttribute("userReview", userReview);
        model.addAttribute("averageRating", averageRating); // Thêm trung bình rating vào model
        //Tối ưu SEO
        model.addAttribute("metaTitle", product.getMetaTitle()); // Thêm meta_title vào model
        model.addAttribute("metaDescription", product.getMetaDescription()); // Thêm meta_description vào model

        // Dropdown giỏ hàng
        UserDtls users = getLoggedInUserDetails(p);
        List<CartItem> carts = cartService.getCartsByUser(users.getId());
        model.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalPrice();
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        model.addAttribute("content", "user/fragments/cart_dropdown"); // Thêm dòng này

        model.addAttribute("content", "user/fragments/single-product"); // Thêm dòng này
        return "layouts/main_layout"; // Trả về layout với nội dung

        // model.addAttribute("content", "user/fragments/single-product"); // Thêm dòng này
    }

    @GetMapping("/posts") // Trang chi tiết sản phẩm
    public String postPage(Model model, Principal p) {

        List<BlogPost> posts = postService.findAll();
        model.addAttribute("posts", posts);

        List<BlogPost> latestPosts = postService.getLatestBlogPosts(); // Lấy ra 3 posts mới nhất
        model.addAttribute("latestPosts", latestPosts);

        // Thêm tiêu đề trang
        model.addAttribute("pageTitle", "Danh sách bài viết - TheGioiCongNghe.vn");

        // Dropdown giỏ hàng
        UserDtls user = getLoggedInUserDetails(p);
        List<CartItem> carts = cartService.getCartsByUser(user.getId());
        model.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalPrice();
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        model.addAttribute("content", "user/fragments/cart_dropdown"); // Thêm dòng này

        model.addAttribute("content", "user/post"); // Thêm dòng này
        return "layouts/main_layout"; // Trả về layout với nội dung
    }

    @GetMapping("/post-details/{slug}")
    public String showPostsDetail(@PathVariable String slug,  Model model, Principal p) {

        BlogPost post = postService.findPostBySlug(slug);

        // Kiểm tra slug để đảm bảo URL chính xác
        if (post == null || !post.getSlug().equals(slug)) {
            return "redirect:/404"; // Điều hướng đến trang lỗi 404 nếu không khớp
        }

        model.addAttribute("post", post);

        List<BlogPost> latestPosts = postService.getLatestBlogPosts(); // Lấy ra 3 posts mới nhất
        model.addAttribute("latestPosts", latestPosts);
        //Tối ưu SEO
        model.addAttribute("metaTitle", post.getMetaTitle()); // Thêm meta_title vào model
        model.addAttribute("metaDescription", post.getMetaDescription()); // Thêm meta_description vào model

        // Dropdown giỏ hàng
        UserDtls user = getLoggedInUserDetails(p);
        List<CartItem> carts = cartService.getCartsByUser(user.getId());
        model.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalPrice();
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        model.addAttribute("content", "user/fragments/cart_dropdown"); // Thêm dòng này

        model.addAttribute("content", "user/single-post"); // Thêm dòng này
        return "layouts/main_layout"; // Trả về layout với nội dung
    }

    @GetMapping("/shop") // Trang chi tiết sản phẩm
    public String shopPage(Model model, Principal p) {

        //Lấy và hiển thị danh sách sản phẩm
        List<Product> allProducts = productService.findAll();
        model.addAttribute("allProducts", allProducts);

        //Lấy và hiển thị danh sách danh mục sản phẩm
        List<ProductCategory> allCategory = productCategoryService.findAll();
        model.addAttribute("allCategory", allCategory);

        // Dropdown giỏ hàng
        UserDtls user = getLoggedInUserDetails(p);
        List<CartItem> carts = cartService.getCartsByUser(user.getId());
        model.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalPrice();
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        model.addAttribute("content", "user/fragments/cart_dropdown"); // Thêm dòng này

        model.addAttribute("content", "user/shop"); // Thêm dòng này
        return "layouts/main_layout"; // Trả về layout với nội dung
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model, Principal p) {
        List<Product> products = productService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);

        // Dropdown giỏ hàng
        UserDtls user = getLoggedInUserDetails(p);
        List<CartItem> carts = cartService.getCartsByUser(user.getId());
        model.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalPrice();
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        model.addAttribute("content", "user/fragments/cart_dropdown"); // Thêm dòng này

        model.addAttribute("content", "user/shop"); // Thêm dòng này
        return "layouts/main_layout"; // Trả về layout với nội dung
    }

    private UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/add-to-cart")
    public String addToCart(@RequestParam Integer pid, HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Lấy userId từ CustomUserDetails
        int userId = userDetails.getUserId();

        // Thực hiện thêm sản phẩm vào giỏ hàng
        CartItem savecart = cartService.saveCart(pid, userId);

        if (savecart == null) {
            session.setAttribute("errorMsg", "Product add to cart failed");
        } else {
            session.setAttribute("succMsg", "Product added to cart");
        }

        return "user/shop";
    }

    @GetMapping("/cart-review")
    public String loadCartReviewPage(Principal p, Model m) {

        UserDtls user = getLoggedInUserDetails(p);
        List<CartItem> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalPrice();
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        m.addAttribute("content", "user/fragments/cart_dropdown"); // Thêm dòng này
        return "layouts/main_layout";
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m) {
        UserDtls user = getLoggedInUserDetails(p);
        List<CartItem> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalPrice();
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        m.addAttribute("content", "user/cart"); // Thêm dòng này
        return "layouts/main_layout"; // Trả về layout với nội dung
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid) {
        cartService.updateQuantity(sy, cid);
        return "redirect:/cart";
    }


    // Xóa sản phẩm khỏi giỏ
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam int productId, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            return "Cart is empty!";
        }

        session.setAttribute("cart", cart);

        return "Product removed from cart!";
    }

    // Xóa toàn bộ giỏ hàng
    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "Cart cleared!";
    }
    @GetMapping("/checkout")
    public String checkOut(Principal p, Model model) {
        // Lấy thông tin user
        UserDtls user = getLoggedInUserDetails(p);
        List<CartItem> carts = cartService.getCartsByUser(user.getId());
        // Lấy danh sách các mục giỏ hàng của user
        // Tính tổng giá trị giỏ hàng
        double totalPrice = carts.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        // Đưa thông tin user và giỏ hàng vào model để hiển thị trong giao diện
        model.addAttribute("user", user);
        model.addAttribute("cartItems", carts);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("content", "user/checkout"); // Thêm dòng này
        return "layouts/main_layout"; // Trả về layout với nội dung
    }
    @PostMapping("/save-order")
    public String saveOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                            Principal p,
                            @RequestParam String address,
                            @RequestParam String hoTen,
                            @RequestParam String phone,
                            @RequestParam String note) {
        // 1. Lấy thông tin người dùng
        UserDtls user = getLoggedInUserDetails(p);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 2. Lấy danh sách sản phẩm trong giỏ hàng
        List<CartItem> carts = cartService.getCartsByUser(user.getId());
        if (carts.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng trống");
        }

        // 3. Tính tổng giá trị đơn hàng
        double totalPrice = carts.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        // 4. Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setHoTen(hoTen);
        order.setPhone(phone);
        order.setNote(note);
        order.setTotalPrice(BigDecimal.valueOf(totalPrice));
        order.setOrderStatus("Đang xử lý");
        orderService.save(order);

        // 5. Lưu sản phẩm vào OrderItem
        for (CartItem cartItem : carts) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // Gắn đơn hàng
            orderItem.setProduct(cartItem.getProduct()); // Lấy sản phẩm từ giỏ hàng
            orderItem.setQuantity(cartItem.getQuantity()); // Lấy số lượng từ giỏ hàng
            orderItem.setUnitPrice(BigDecimal.valueOf(cartItem.getTotalPrice())); // Lấy giá từ giỏ hàng

            // Lưu OrderItem vào cơ sở dữ liệu
            orderItemService.save(orderItem); // Đảm bảo bạn có OrderItemService để lưu
        }


        // 6. Xóa giỏ hàng
        cartService.clearCart(user.getId());

        return "redirect:/order-success";
    }

    @PostMapping("/save-product-rating")
    public String saveProductRating(
            @RequestParam("productId") Integer productId,
            @RequestParam("rating") int rating,
            @RequestParam("reviewText") String reviewText,
            Principal p,
            Model model) {

        // 1. Xác định người dùng hiện tại
        UserDtls user = getLoggedInUserDetails(p);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        // 2. Lấy thông tin sản phẩm từ productId
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);

        // 3. Tạo đánh giá sản phẩm
        ProductReview review = new ProductReview();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setReviewText(reviewText);

        // Debug log
        System.out.println("Product ID: " + productId);
        System.out.println("Rating: " + rating);
        System.out.println("Review Text: " + reviewText);

        // 4. Lưu đánh giá
        productReviewService.saveReview(review);

        // Gửi thông báo hoặc chuyển hướng
        return "user/shop";
    }



    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDtls());
        return "user/register";
    }

    // Xử lý dữ liệu từ form
    @PostMapping("/register")
    public String saveUser(
            @ModelAttribute UserDtls user,
            @RequestParam("Image") MultipartFile Image,
            HttpSession session) throws IOException {

        // Kiểm tra email đã tồn tại
        if (userService.existsEmail(user.getEmail())) {
            session.setAttribute("errorMsg", "Email already exists");
            return "redirect:/register";
        } else {
            String imageName = "default.jpg";
            if (!Image.isEmpty()) {
                imageName = Image.getOriginalFilename();
                user.setProfileImage(imageName);

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
            userService.saveUser(user);
        }
        return "user/login";
    }


    // Hiển thị form đăng nhập
    @GetMapping("/signin")
    public String showLoginForm() {
        return "user/login";
    }

    // kiểm tra người dùng đã đăng nhập hay chưa
    @GetMapping("/current-user")
    public String getCurrentUser(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            return "user/user"; // trả về tên của template (VD: user.html)
        }
        return "user/login"; // hoặc một trang mặc định khác
    }
    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            m.addAttribute("userDtls", userDtls);
        }
    }
    @GetMapping("/my-account")
    public String getMyAccount(Model model, @AuthenticationPrincipal UserDetails userDetails, UserDtls userDtls) {
        if (userDetails != null) {
            model.addAttribute("user", userDetails.getUsername());
            model.addAttribute("name", userDtls.getName());
            model.addAttribute("content", "user/my-account"); // Thêm dòng này
            return "layouts/main_layout"; // Trả về layout với nội dung
        }
        return "user/login";
    }
}