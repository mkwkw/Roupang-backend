package com.teamsupercat.roupangbackend.dto.payment.request;

import com.teamsupercat.roupangbackend.entity.DeliveryAddress;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private DeliveryAddressRequest deliveryAddress;
    private List<CartItemRequest> cartItems;


    public DeliveryAddress deliveryToEntity(DeliveryAddressRequest request) {
        return DeliveryAddress.builder()
                .recipient(request.getRecipient())
                .recipientPhone(request.getRecipientPhone())
                .address(request.getAddress())
                .recipientEmail(request.getRecipientEmail())
                .deliveryMemo(request.getDeliveryMemo())
                .build();
    }




}
