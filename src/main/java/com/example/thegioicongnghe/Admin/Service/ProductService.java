package com.example.thegioicongnghe.Admin.Service;

import com.example.thegioicongnghe.Admin.Model.Product;
import com.example.thegioicongnghe.Admin.Model.ProductCategory;
import com.example.thegioicongnghe.Admin.Model.ProductImage;
import com.example.thegioicongnghe.Admin.Repository.ProductImageRepository;
import com.example.thegioicongnghe.Admin.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;


    public String generateUniqueSlug(String title) {
        String baseSlug = SlugUtils.toSlug(title); // Tạo slug cơ bản từ tiêu đề
        String productSlug = baseSlug;
        int count = 1;

        // Kiểm tra xem slug đã tồn tại trong cơ sở dữ liệu chưa
        while (productRepository.existsByProductSlug(productSlug)) {
            productSlug = baseSlug + "-" + count; // Thêm số đằng sau
            count++;
        }

        return productSlug;
    }

    /*
    * public Product addProduct(Product product) {

        // Lưu sản phẩm vào cơ sở dữ liệu
        Product savedProduct = productRepository.save(product);

        // Lưu các hình ảnh liên quan nếu có
        if (product.getImageUrls() != null) {
            for (String imageUrl : product.getImageUrls()) {
                ProductImage image = new ProductImage();
                image.setImageUrl(imageUrl);
                image.setProduct(savedProduct);
                productImageRepository.save(image);
            }
        }
        return savedProduct;
    }*/
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Tìm kiếm sản phẩm theo tên
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }

    // Tìm sản phẩm theo ID
    public Optional<Product> findById(int id) {
        return Optional.ofNullable(productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id)));
    }

    public Product findProductBySlug(String productSlug) {
        return productRepository.findByproductSlug(productSlug).orElse(null);
    }

    // Lấy danh sách sản phẩm theo Slug danh mục
    public List<Product> findProductsByCategory(ProductCategory productCategory) {
        return productRepository.findByCategory(productCategory);
    }

    public Product save(Product product) {
        // Tạo slug duy nhất
        String uniqueSlug = generateUniqueSlug(product.getProductName());
        product.setProductSlug(uniqueSlug);

        // Kiểm tra và xử lý giá trị imageUrl
        if (product.getImageUrl() == null || product.getImageUrl().isEmpty()) {
            // Gán ảnh mặc định nếu imageUrl null hoặc rỗng
            product.setImageUrl("default-image.jpg");
        }

        return productRepository.save(product);
    }


    public void deleteById(int id) {
        productRepository.deleteById(id);
    }
    // Lấy hình ảnh theo ID
    public ProductImage findImageById(int id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hình ảnh với ID: " + id));
    }

    // Lưu hình ảnh
    public ProductImage saveImage(ProductImage image) {
        return productImageRepository.save(image);
    }

    public void deleteProductImage(int imageId) {
        Optional<ProductImage> productImage = productImageRepository.findById(imageId);
        if (productImage.isPresent()) {
            productImageRepository.delete(productImage.get());
        }
    }

    public List<Product> getLatestProducts() {
        return productRepository.findTop10ByOrderByCreatedAtDesc();
    }

    // Xóa hình ảnh theo ID
    public void deleteImageById(int id) {
        productImageRepository.deleteById(id);
    }
    // sử dụng cho trang chi tiết sản phẩm
    public Product getProductById(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với ID: " + productId));
    }

}
