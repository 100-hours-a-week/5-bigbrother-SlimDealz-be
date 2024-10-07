package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.entity.product.QPrice;
import bigbrother.slimdealz.entity.product.QProduct;
import bigbrother.slimdealz.entity.product.QVendor;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@PersistenceContext
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QProduct product = QProduct.product;
    private final QPrice price = QPrice.price;
    private final QVendor vendor = QVendor.vendor;

    private final LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
    private final LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59, 999999999);

    // 현재 데이터 없는 경우, 과거 데이터 조회
    private List<Product> backToDayList(LocalDateTime startOfDay, LocalDateTime endOfDay, Function<JPAQueryFactory, List<Product>> queryFunction) {
        List<Product> products = queryFunction.apply(queryFactory);

        int maxDaysBack = 3;  // 예시: 최대 3일 이전까지만 조회
        int daysBack = 0;

        while (products.isEmpty() && daysBack < maxDaysBack) {
            startOfDay = startOfDay.minusDays(1);
            endOfDay = endOfDay.minusDays(1);
            daysBack++;

            products = queryFunction.apply(queryFactory);

            if(!products.isEmpty()) {
                break;
            }
        }
        return products;
    }

    // 검색 결과 목록
    @Override
    public List<Product> searchByKeyword(String keyword, Long lastSeenId, String lastSeenProductName,int size) {
        QProduct productSub = new QProduct("productSub");
        QPrice priceSub = new QPrice("priceSub"); // Price 서브쿼리용 객체

        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .selectFrom(product)
                        .where(
                                product.productName.containsIgnoreCase(keyword),
                                lastSeenId != null ? product.id.gt(lastSeenId) : null,
                                product.createdAt.between(startOfDay, endOfDay),
                                lastSeenProductName != null ? product.productName.ne(lastSeenProductName) : null
                        )
                        .groupBy(product.productName) // name을 기준으로 그룹화
                        .having(product.prices.any().setPrice.eq( // 최저가 조건 추가
                                JPAExpressions
                                        .select(priceSub.setPrice.min()) // 최저가 서브쿼리
                                        .from(priceSub)
                                        .where(priceSub.product.eq(product)) // 동일한 제품에 대한 가격 조회
                        ))
                        .orderBy(product.prices.any().setPrice.asc(), product.id.asc())
                        .limit(size) // 결과 크기 제한
                        .fetch()
        );
    }

    // 오늘의 최저가
    @Override
    public List<Product> findLowestPriceProducts() {
        QPrice subPrice = new QPrice("subPrice");

        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .select(product)
                        .from(price)
                        .join(price.product, product)
                        .where(price.createdAt.between(startOfDay, endOfDay)
                                .and(price.setPrice.eq(
                                        JPAExpressions
                                                .select(subPrice.setPrice.min())
                                                .from(subPrice)
                                                .where(subPrice.product.productName.eq(product.productName)
                                                        .and(subPrice.createdAt.between(startOfDay, endOfDay)))
                                )))
                        .groupBy(product.productName, price.setPrice)
                        .orderBy(price.setPrice.asc(), product.id.asc())
                        .select(product)
                        .limit(10)
                        .fetch()
        );
    }

    // 상품 상세 페이지
    @Override
    public Product findProductWithLowestPriceByName(String productName) {
        List<Product> products = backToDayList(startOfDay, endOfDay, queryFactory ->
                Collections.singletonList(queryFactory
                        .selectFrom(product)
                        .join(product.prices, price)
                        .where(product.productName.eq(productName),
                                price.createdAt.between(startOfDay, endOfDay)) // 상품명과 일치하는 상품만 조회
                        .groupBy(product.id, price.vendor.id)
                        .orderBy(product.productName.asc(), price.setPrice.asc(), product.id.asc()) // 할인가 기준 최저가 정렬
                        .fetchFirst()) // 정렬한 상품 중 첫번째 상품 반환
        );

        return products.isEmpty() ? null : products.get(0);
    }

    // 카테고리 목록
    @Override
    public List<Product> findByCategory(String category, Long lastSeenId, String lastSeenProductName, int size) {
        QProduct productSub = new QProduct("productSub");
        QPrice priceSub = new QPrice("priceSub");

        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .selectFrom(product)
                        .join(product.prices, price)
                        .where(
                                product.category.eq(category),
                                lastSeenId != null ? product.id.gt(lastSeenId) : null,
                                product.createdAt.between(startOfDay, endOfDay),
                                lastSeenProductName != null ? product.productName.ne(lastSeenProductName) : null,
                                price.setPrice.eq(
                                        JPAExpressions.select(priceSub.setPrice.min())
                                                .from(priceSub)
                                                .join(priceSub.product, productSub)
                                                .where(productSub.productName.eq(product.productName))
                                )
                        )
                        .orderBy(price.setPrice.asc(), product.id.asc())
                        .limit(size)
                        .fetch()
        );
    }

    // 판매처 리스트
    @Override
    public List<Product> findProductWithVendors(String productName) {
        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .selectFrom(product)
                        .leftJoin(product.prices, price)
                        .fetchJoin()
                        .where(product.productName.eq(productName)
                                , product.createdAt.between(startOfDay, endOfDay)
                                , price.createdAt.between(startOfDay, endOfDay)
                                ,vendor.createdAt.between(startOfDay, endOfDay))
                        .fetch()
        );
    }

    // 랜덤 추천
    @Override
    public List<Product> findRandomProducts() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59, 999999999);

        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .selectFrom(product)
                        .join(product.prices, price)
                        .where(
                                product.createdAt.between(startOfDay, endOfDay), // 오늘의 생성된 상품만 필터링
                                price.setPrice.eq(
                                        JPAExpressions
                                                .select(price.setPrice.min())
                                                .from(price)
                                                .where(price.product.eq(product))
                                )
                        )
                        .orderBy(Expressions.numberTemplate(Double.class, "function('RAND')").asc(), product.id.asc()) // 랜덤으로 섞기
                        .limit(10) // 10개만 선택
                        .fetch()
        );
    }


    // 인기 급상승
    @Override
    public List<Product> findPopularProducts(LocalDateTime oneHourAgo) {
        QProduct product = QProduct.product;
        QPrice price = QPrice.price;

        return queryFactory
                .selectFrom(product)
                .leftJoin(product.prices, price)
                .fetchJoin() // 연관된 엔티티를 함께 가져옴
                .where(product.viewCount.gt(0), product.updatedAt.after(LocalDate.now().atStartOfDay()))
                .orderBy(product.viewCount.desc())
                .limit(10)
                .fetch();
    }

    // 최고가 순 가격
    @Override
    public List<Product> findTopProductsByPrice() {
        QProduct product = QProduct.product;
        QPrice price = QPrice.price;

        return queryFactory
                .selectFrom(product)
                .leftJoin(product.prices, price)
                .fetchJoin() // 연관된 엔티티를 함께 가져옴
                .orderBy(product.prices.any().setPrice.desc(), product.id.asc())
                .limit(10)
                .fetch();
    }
}