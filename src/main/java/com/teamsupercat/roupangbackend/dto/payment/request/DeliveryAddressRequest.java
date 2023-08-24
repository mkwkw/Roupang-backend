package com.teamsupercat.roupangbackend.dto.payment.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAddressRequest {
    private String recipient;
    private String recipientPhone;
    private String address;
    private String recipientEmail;
    private String deliveryMemo;
}