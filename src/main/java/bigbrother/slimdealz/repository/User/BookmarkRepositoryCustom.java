package bigbrother.slimdealz.repository.User;

import bigbrother.slimdealz.entity.product.Product;

import java.util.List;

public interface BookmarkRepositoryCustom {
    List<Product> findRecommendedProducts(Long userId);
}
