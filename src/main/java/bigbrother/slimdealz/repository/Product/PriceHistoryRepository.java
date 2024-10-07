package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.dto.product.ChartDto;
import bigbrother.slimdealz.entity.product.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    // 이름으로 제품 검색
    @Query("SELECT new bigbrother.slimdealz.dto.product.ChartDto(ph.productName, ph.previousPrice, ph.updatedAt) " +
            "FROM PriceHistory ph " +
            "WHERE ph.productName = :productName " +
            "AND ph.updatedAt >= :startDate " +
            "ORDER BY ph.updatedAt ASC")
    List<ChartDto> findChartData(@Param("productName") String productName, @Param("startDate") LocalDateTime startDate);  // LocalDateTime으로 수정


}