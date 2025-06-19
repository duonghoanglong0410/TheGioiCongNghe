package com.example.thegioicongnghe.Admin.Controller;

import com.example.thegioicongnghe.Admin.Model.Order;
import com.example.thegioicongnghe.Admin.Model.OrderItem;
import com.example.thegioicongnghe.Admin.Repository.OrderRepository;
import com.example.thegioicongnghe.Admin.Service.OrderItemService;
import com.example.thegioicongnghe.Admin.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @GetMapping("/list")
    public String listOrder(Model model) {
        List<Order> orderList = orderService.findAll();
        model.addAttribute("orderList", orderList);
        model.addAttribute("content", "admin/orders/list"); // Thêm dòng này
        return "layouts/admin_layout"; // Trả về layout với nội dung

    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Order order = orderService.findOrderById(id).orElse(null);
        model.addAttribute("order", order);
        model.addAttribute("content", "admin/orders/edit");
        return "layouts/admin_layout";
    }
    @PostMapping("/edit/{id}")
    public String editOrder(@PathVariable int id, @ModelAttribute Order order) {
        Order orderEdit = orderService.findOrderById(id).orElse(null);
        if (orderEdit == null) {
            return "redirect:/admin/order/list";
        }
        orderEdit.setOrderStatus(order.getOrderStatus());
        orderService.saveEdit(orderEdit);
        return "redirect:/admin/order/list";
    }
    @GetMapping("/order-item/{id}")
    public String showOrderItem(@PathVariable int id, Model model) {
        Order order = orderService.findOrderById(id).orElse(null);
        if (order != null) {
            List<OrderItem> orderItems = orderItemService.findOrderItemsByOrderId(id);
            model.addAttribute("orderItems", orderItems);
        }
        model.addAttribute("order", order);
        model.addAttribute("content", "admin/orders/order-item");
        return "layouts/admin_layout";
    }
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable int id) {
        orderService.deleteById(id);
        return "redirect:/admin/order/list";
    }
}
