package bigbrother.slimdealz.service;

import bigbrother.slimdealz.entity.Product;
import bigbrother.slimdealz.repository.ProductRepository;
import jakarta.persistence.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(value = "productSearchCache", key = "#keyword")
    public List<Product> searchByKeyword(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }

    public List<Product> searchByMeaning(String keyword) {
        return productRepository.searchByMeaning(keyword);
    }

    public List<Product> searchByKoreanKeyword(String keyword) {
        return productRepository.searchByKoreanKeyword(keyword);
    }
}
