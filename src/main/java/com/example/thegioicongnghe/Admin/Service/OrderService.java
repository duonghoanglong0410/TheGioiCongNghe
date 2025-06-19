package com.example.thegioicongnghe.Admin.Service;

import com.example.thegioicongnghe.Admin.Model.*;
import com.example.thegioicongnghe.Admin.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Order> findAll(){
        return orderRepository.findAll();
    }
    public Optional<Order> findOrderById(int id) {
        return orderRepository.findById(id);
    }
    public Order saveEdit(Order order) {
        return orderRepository.save(order);
    }
    public void deleteById(int id) {
        orderRepository.deleteById(id);
    }
    @Transactional
    public void createOrder(Integer userid) {
        // Lấy danh sách sản phẩm trong giỏ hàng của người dùng
        List<CartItem> cartItems = cartItemRepository.findByUserId(userid);
        for (CartItem cartItem : cartItems) {

            if (cartItems.isEmpty()) {
                throw new IllegalArgumentException("Giỏ hàng trống!");
            }

            // Tính tổng giá trị đơn hàng
            BigDecimal totalPrice = BigDecimal.ZERO;
                totalPrice = totalPrice.add(BigDecimal.valueOf(cartItem.getTotalPrice()));
                // Tạo đơn hàng
                Order order = new Order();
            order.setOrderStatus("Đang xử lý");
            order.setTotalPrice(totalPrice);
            order.setHoTen(order.getHoTen());
            order.setAddress(order.getAddress());
            order.setPhone(order.getPhone());
            order.setNote(order.getNote());
            order.setUser(cartItem.getUser());
            orderRepository.save(order);
            orderRepository.flush();

            // Lưu từng mục đơn hàng
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnitPrice(BigDecimal.valueOf(cartItem.getPrice()));

                orderItemRepository.save(orderItem);
                orderItemRepository.flush();

            // Xóa giỏ hàng sau khi thanh toán
            cartItemRepository.deleteAll(cartItems);
        }
    }
    public void saveOrder(Integer userid) throws Exception {

        List<CartItem> carts = cartItemRepository.findByUserId(userid);

        for (CartItem cart : carts) {

            Order order = new Order();

            order.setOrderId(Integer.parseInt(UUID.randomUUID().toString()));
            order.setOrderStatus("Đang xử lý!");
            order.setAddress(order.getAddress());
            order.setHoTen(order.getHoTen());
            order.setPhone(order.getPhone());
            order.setNote(order.getNote());
            order.setUser(cart.getUser());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orderItem.getOrder());
            orderItem.setProduct(cart.getProduct());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setUnitPrice(BigDecimal.valueOf(cart.getTotalPrice()));

            Order saveOrder = orderRepository.save(order);
        }
    }
    public void save(Order order) {
        orderRepository.save(order);
    }


}
