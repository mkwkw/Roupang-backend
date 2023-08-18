package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.dynamicMessageException.CustomMessageException;
import com.teamsupercat.roupangbackend.dto.cart.request.PurchaseItemRequest;
import com.teamsupercat.roupangbackend.entity.*;
import com.teamsupercat.roupangbackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartOrderService {

    private final SingleOrderRepository singleOrderRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final GroupedOrderRepository groupedOrderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;


    // todo: 장바구니 상품값을 가져와서 Single_orders테이블에 넣고 idx값을 가지고 Grouped_order 테이블을 만들고 Grouped_order 테이블 고유번호를 뽑는다
    @Transactional
    public void purchaseItemsFromCart(Integer memberId, List<PurchaseItemRequest> requests) {
        Member bymember = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_USER));
        Instant now = Instant.now();
        // Grouped_order 테이블 식별값
        String groupedId = bymember.getNickname() + now;
        long productAllAmount = 0;// 구매수량*상품가격 = 총 결제금액
        // requests담을 리스트선언
        List<SingleOrder> singleOrders = new ArrayList<>();


        for (PurchaseItemRequest request : requests) {
            Product product = productRepository.findById(request.getProductIdx()).orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOTFOUND));
            SingleOrder singleOrder = new SingleOrder();
            singleOrder.setMemberIdx(bymember);
            singleOrder.setProductIdx(product);
            singleOrder.setAmount(request.getAmount());
            singleOrder.setOrderDate(now);
            singleOrder.setIsDeleted(false);
            singleOrders.add(singleOrder);
        }
        List<SingleOrder> bySaveAll = singleOrderRepository.saveAll(singleOrders);

        // saveAll 에 보내줄 리스트 생성
        List<GroupedOrder> groupedOrderList = new ArrayList<>();

        // bySaveAll 리스트만큼 order 값을 set 해줌
        for (SingleOrder order : bySaveAll) {
            // SingleOrder 데이터를 객체로 받아옵니다.
            SingleOrder singleOrder = singleOrderRepository.findById(order.getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SINGLE_ORDER));
            // 리스트형태가 아닌 GroupedOrder 객체를 선언
            GroupedOrder groupedOrder = new GroupedOrder();
            // 데이터 하나씩 추가
            groupedOrder.setSingleOrders(singleOrder);
            groupedOrder.setGroupedId(groupedId);

            groupedOrderList.add(groupedOrder);

            productAllAmount += groupedOrder.getSingleOrders().getProductIdx().getPrice() * groupedOrder.getSingleOrders().getAmount();
        }
        groupedOrderRepository.saveAll(groupedOrderList);

        payment(groupedId, productAllAmount);


    }

    @Transactional
    public void payment(String groupedId, long productAllAmount) {
        // 그룹 묶음 하나만
        GroupedOrder groupedOrder = groupedOrderRepository.findFirstByGroupedId(groupedId);
        // 그룹 묶음 전체
        List<GroupedOrder> groupedOrderList = groupedOrderRepository.findByGroupedId(groupedId);
        // 유저 객체
        Member member = memberRepository.findById(groupedOrder.getSingleOrders().getMemberIdx().getId()).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_USER));
        // 결제수단 1.슈퍼포인트결제
        PaymentMethod paymentMethod = paymentMethodRepository.findById(1).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUNT_PAY));


        // 주문하려는 상품의 낱개 갯수 파악

        Payment payment = new Payment();
        Member bymember = new Member();

        payment.setMemberIdx(member);
        payment.setGroupedOrderIdx(groupedOrder);
        payment.setPaymentMethodsIdx(paymentMethod);
        payment.setPaymentTotalAmount(productAllAmount);

        bymember.setId(member.getId());
        bymember.setNickname(member.getNickname());
        bymember.setEmail(member.getEmail());
        bymember.setUserPassword(member.getUserPassword());
        bymember.setPhoneNumber(member.getPhoneNumber());
        bymember.setAddress(member.getAddress());
        bymember.setMemberImg(member.getMemberImg());
        bymember.setCreatedAt(member.getCreatedAt());
        bymember.setUpdatedAt(member.getUpdatedAt());
        bymember.setIsDeleted(member.getIsDeleted());
        Long minusPoint = member.getUserPoint() - productAllAmount;
        bymember.setUserPoint(minusPoint);

        // 결제 후 유저포인트가 0보다 많은지 확인
        if (minusPoint < 0) {
            throw new CustomMessageException(ErrorCode.LACKING_USER_POINT, "유저 잔여 포인트", String.valueOf(member.getUserPoint()));
        }


        // 장바구니 상품마다 돌면서 구매수량을 뽑아 상품의 재고를 차감한다
        // 차감 시 재고가 0이랑 같거나 적으면 적으면 예외처리
        for (GroupedOrder order : groupedOrderList) {
            Product product = productRepository.findById(order.getSingleOrders().getProductIdx().getId()).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_PRODUCT));

            Integer stock = order.getSingleOrders().getProductIdx().getStock();
            Integer amount = order.getSingleOrders().getAmount();

            // 상품 제고 수량 파악해서 재고가 없으면 예외처리
            Integer updatedStock = stock - amount;
            if (updatedStock < 0) {
                throw new CustomMessageException(ErrorCode.NOT_STOCK_PRODUCT, product.getProductName() + " 상품의 재고수량:", String.valueOf(stock));
            }
            product.setStock(updatedStock);
            productRepository.save(product);
        }
        // todo 포인트거래내역 테이블 거쳐서 포인트를 차감해야함
        // 유저 포인트 차감
        memberRepository.save(bymember);

        // 결제테이블 결제내역 추가
        paymentRepository.save(payment);

        // 결제 후 카트 비우기
        List<Cart> byMemberIdx = cartRepository.findByMemberIdxAndIsDeleted(member);
        for (Cart cart : byMemberIdx) {
            cart.setIsDeleted(true);
            cartRepository.save(cart);
        }
    }
}
