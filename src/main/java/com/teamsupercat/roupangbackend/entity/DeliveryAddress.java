package com.teamsupercat.roupangbackend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Delivery_address", schema = "supercat")
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx",nullable = false)
    private Integer idx;

    @Column(nullable = false)
    private String recipient;

    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "recipient_email")
    private String recipientEmail;

    @Column(name = "delivery_memo")
    private String deliveryMemo;

}

