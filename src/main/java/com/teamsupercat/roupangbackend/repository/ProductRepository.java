package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    Page<Product> findAllByIsDeletedAndSellerIdx(Boolean isDeleted, Seller seller, Pageable pageable);

    Page<Product> findProductByIsDeletedAndStockGreaterThanEqualAndSellerIdxOrderByPrice(Boolean isDeleted, Integer stock, Seller sellerId, Pageable pageable);

    Page<Product> findProductByIsDeletedAndStockGreaterThanEqualAndSellerIdxOrderByPriceDesc(Boolean isDeleted, Integer stock, Seller sellerId, Pageable pageable);

    Page<Product> findProductByIsDeletedAndStockGreaterThanEqualAndSellerIdxOrderByCreatedAtDesc(Boolean isDeleted, Integer stock, Seller sellerId, Pageable pageable);

    Page<Product> findProductByOrderByPrice(Pageable pageable);

    Page<Product> findProductByOrderByPriceDesc(Pageable pageable);

    Page<Product> findProductByProductsCategoryIdxId(Integer categoryIdx, Pageable pageable);

    List<Product> findProductByProductsCategoryIdxId(Integer categoryIdx);

    Page<Product> findProductByProductsCategoryIdxIdOrderByPrice(Integer categoryIdx, Pageable pageable);

    Page<Product> findProductByProductsCategoryIdxIdOrderByPriceDesc(Integer categoryIdx, Pageable pageable);


    Product findProductById(int productIdx);


    //:seller는 나중에 매개변수인 'seller' 변수로 대체되는 부분
    //LEFT JOIN: 왼쪽 테이블(Product)의 모든 레코드를 포함하는 결과를 가져옴, 판매량이 없는 상품도 결과에 포함될 수 있다.
    @Query("SELECT p " +
            "FROM Product p LEFT JOIN SingleOrder so ON p.id = so.productIdx.id " +
            "WHERE p.isDeleted = false AND p.sellerIdx = :seller " +
            "GROUP BY p " +
            "ORDER BY SUM(so.amount) DESC")
    Page<Product> findBySellerOrderBySalesAmounts(Seller seller, Pageable pageable);

    List<Product> findProductByProductNameContaining(String keyword);

    Page<Product> findProductByProductNameContaining(String keyword, Pageable pageable);

    Page<Product> findProductByProductNameContainingOrderByPriceDesc(String keyword, Pageable pageable);

    Page<Product> findProductByProductNameContainingOrderByPrice(String keyword, Pageable pageable);

    Page<Product> findProductByIsDeletedAndStockGreaterThanOrderByPrice(boolean isDeleted, Integer stock, Pageable pageable);

    Page<Product> findProductByIsDeletedAndStockGreaterThanOrderByPriceDesc(boolean isDeleted, Integer stock, Pageable pageable);

    Page<Product> findProductByIsDeletedAndStockGreaterThan(boolean isDeleted, Integer stock, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Product p SET p.isDeleted = true WHERE p.id=:idx ")
    void deleteProduct(@Param("idx") Integer productId);

    //판매순 정렬 - 물품 조회
    @Query(value = "select p.idx, p.products_category_idx, p.seller_idx, p.product_name, p.product_img, p.description, p.description_img, p.price, p.stock, p.sales_end_date, sum(s.amount) as sales_total from single_orders s right join product p on s.product_idx = p.idx group by p.idx order by sales_total desc", nativeQuery = true)
    List<Map<String, Object>> findAllProductsBySingleOrder();

    @Query("SELECT p " +
            "FROM Product p LEFT JOIN SingleOrder so ON p.id = so.productIdx.id " +
            "WHERE p.isDeleted = false " +
            "GROUP BY p " +
            "ORDER BY SUM(so.amount) DESC")
    Page<Product> findAllProductsOrderBySalesAmounts(Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p LEFT JOIN SingleOrder so ON p.id = so.productIdx.id " +
            "WHERE p.isDeleted = false AND p.productsCategoryIdx.id = :categoryIdx " +
            "GROUP BY p " +
            "ORDER BY SUM(so.amount) DESC")
    Page<Product> findProductsByCategoryIdxOrderBySalesAmounts(@Param("categoryIdx") Integer categoryIdx, Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p LEFT JOIN SingleOrder so ON p.id = so.productIdx.id " +
            "WHERE p.isDeleted = false AND p.productName LIKE %:keyword% " +
            "GROUP BY p " +
            "ORDER BY SUM(so.amount) DESC")
    Page<Product> findProductsByKeywordOrderBySalesAmounts(@Param("keyword") String keyword, Pageable pageable);


}
