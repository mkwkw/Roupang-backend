package com.teamsupercat.roupangbackend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Timestamp salesEndDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;


}