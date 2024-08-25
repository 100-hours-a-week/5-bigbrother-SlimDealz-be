
package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}