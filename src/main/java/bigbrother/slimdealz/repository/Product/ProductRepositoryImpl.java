package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static bigbrother.slimdealz.entity.product.QPrice.price;
import static bigbrother.slimdealz.entity.product.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> searchByKeyword(String keyword) {

        return queryFactory
                .selectFrom(product)
                .where(product.name.containsIgnoreCase(keyword))
                .fetch();
    }
}
