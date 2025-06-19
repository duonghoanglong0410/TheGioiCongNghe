package com.example.thegioicongnghe.Admin.Service.Impl;
import com.example.thegioicongnghe.Admin.Model.CartItem;
import com.example.thegioicongnghe.Admin.Model.Product;
import com.example.thegioicongnghe.Admin.Model.UserDtls;
import com.example.thegioicongnghe.Admin.Repository.CartRepository;
import com.example.thegioicongnghe.Admin.Repository.ProductRepository;
import com.example.thegioicongnghe.Admin.Repository.UserRepository;
import com.example.thegioicongnghe.Admin.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;
    /*
    *
        *
    }
    *     public Optional<CartItem> findById(int id) {
        return cartRepository.findById(id);
    }
    public void deleteById(int id) {
        cartRepository.deleteById(id);
    }
    * */
    public Optional<CartItem> findByProductId(Integer productId, Integer userId) {
        return cartRepository.findById(productId);
    }
    @Override
    public int countCartByUser(Integer userId) {
        return cartRepository.countByUserId(userId); // Đếm số lượng sản phẩm trong giỏ hàng
    }

    public List<CartItem> findAll() {
        return cartRepository.findAll();
    }
    @Override
    public CartItem saveCart(Integer productId, Integer userId) {

        UserDtls user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Optional<CartItem> cartStatus = findByProductId(productId, userId);

        CartItem cart = null;

        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new CartItem();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getPrice().doubleValue());
        } else {
            cart = cartStatus.get();
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(product.getPrice().doubleValue() * cart.getQuantity());
        }
        CartItem saveCart = cartRepository.save(cart);

        return saveCart;
    }
    @Override
    public List<CartItem> getCartsByUser(Integer userId) {
        List<CartItem> carts = cartRepository.findByUserId(userId);

        Double totalOrderPrice = 0.0;
        List<CartItem> updateCarts = new ArrayList<>();
        for (CartItem c : carts) {
            Double totalPrice = (c.getProduct().getPrice().doubleValue() * c.getQuantity());
            c.setTotalPrice(totalPrice);
            totalOrderPrice = totalOrderPrice + totalPrice;
            c.setTotalPrice(totalOrderPrice);
            updateCarts.add(c);
        }

        return updateCarts;
    }

    public Integer getCountCart(Integer userId) {
        Integer countByUserId = cartRepository.countByUserId(userId);
        return countByUserId;
    }

    public void updateQuantity(String sy, Integer cid) {

        CartItem cart = cartRepository.findById(cid).get();
        int updateQuantity;

        if (sy.equalsIgnoreCase("de")) {
            updateQuantity = cart.getQuantity() - 1;

            if (updateQuantity <= 0) {
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(updateQuantity);
                cartRepository.save(cart);
            }

        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            cartRepository.save(cart);
        }

    }
    // Phương thức xóa giỏ hàng của người dùng
    public Integer clearCart(Integer userId) {
        cartRepository.deleteByUserId(userId);
        return userId;
    }
}
