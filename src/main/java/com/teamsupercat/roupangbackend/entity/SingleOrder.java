package com.teamsupercat.roupangbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Single_orders", schema = "supercat")
public class SingleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_idx", nullable = false)
    private Member memberIdx;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_idx", nullable = false)
    private Product productIdx;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}