package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.dynamicMessageException.CustomMessageException;
import com.teamsupercat.roupangbackend.dto.order.request.PurchaseItemRequest;
import com.teamsupercat.roupangbackend.dto.order.response.PurchaseItemResponse;
import com.teamsupercat.roupangbackend.dto.order.response.PurchaseMemberResponse;
import com.teamsupercat.roupangbackend.dto.payment.request.CartItemRequest;
import com.teamsupercat.roupangbackend.dto.payment.request.PaymentRequest;
import com.teamsupercat.roupangbackend.entity.*;
import com.teamsupercat.roupangbackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartOrderService {

    private final SingleOrderRepository singleOrderRepository;
    private final GroupedOrderRepository groupedOrderRepository;
    private final ProductRepository productRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;

    // todo: 장바구니 상품값을 가져와서 Single_orders테이블에 넣고 idx값을 가지고 Grouped_order 테이블을 만들고 Grouped_order 테이블 고유번호를 뽑는다
    @Transactional
    public PurchaseMemberResponse purchaseItemsFromCart(Member member, List<PurchaseItemRequest> requests) {
        PurchaseMemberResponse purchaseMemberResponse = new PurchaseMemberResponse();
        PurchaseItemResponse purchaseItemResponse = new PurchaseItemResponse();
        PurchaseItemRequest purchaseItemRequest = new PurchaseItemRequest();
        Instant now = Instant.now();

        // singleOrder 로 변환
        List<SingleOrder> singleOrderList = requests.stream().map(request -> {
            Product product = productRepository.findById(request.getProductIdx()).orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));
            return purchaseItemRequest.toEntity(member, request, product, now);
        }).collect(Collectors.toList());

        // 저장 후 저장한 테이블을 가져옴
        List<SingleOrder> singleOrders = singleOrderRepository.saveAll(singleOrderList);
        // response 로 형변환
        List<PurchaseItemResponse> collect = singleOrders.stream().map(purchaseItemResponse::toSingleOrderList).collect(Collectors.toList());

        PurchaseMemberResponse responseMemberAndItem = purchaseMemberResponse.toMember(member);
        responseMemberAndItem.setPurchaseItemResponseList(collect);

        return responseMemberAndItem;
    }

    //TODO: 결제상세화면 뿌려주기 완료 후 결제버튼 눌렀을 때 실행 로직 구현하기
    @Transactional
    public void paymentProcessing(Member member, PaymentRequest paymentRequest) {

        for (CartItemRequest request : paymentRequest.getCartItems()) {
            if (groupedOrderRepository.findById(request.getSingleOrderNum()).isPresent()) {
                throw new CustomException(ErrorCode.CART_ORDER_ALREADY_PURCHASED);
            }
        }

        // 총 결제금액
        Long paymentTotalAmount = 0L;
        // 배송지를 모두 디비에 저장한다
        DeliveryAddress saveDelivery = deliveryAddressRepository.save(paymentRequest.deliveryToEntity(paymentRequest.getDeliveryAddress()));
        // 그룹번호 생성
        String groupedOrderUniqueNum = member.getEmail() + Instant.now();
        List<GroupedOrder> groupedOrderList = new ArrayList<>();

        for (CartItemRequest itemRequest : paymentRequest.getCartItems()) {
            SingleOrder order = singleOrderRepository.findById(itemRequest.getSingleOrderNum()).orElseThrow(() -> new CustomException(ErrorCode.SHOP_PRODUCT_NOT_FOUND));
            //   총금액 = 상품가격 * 상품 수량
            paymentTotalAmount += order.getProductIdx().getPrice() * order.getAmount();
            groupedOrderList.add(itemRequest.toEntity(groupedOrderUniqueNum, order));
        }
        //


        // 결제 후 유저포인트가 0보다 많은지 확인
        if (member.getUserPoint() - paymentTotalAmount < 0) {
            throw new CustomMessageException(ErrorCode.CART_ORDER_INSUFFICIENT_USER_POINT, "유저 잔여 포인트", String.valueOf(member.getUserPoint()));
        }

        GroupedOrder groupedOrderNum = groupedOrderRepository.saveAll(groupedOrderList).get(0);
        // 결테이블 값 추가하기
        Payment payment = new Payment();
        payment.setMemberIdx(member);
        payment.setGroupedOrderIdx(groupedOrderNum);
        payment.setDeliveryAddress(saveDelivery);
        payment.setPaymentTotalAmount(paymentTotalAmount);
        payment.setPaymentDate(Instant.now());


        // 장바구니 상품마다 돌면서 구매수량을 뽑아 상품의 재고를 차감한다
        // 차감 시 재고가 0이랑 같거나 적으면 적으면 예외처리
        for (GroupedOrder order : groupedOrderList) {
            Product product = productRepository.findById(order.getSingleOrders().getProductIdx().getId()).orElseThrow(() -> new CustomException(ErrorCode.SHOP_PRODUCT_NOT_FOUND));

            Integer stock = order.getSingleOrders().getProductIdx().getStock();
            Integer amount = order.getSingleOrders().getAmount();

            // 상품 제고 수량 파악해서 재고가 없으면 예외처리
            Integer updatedStock = stock - amount;
            if (updatedStock < 0) {
                throw new CustomMessageException(ErrorCode.CART_ORDER_PRODUCT_OUT_OF_STOCK, product.getProductName() + " 상품의 재고수량:", String.valueOf(stock));
            }
            product.setStock(updatedStock);
            productRepository.save(product);
        }
        paymentRepository.save(payment);

        // 결제 후 카트 비우기
        List<Cart> byMemberIdx = cartRepository.findByMemberIdxAndIsDeleted(member);
        for (Cart cart : byMemberIdx) {
            cart.setIsDeleted(true);
            cartRepository.save(cart);
        }
    }
}
