package com.example.thegioicongnghe.Admin.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(unique = true, nullable = false, length = 255)
    private String productSlug; // URL thân thiện cho SEO

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    private int stockQuantity;

    private String metaTitle; // Tiêu đề SEO
    private String metaDescription; // Mô tả SEO

    @Column(name = "keywords")
    private String keywords;  // Keyword

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category; // Danh mục của sản phẩm

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>(); // Hình ảnh của sản phẩm

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductReview> reviews = new HashSet<>(); // Đánh giá của sản phẩm

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>(); // Mục đơn hàng

    @Column(nullable = false)
    private String imageUrl; // URL hình ảnh chính của sản phẩm

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt; // Thời gian tạo

    @Transient
    private List<String> imageUrls; // Danh sách URL hình ảnh tạm thời

    // Constructor
    public Product() {
        this.createdAt = new Timestamp(System.currentTimeMillis()); // Đặt thời gian tạo mặc định
    }

    // Getters và Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public void setProductSlug(String productSlug) {
        this.productSlug = productSlug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
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

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }

    public Set<ProductReview> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ProductReview> reviews) {
        this.reviews = reviews;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Thêm phương thức trợ giúp cho tập hợp images
    public void addImage(ProductImage image) {
        if (images == null) {
            images = new HashSet<>();
        }
        images.add(image);
        image.setProduct(this); // Liên kết hai chiều
    }

    public void removeImage(ProductImage image) {
        if (images != null) {
            images.remove(image);
            image.setProduct(null); // Ngắt liên kết hai chiều
        }
    }
}
