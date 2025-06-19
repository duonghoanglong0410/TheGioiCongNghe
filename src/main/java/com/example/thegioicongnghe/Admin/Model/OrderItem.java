package com.example.thegioicongnghe.Admin.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "OrderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;  // Khóa chính cho mục đơn hàng

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;  // Đơn hàng mà mục này thuộc về

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // Sản phẩm trong mục đơn hàng

    @Column(nullable = false)
    private int quantity;  // Số lượng sản phẩm

    @Column(nullable = false)
    private BigDecimal unitPrice;  // Giá mỗi sản phẩm

    // Constructor
    public OrderItem() {
    }

    // Getters và Setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
