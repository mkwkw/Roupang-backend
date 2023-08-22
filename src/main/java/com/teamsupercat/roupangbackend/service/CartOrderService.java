package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.dynamicMessageException.CustomMessageException;
import com.teamsupercat.roupangbackend.dto.order.request.PurchaseItemRequest;
import com.teamsupercat.roupangbackend.dto.order.response.PurchaseItemResponse;
import com.teamsupercat.roupangbackend.entity.*;
import com.teamsupercat.roupangbackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<PurchaseItemResponse> purchaseItemsFromCart(Member member, List<PurchaseItemRequest> requests) {

        PurchaseItemResponse purchaseItemResponse = new PurchaseItemResponse();

        Instant now = Instant.now();
        // Grouped_order 테이블 식별값
        String groupedId = member.getNickname() + now;
        long productAllPrice = 0;// 구매수량*상품가격 = 총 결제금액
        // request 담을 리스트선언
        List<SingleOrder> singleOrderList = new ArrayList<>();

        //받아온 값을 Single_order 테이블에 넣어주는 작업
        for (PurchaseItemRequest request : requests) {
            // 실제 상품검색
            Product product = productRepository.findById(request.getProductIdx()).orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));
            // 데이터 담을 객체 생성
            SingleOrder singleOrder = new SingleOrder();
            singleOrder.setMemberIdx(member);
            singleOrder.setProductIdx(product);
            singleOrder.setAmount(request.getAmount());
            // TODO: 상품옵션도 추가할 예정
            singleOrder.setOrderDate(now);
            singleOrder.setIsDeleted(false);
            // set 한 객체를 SingleOrder리스트에 담기
            singleOrderList.add(singleOrder);
        }
        // 저장 후 저장한 테이블을 가져옴
        List<SingleOrder> singleOrders = singleOrderRepository.saveAll(singleOrderList);
        // response 로 형변환
        List<PurchaseItemResponse> collect = singleOrders.stream().map(purchaseItemResponse::toSingleOrderList).collect(Collectors.toList());

        return collect;


 /*
        // GroupOrder리스트 생성
        List<GroupedOrder> groupedOrderList = new ArrayList<>();

      // singleOrders 리스트만큼 order 값을 set 해줌
        for (SingleOrder order : singleOrders) {
            // 리스트형태가 아닌 GroupedOrder 객체를 선언
            GroupedOrder groupedOrder = new GroupedOrder();
            // 데이터 하나씩 추가
            groupedOrder.setSingleOrders(order);
            // 위에서 생성한 유저닉네임+현재시간을 groupedId로 만들어서 추가
            groupedOrder.setGroupedId(groupedId);
            // set 한 값을 리스트에 추가
            groupedOrderList.add(groupedOrder);

            // 장바구니에 담긴 상품각각의 가격+수량 계산하여 장바구니 총 결제금액을 만든다
            productAllPrice += groupedOrder.getSingleOrders().getProductIdx().getPrice() * groupedOrder.getSingleOrders().getAmount();
        }
        // 저장
        groupedOrderRepository.saveAll(groupedOrderList);
*/

        // 보류 나중에 사용할 수 있음 payment(groupedId, productAllPrice);
    }

    // 결제확인화면 유저기본정보 표시, 주소,전화번호 등등
    public PurchaseItemResponse purchaseMemberFromCart(Member member) {
        PurchaseItemResponse purchaseItemResponse = new PurchaseItemResponse();
        return purchaseItemResponse.toMember(member);
    }

  /*  @Transactional
    public void payment(String groupedId, long productAllAmount) {
        // 그룹 묶음 하나만
        GroupedOrder groupedOrder = groupedOrderRepository.findFirstByGroupedId(groupedId);
        // 그룹 묶음 전체
        List<GroupedOrder> groupedOrderList = groupedOrderRepository.findByGroupedId(groupedId);
        // 유저 객체
        Member member = memberRepository.findById(groupedOrder.getSingleOrders().getMemberIdx().getId()).orElseThrow(() -> new CustomException(ErrorCode.CART_USER_NOT_FOUND));
        // 결제수단 1.슈퍼포인트결제
        PaymentMethod paymentMethod = paymentMethodRepository.findById(1).orElseThrow(() -> new CustomException(ErrorCode.CART_PAYMENT_METHOD_NOT_FOUND));


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
            throw new CustomMessageException(ErrorCode.CART_ORDER_INSUFFICIENT_USER_POINT, "유저 잔여 포인트", String.valueOf(member.getUserPoint()));
        }


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
*/

}
