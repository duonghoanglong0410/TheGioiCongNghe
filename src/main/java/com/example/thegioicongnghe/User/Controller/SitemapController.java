package com.example.thegioicongnghe.User.Controller;

import com.example.thegioicongnghe.Admin.Model.BlogPost;
import com.example.thegioicongnghe.Admin.Model.Product;
import com.example.thegioicongnghe.Admin.Service.PostService;
import com.example.thegioicongnghe.Admin.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Controller
public class SitemapController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PostService postService;

    // Đặt lịch để tạo sitemap mỗi ngày vào lúc 00h00 (mỗi đêm)
    @Scheduled(cron = "0 0 0 * * ?") // Mỗi ngày lúc 00:00 (12:00 AM)
    @GetMapping("/generate-sitemap")
    public String generateSitemap() throws IOException {
        // Lấy tất cả các sản phẩm và bài viết từ database
        List<Product> products = productService.findAll();
        List<BlogPost> posts = postService.findAll();

        // Tạo sitemap
        StringBuilder sitemap = new StringBuilder();
        sitemap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sitemap.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Thêm trang chủ vào sitemap
        sitemap.append("<url>\n");
        sitemap.append("<loc>https://www.TheGioiCongNghe.vn/</loc>\n");
        sitemap.append("<lastmod>2024-12-07</lastmod>\n");  // Update lastmod tùy theo thời gian cập nhật
        sitemap.append("<priority>1.00</priority>\n");
        sitemap.append("</url>\n");

        // Thêm URL của sản phẩm vào sitemap
        for (Product product : products) {
            sitemap.append("<url>\n");
            sitemap.append("<loc>https://www.TheGioiCongNghe.vn/product-detail/" + product.getProductSlug() + "</loc>\n");
            sitemap.append("<lastmod>" + product.getCreatedAt() + "</lastmod>\n");
            sitemap.append("<priority>0.80</priority>\n");
            sitemap.append("</url>\n");
        }

        // Thêm URL của bài viết vào sitemap
        for (BlogPost post : posts) {
            sitemap.append("<url>\n");
            sitemap.append("<loc>https://www.TheGioiCongNghe.vn/post-details/" + post.getSlug() + "</loc>\n");
            sitemap.append("<lastmod>" + post.getUpdatedAt() + "</lastmod>\n");
            sitemap.append("<priority>0.70</priority>\n");
            sitemap.append("</url>\n");
        }

        // Thêm các trang phụ khác vào sitemap
        sitemap.append("<url>\n");
        sitemap.append("<loc>https://www.TheGioiCongNghe.vn/shop</loc>\n");
        sitemap.append("<lastmod>2024-12-07</lastmod>\n");
        sitemap.append("<priority>0.90</priority>\n");
        sitemap.append("</url>\n");

        sitemap.append("<url>\n");
        sitemap.append("<loc>https://www.TheGioiCongNghe.vn/cart</loc>\n");
        sitemap.append("<lastmod>2024-12-07</lastmod>\n");
        sitemap.append("<priority>0.80</priority>\n");
        sitemap.append("</url>\n");

        sitemap.append("<url>\n");
        sitemap.append("<loc>https://www.TheGioiCongNghe.vn/checkout</loc>\n");
        sitemap.append("<lastmod>2024-12-07</lastmod>\n");
        sitemap.append("<priority>0.80</priority>\n");
        sitemap.append("</url>\n");

        sitemap.append("<url>\n");
        sitemap.append("<loc>https://www.TheGioiCongNghe.vn/register</loc>\n");
        sitemap.append("<lastmod>2024-12-07</lastmod>\n");
        sitemap.append("<priority>0.60</priority>\n");
        sitemap.append("</url>\n");

        sitemap.append("<url>\n");
        sitemap.append("<loc>https://www.TheGioiCongNghe.vn/signin</loc>\n");
        sitemap.append("<lastmod>2024-12-07</lastmod>\n");
        sitemap.append("<priority>0.60</priority>\n");
        sitemap.append("</url>\n");

        // Thêm trang giỏ hàng và thông báo thành công
        sitemap.append("<url>\n");
        sitemap.append("<loc>https://www.TheGioiCongNghe.vn/cart-review</loc>\n");
        sitemap.append("<lastmod>2024-12-07</lastmod>\n");
        sitemap.append("<priority>0.80</priority>\n");
        sitemap.append("</url>\n");

        sitemap.append("<url>\n");
        sitemap.append("<loc>https://www.TheGioiCongNghe.vn/404</loc>\n");
        sitemap.append("<lastmod>2024-12-07</lastmod>\n");
        sitemap.append("<priority>0.50</priority>\n");
        sitemap.append("</url>\n");

        sitemap.append("</urlset>");

        // Lưu sitemap vào file
        File file = new File("sitemap.xml");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(sitemap.toString());
        }
        System.out.println("Sitemap saved at: " + file.getAbsolutePath());
        return "/sitemap-success";  // Đảm bảo bạn có template hoặc một URL dẫn đến trang thành công
    }
}
