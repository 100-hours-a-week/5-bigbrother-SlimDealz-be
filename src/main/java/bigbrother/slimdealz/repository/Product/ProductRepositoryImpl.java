<<<<<<< Updated upstream
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
}
=======
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
>>>>>>> Stashed changes
