package com.dasunhq.application.service.cart;

import com.dasunhq.application.model.CartItem;

public interface ICartItemService {
    void addCartItemToCart(Long cartId, Long productId, int quantity);
    void removeCartItemFromCart(Long cartId, Long productId);
    void updateCartItemQuantity(Long cartId, Long productId, int quantity);


    CartItem getCartItem(Long cartId, Long productId);
}
