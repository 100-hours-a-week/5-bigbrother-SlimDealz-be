package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.entity.product.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryCustom {
    // 키워드 검색
    List<ProductDto> searchByKeyword(String keyword, Long lastSeenId, int size);

    // 오늘의 최저가 상품
    List<ProductDto> findLowestPriceProducts();

    // 상품 상세 페이지
    ProductDto findProductWithLowestPriceByName(String productName);

    // 상품 목록
    List<ProductDto> findByCategory(String category, Long lastSeenId, int size);

    // 판매처 리스트
    List<ProductDto> findProductWithVendors(String productName);

    // 랜덤 추천
    List<ProductDto> findRandomProducts();
}
