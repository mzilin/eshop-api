package com.mariuszilinskas.eshopapi.model;

import com.mariuszilinskas.eshopapi.enums.Label;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int product_id;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Column(name = "added_at")
    private LocalDate addedAt;

    @ElementCollection(targetClass = Label.class)
    @Enumerated(EnumType.STRING)
    @Column
    private List<Label> labels;

}
