package com.mariuszilinskas.eshopapi.repository;

import com.mariuszilinskas.eshopapi.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
}
