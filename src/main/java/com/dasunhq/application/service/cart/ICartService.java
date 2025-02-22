package com.dasunhq.application.service.cart;

import com.dasunhq.application.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long cartId);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long cartId);
}
