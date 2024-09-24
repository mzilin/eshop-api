package com.mariuszilinskas.eshopapi.repository;

import com.mariuszilinskas.eshopapi.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    Optional<CartItem> findByProductId(int productId);

}
