package com.example.thegioicongnghe.Admin.Service;

import com.example.thegioicongnghe.Admin.Model.OrderItem;
import com.example.thegioicongnghe.Admin.Repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItem> findOrderItemsByOrderId(int orderId) {
        return orderItemRepository.findByOrder_OrderId(orderId);
    }

    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }
}
