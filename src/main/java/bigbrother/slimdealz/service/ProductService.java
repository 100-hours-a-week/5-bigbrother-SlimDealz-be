package bigbrother.slimdealz.service;

import bigbrother.slimdealz.entity.Product;
import bigbrother.slimdealz.repository.Product.ProductRepository;
import bigbrother.slimdealz.repository.Product.ProductRepositoryImpl;
import jakarta.persistence.Cacheable;
import lombok.AllArgsConstructor;
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
