<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.entity.product.QPrice;
import bigbrother.slimdealz.entity.product.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QProduct product = QProduct.product;
    private final QPrice price = QPrice.price;

    @Override
    public List<Product> searchByKeyword(String keyword) {

        return queryFactory
                .selectFrom(product)
                .where(product.name.containsIgnoreCase(keyword)) // containsIgnoreCase 부분검색, like '%%'
                .fetch();
    }

    @Override
    public List<Product> findLowestPriceProducts() {
        return queryFactory
                .select(product)
                .from(price)
                .join(price.product, product)
                .orderBy(price.discountedPrice.asc())
                .limit(10)
                .fetch();
    }
<<<<<<< Updated upstream
<<<<<<< Updated upstream
}
=======
=======

>>>>>>> Stashed changes
//package bigbrother.slimdealz.repository.Product;
//
//import bigbrother.slimdealz.entity.product.Product;
//import bigbrother.slimdealz.entity.product.QPrice;
//import bigbrother.slimdealz.entity.product.QProduct;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//
//@Repository
//@RequiredArgsConstructor
//public class ProductRepositoryImpl implements ProductRepositoryCustom {
//    private final JPAQueryFactory queryFactory;
//
//    private final QProduct product = QProduct.product;
//    private final QPrice price = QPrice.price;
//
//    @Override
//    public List<Product> searchByKeyword(String keyword) {
//
//        return queryFactory
//                .selectFrom(product)
//                .where(product.name.containsIgnoreCase(keyword)) // containsIgnoreCase 부분검색, like '%%'
//                .fetch();
//    }
//
//    @Override
//    public List<Product> findLowestPriceProducts() {
//        return queryFactory
//                .select(product)
//                .from(price)
//                .join(price.product, product)
//                .orderBy(price.discountedPrice.asc())
//                .limit(10)
//                .fetch();
//    }
//}
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
=======
>>>>>>> Stashed changes

    @Override
    public Product findProductWithLowestPriceByName(String productName) {
        return queryFactory
                .selectFrom(product)
                .join(product.prices, price)
                .where(product.name.eq(productName)) // 상품명과 일치하는 상품만 조회
                .groupBy(product.id, price.vendor.id)
                .orderBy(product.name.asc(), price.discountedPrice.asc()) // 할인가 기준 최저가 정렬
                .fetchFirst(); // 정렬한 상품 중 첫번째 상품 반환
    }

    @Override
    public List<Product> findByCategory(String category) {
        return queryFactory
                .selectFrom(product)
                .where(product.category.eq(category)) // 카테고리 별 조회
                .fetch();
    }
}
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
