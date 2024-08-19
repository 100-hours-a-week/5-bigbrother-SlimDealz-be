package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}
