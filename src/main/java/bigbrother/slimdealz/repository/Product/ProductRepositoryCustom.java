package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryCustom {
    // 키워드 검색
    List<Product> searchByKeyword(String keyword, Long lastSeenId, int size);

    // 오늘의 최저가 상품
    List<Product> findLowestPriceProducts();

    // 상품 상세 페이지
    Product findProductWithLowestPriceByName(String productName);

    // 상품 목록
    List<Product> findByCategory(String category, Long lastSeenId, int size);
}
