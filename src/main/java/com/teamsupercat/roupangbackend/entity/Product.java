package com.teamsupercat.roupangbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Product", schema = "supercat")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "products_category_idx", nullable = false)
    private ProductsCategory productsCategoryIdx;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_idx", nullable = false)
    private Seller sellerIdx;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Lob
    @Column(name = "product_img", nullable = false)
    private String productImg;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "description_img", nullable = false)
    private String descriptionImg;

    @Column(name = "price")
    private Long price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "sales_end_date")
    private Instant salesEndDate;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}