package com.teamsupercat.roupangbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Product_option2", schema = "supercat")
public class ProductOption2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_category_idx", nullable = false)
    private ProductsCategory productCategoryIdx;

    @Column(name = "option2_name", nullable = false)
    private String option2Name;

    @Column(name = "option2_value", nullable = false)
    private String option2Value;

}