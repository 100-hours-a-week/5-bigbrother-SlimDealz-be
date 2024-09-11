package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.dto.product.PriceDto;
import bigbrother.slimdealz.dto.product.ProductConverter;
import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.dto.product.VendorDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.entity.product.QPrice;
import bigbrother.slimdealz.entity.product.QProduct;
import bigbrother.slimdealz.entity.product.QVendor;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


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
    private List<ProductDto> backToDayList(LocalDateTime startOfDay, LocalDateTime endOfDay, Function<JPAQueryFactory, List<ProductDto>> queryFunction) {
        List<ProductDto> products = queryFunction.apply(queryFactory);

        while (products.isEmpty()) {
            startOfDay = startOfDay.minusDays(1);
            endOfDay = endOfDay.minusDays(1);

            products = queryFunction.apply(queryFactory);

            if (!products.isEmpty()) {
                break;
            }
        }
        return products;
    }

    // 검색 결과 목록
    @Override
    public List<ProductDto> searchByKeyword(String keyword, Long lastSeenId, int size) {

        QProduct productSub = new QProduct("productSub");
        QPrice priceSub = new QPrice("priceSub");
        QVendor vendorSub = new QVendor("vendorSub");

        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .select(Projections.fields(ProductDto.class,
                                product.id.as("id"),
                                product.name.as("name"),
                                product.shippingFee.as("shippingFee"),
                                price.setPrice.as("setPrice")
                        ))
                        .from(product)
                        .join(product.prices, price)
                        .where(
                                product.name.containsIgnoreCase(keyword),
                                lastSeenId != null ? product.id.gt(lastSeenId) : null,
                                product.createdAt.between(startOfDay, endOfDay),
                                price.setPrice.eq(
                                        JPAExpressions
                                                .select(priceSub.setPrice.min())
                                                .from(priceSub)
                                                .where(priceSub.product.name.eq(product.name))
                                )
                        )
                        .groupBy(product.id ,product.name, product.shippingFee)
                        .orderBy(price.setPrice.asc())
                        .limit(size)
                        .fetch()
        );
    }


    // 오늘의 최저가
    @Override
    public List<ProductDto> findLowestPriceProducts() {
        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .select(Projections.fields(ProductDto.class,
                                product.id.as("id"),
                                product.name.as("name"),
                                product.shippingFee.as("shippingFee"),
                                price.setPrice.as("setPrice")
                        ))
                        .from(price)
                        .join(price.product, product)
                        .where(price.createdAt.between(startOfDay, endOfDay))
                        .groupBy(product.id, product.name, product.shippingFee)
                        .orderBy(price.setPrice.asc())
                        .limit(10)
                        .fetch()
        );
    }

    // 상품 상세 페이지
    @Override
    public ProductDto findProductWithLowestPriceByName(String productName) {
        List<ProductDto> products = backToDayList(startOfDay, endOfDay, queryFactory ->
                Collections.singletonList(queryFactory
                        .select(Projections.fields(ProductDto.class,
                                product.id.as("id"),
                                product.name.as("name"),
                                product.shippingFee.as("shippingFee"),
                                price.setPrice.as("setPrice")
                        ))
                        .from(product)
                        .leftJoin(product.prices, price)
                        .where(product.name.eq(productName),
                                price.createdAt.between(startOfDay, endOfDay))
                        .groupBy(product.id, product.name, product.shippingFee)
                        .orderBy(price.setPrice.asc())
                        .limit(1)
                        .fetchFirst()
                )
        );

        return products.isEmpty() ? null : products.get(0);
    }

    // 카테고리 목록
    @Override
    public List<ProductDto> findByCategory(String category, Long lastSeenId, int size) {
        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .select(Projections.fields(ProductDto.class,
                                product.id.as("id"),
                                product.name.as("name"),
                                product.shippingFee.as("shippingFee"),
                                price.setPrice.as("setPrice")
                        ))
                        .from(product)
                        .leftJoin(product.prices, price)
                        .where(
                                product.category.eq(category),
                                lastSeenId != null ? product.id.gt(lastSeenId) : null,
                                product.createdAt.between(startOfDay, endOfDay),
                                price.setPrice.eq(
                                        JPAExpressions.select(price.setPrice.min())
                                                .from(price)
                                                .where(price.product.eq(product))
                                )
                        )
                        .groupBy(product.id, product.name, product.shippingFee)
                        .orderBy(price.setPrice.asc())
                        .limit(size)
                        .fetch()
        );
    }

    // 판매처 리스트
    @Override
    public List<ProductDto> findProductWithVendors(String productName) {
        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .select(Projections.fields(ProductDto.class,
                                product.id.as("id"),
                                product.name.as("name"),
                                product.shippingFee.as("shippingFee"),
                                price.setPrice.as("setPrice")
                        ))
                        .from(product)
                        .leftJoin(product.prices, price)
                        .leftJoin(price.vendor, vendor)
                        .where(product.name.eq(productName),
                                product.createdAt.between(startOfDay, endOfDay),
                                price.createdAt.between(startOfDay, endOfDay),
                                vendor.createdAt.between(startOfDay, endOfDay))
                        .fetch()
        );
    }

    // 랜덤 추천
    @Override
    public List<ProductDto> findRandomProducts() {
        return backToDayList(startOfDay, endOfDay, queryFactory ->
                queryFactory
                        .select(Projections.fields(ProductDto.class,
                                product.id.as("id"),
                                product.name.as("name"),
                                product.shippingFee.as("shippingFee"),
                                price.setPrice.as("setPrice")
                        ))
                        .from(product)
                        .leftJoin(product.prices, price)
                        .where(
                                product.createdAt.between(startOfDay, endOfDay),
                                price.setPrice.eq(
                                        JPAExpressions
                                                .select(price.setPrice.min())
                                                .from(price)
                                                .where(price.product.eq(product))
                                )
                        )
                        .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                        .limit(10)
                        .fetch()
        );
    }
}