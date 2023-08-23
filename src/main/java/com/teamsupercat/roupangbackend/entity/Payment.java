package com.teamsupercat.roupangbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

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
    @JoinColumn(name = "delivery_address_idx", nullable = false)
    private DeliveryAddress deliveryAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grouped_order_idx", nullable = false)
    private GroupedOrder groupedOrderIdx;

    @Column(name = "payment_total_amount", nullable = false)
    private Long paymentTotalAmount;

    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @Column(name = "cancellation_date")
    private Instant cancellationDate;

}