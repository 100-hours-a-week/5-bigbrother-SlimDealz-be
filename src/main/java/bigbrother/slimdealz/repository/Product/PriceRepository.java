package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
}
