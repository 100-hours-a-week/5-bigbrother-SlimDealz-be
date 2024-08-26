package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.entity.product.QPrice;
import bigbrother.slimdealz.entity.product.QProduct;
import bigbrother.slimdealz.entity.product.QVendor;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.beans.Expression;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QProduct product = QProduct.product;
    private final QPrice price = QPrice.price;
    private final QVendor vendor = QVendor.vendor;

    // 검색 결과 목록
    @Override
    public List<Product> searchByKeyword(String keyword, Long lastSeenId, int size) {

        return queryFactory
                .selectFrom(product)
                .where(
                        product.name.containsIgnoreCase(keyword),
                        lastSeenId != null ? product.id.gt(lastSeenId) : null
                ) // containsIgnoreCase 부분검색, like '%%'
                .orderBy(product.id.asc())
                .limit(size)
                .fetch();
    }

    // 오늘의 최저가
    @Override
    public List<Product> findLowestPriceProducts() {
        return queryFactory
                .select(product)
                .from(price)
                .join(price.product, product)
                .orderBy(price.setPrice.asc())
                .limit(10)
                .fetch();
    }

    // 상품 상세 페이지

    @Override
    public Product findProductWithLowestPriceByName(String productName) {
        return queryFactory
                .selectFrom(product)
                .join(product.prices, price)
                .where(product.name.eq(productName)) // 상품명과 일치하는 상품만 조회
                .groupBy(product.id, price.vendor.id)
                .orderBy(product.name.asc(), price.setPrice.asc()) // 할인가 기준 최저가 정렬
                .fetchFirst(); // 정렬한 상품 중 첫번째 상품 반환
    }

    // 카테고리 목록
    @Override
    public List<Product> findByCategory(String category, Long lastSeenId, int size) {
        QProduct productSub = new QProduct("productSub");
        QPrice priceSub = new QPrice("priceSub");

        return queryFactory
                .selectFrom(product)
                .join(product.prices, price)
                .where(
                        product.category.eq(category),
                        lastSeenId != null ? product.id.gt(lastSeenId) : null,
                        price.setPrice.eq(
                                JPAExpressions.select(priceSub.setPrice.min())
                                        .from(priceSub)
                                        .join(priceSub.product, productSub)
                                        .where(productSub.name.eq(product.name))
                        )
                )
                .orderBy(product.id.asc())
                .limit(size)
                .fetch();
    }

    // 판매처 리스트
    @Override
    public List<Product> findProductWithVendors(String productName) {
        return queryFactory
                .selectFrom(product)
                .leftJoin(product.prices, price)
                .fetchJoin()
                .where(product.name.eq(productName))
                .fetch();
    }

    // 랜덤 추천
    @Override
    public List<Product> findRandomProducts() {
        QProduct productSub = new QProduct("productSub");
        QPrice priceSub = new QPrice("priceSub");

        return queryFactory
                .selectFrom(product)
                .join(product.prices, price)
                .where(price.setPrice.eq(
                        JPAExpressions
                                .select(priceSub.setPrice.min())
                                .from(priceSub)
                                .join(priceSub.product, productSub)
                                .where(productSub.name.eq(product.name))
                ))
                .orderBy(Expressions.numberTemplate(Double.class, "function('RAND')").asc()) // 랜덤으로 섞기
                .limit(10) // 10개만 선택
                .fetch();
    }

}
