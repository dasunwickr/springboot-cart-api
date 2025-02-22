package com.dasunhq.application.repository;

import com.dasunhq.application.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
