package bigbrother.slimdealz.service;

import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.repository.Product.ProductRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepositoryImpl productRepositoryImpl;

    public List<Product> searchProducts(String keyword) {
        return productRepositoryImpl.searchByKeyword(keyword);
    }

//    public List<Product> searchByMeaning(String keyword) {
//        return productRepository.searchByMeaning(keyword);
//    }
//
//    public List<Product> searchByKoreanKeyword(String keyword) {
//        return productRepository.searchByKoreanKeyword(keyword);
//    }
}
