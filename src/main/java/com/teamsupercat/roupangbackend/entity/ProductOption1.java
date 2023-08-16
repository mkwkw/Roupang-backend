package com.teamsupercat.roupangbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Product_option1", schema = "supercat")
public class ProductOption1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_category_idx", nullable = false)
    private ProductsCategory productCategoryIdx;

    @Column(name = "option1_name", nullable = false)
    private String option1Name;

    @Column(name = "option2_value", nullable = false)
    private String option2Value;

}