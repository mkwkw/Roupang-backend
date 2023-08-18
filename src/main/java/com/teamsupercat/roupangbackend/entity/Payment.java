package com.teamsupercat.roupangbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Payments", schema = "supercat")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_idx", nullable = false)
    private Member memberIdx;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grouped_order_idx", nullable = false)
    private GroupedOrder groupedOrderIdx;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_methods_idx", nullable = false)
    private PaymentMethod paymentMethodsIdx;

    @Column(name = "payment_total_amount", nullable = false)
    private Long paymentTotalAmount;

}