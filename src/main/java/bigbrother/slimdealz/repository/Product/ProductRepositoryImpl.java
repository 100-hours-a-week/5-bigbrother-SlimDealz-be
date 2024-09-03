package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.entity.product.QPrice;
import bigbrother.slimdealz.entity.product.QProduct;
import bigbrother.slimdealz.entity.product.QVendor;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;


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

        while (products.isEmpty()) {
            startOfDay = startOfDay.minusDays(1);
            endOfDay = endOfDay.minusDays(1);

            products = queryFunction.apply(queryFactory);

            if(!products.isEmpty()) {
                break;
            }
        }
        return products;
    }

    // 검색 결과 목록
    @Override
    public List<Product> searchByKeyword(String keyword, Long lastSeenId, int size) {

        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .selectFrom(product)
                        .where(
                                product.name.containsIgnoreCase(keyword),
                                lastSeenId != null ? product.id.gt(lastSeenId) : null,
                                product.createdAt.between(startOfDay, endOfDay)
                        ) // containsIgnoreCase 부분검색, like '%%'
                        .orderBy(product.id.asc())
                        .limit(size)
                        .fetch()
        );
    }

    // 오늘의 최저가
    @Override
    public List<Product> findLowestPriceProducts() {
        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .select(product)
                        .from(price)
                        .join(price.product, product)
                        .where(price.createdAt.between(startOfDay, endOfDay))
                        .orderBy(price.setPrice.asc())
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
                        .where(product.name.eq(productName),
                                price.createdAt.between(startOfDay, endOfDay)) // 상품명과 일치하는 상품만 조회
                        .groupBy(product.id, price.vendor.id)
                        .orderBy(product.name.asc(), price.setPrice.asc()) // 할인가 기준 최저가 정렬
                        .fetchFirst()) // 정렬한 상품 중 첫번째 상품 반환
        );

        return products.isEmpty() ? null : products.get(0);
    }

    // 카테고리 목록
    @Override
    public List<Product> findByCategory(String category, Long lastSeenId, int size) {
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
                                price.setPrice.eq(
                                        JPAExpressions.select(priceSub.setPrice.min())
                                                .from(priceSub)
                                                .join(priceSub.product, productSub)
                                                .where(productSub.name.eq(product.name))
                                )
                        )
                        .orderBy(product.id.asc())
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
                        .where(product.name.eq(productName)
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
                        .orderBy(Expressions.numberTemplate(Double.class, "function('RAND')").asc()) // 랜덤으로 섞기
                        .limit(10) // 10개만 선택
                        .fetch()
        );
    }

}