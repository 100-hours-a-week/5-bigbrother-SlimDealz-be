package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryCustom {
    // 키워드 검색
    List<Product> searchByKeyword(String keyword);

    // 의미 검색
//    List<Product> searchByMeaning(String keyword);

    // 한글 키워드 검색
//    List<Product> searchByKoreanKeyword(String keyword);
}
