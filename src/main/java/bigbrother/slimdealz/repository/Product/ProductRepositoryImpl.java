package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static bigbrother.slimdealz.entity.QProduct.product;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Product> searchByKeyword(String keyword) {
        // 두글자씩 분할 검색
        String searchPattern = "%" + keyword.replaceAll("(.{2})", "$1%") + "%";

        return queryFactory
                .selectFrom(product)
                .leftJoin(product.prices, price).fetchJoin()
                .where(product.name.like(searchPattern))
                .fetch();
    }
}
