package com.example.thegioicongnghe.Admin.Service;

import com.example.thegioicongnghe.Admin.Model.ProductImage;
import com.example.thegioicongnghe.Admin.Repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public ProductImage saveImage(ProductImage image) {
        return productImageRepository.save(image);
    }

    public List<ProductImage> getImagesByProductId(int productId) {
        return productImageRepository.findByProduct_ProductId(productId);
    }

    public void deleteById(int id) {
        productImageRepository.deleteById(id);
    }

    // Lấy hình ảnh theo ID
    public ProductImage findImageById(int id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hình ảnh với ID: " + id));
    }
    // Xóa hình ảnh theo ID
    public void deleteImageById(int id) {
        productImageRepository.deleteById(id);
    }
}
