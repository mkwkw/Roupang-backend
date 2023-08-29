# Roupang - backend

### 팀 이름: Team SuperCat

### 작업 기간 : 2023.08.14~08.25 
### 주제: 고양이 용품 쇼핑몰 만들기
### 프로젝트 목표
![image](https://github.com/mkwkw/Roupang-backend/assets/76611903/cdac572e-e5c2-4d57-9bc3-422d598bfac1)


### 맡은 역할
- 백엔드 6명, 프론트엔드 5명이서 개발
- 백엔드 역할 분배: 회원 2명, 마이페이지와 장바구니 2명, 쇼핑몰과 판매자: 2명
- **민경원: 쇼핑몰 - AWS RDS 담당 및 상품 옵션, 조회, 검색**
    1. AWS RDS 담당
    2. 상품 조회, 검색
    3. 상품 옵션 등록, 조회
       
    <br>![image](https://github.com/mkwkw/Roupang-backend/assets/76611903/61bc3254-5000-4bf9-82a8-eb7b11acc82a)
- **상품 옵션 부분**
 ![ERD 옵션](https://github.com/mkwkw/Roupang-backend/assets/76611903/89cd96da-72e5-4a11-9634-bcb0bf52cf6c)



### 개발 환경
- Java 11, Spring boot 2.7.14
- AWS RDS(MySQL), EC2

### 의의
- NoSQL이 아닌 RDB로 상품 옵션 테이블 설계
- DB Too Many Connections 오류 발생! - 트러블 슈팅
- Stream API 많이 사용하도록 노력하여 가독성 향상
- LEFT JOIN 이용하여 판매순 정렬 개발
- 성능 향상 위해서 Spring Cache, MySQL Index 적용
### 회고 글 URL
https://mkwkw.tistory.com/101 
