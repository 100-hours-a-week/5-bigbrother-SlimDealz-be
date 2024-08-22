package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryCustom {
    // 키워드 검색
    List<Product> searchByKeyword(String keyword);

    // 오늘의 최저가 상품
    List<Product> findLowestPriceProducts();

    // 상품 상세 페이지
    Product findProductWithLowestPriceByName(String productName);
}
