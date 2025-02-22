package com.dasunhq.application.service.cart;

import com.dasunhq.application.exceptions.ResourceNotFoundException;
import com.dasunhq.application.model.Cart;
import com.dasunhq.application.model.CartItem;
import com.dasunhq.application.model.Product;
import com.dasunhq.application.repository.CartItemRepository;
import com.dasunhq.application.repository.CartRepository;
import com.dasunhq.application.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;


    @Override
    public void addCartItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);

        CartItem cartItem = getCartItem(cartId, productId);

        if (cartItem.getProductId() == null){
            cartItem.setCart(cart);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cartItemRepository.save(cartItem);
        cartRepository.delete(cart);
    }

    @Override
    public void removeCartItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeCartItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getCartItems().stream().filter(itm -> itm.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(itm -> {
                    itm.setQuantity(quantity);
                    itm.setUnitPrice(itm.getProduct().getPrice());
                    itm.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}
