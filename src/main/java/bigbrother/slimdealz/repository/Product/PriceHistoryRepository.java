package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
}
