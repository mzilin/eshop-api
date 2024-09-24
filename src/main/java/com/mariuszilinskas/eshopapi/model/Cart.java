package com.mariuszilinskas.eshopapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> products = new ArrayList<>();

    @Column
    private boolean checkedOut;

    @Column
    private BigDecimal totalCost;

    @Column
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column
    private ZonedDateTime updatedAt = ZonedDateTime.now();

}
