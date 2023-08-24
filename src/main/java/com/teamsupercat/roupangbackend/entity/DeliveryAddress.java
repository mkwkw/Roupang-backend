package com.teamsupercat.roupangbackend.entity;


import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Delivery_address", schema = "supercat")
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "recipient", nullable = false)
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

