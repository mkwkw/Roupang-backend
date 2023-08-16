package com.teamsupercat.roupangbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Point_transaction", schema = "supercat")
public class PointTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_idx", nullable = false)
    private Member memberIdx;

    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;

    @Column(name = "transaction_amount", nullable = false)
    private Long transactionAmount;

    @Column(name = "transaction_date", nullable = false)
    private Instant transactionDate;

    @Column(name = "remaining_points", nullable = false)
    private Long remainingPoints;

}