package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.dto.product.ReviewDto;
import bigbrother.slimdealz.entity.product.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT new bigbrother.slimdealz.dto.product.ReviewDto(r.productName, r.customerRating, r.content, r.productSource, r.reviewDate, r.userName) " +
            "FROM Review r WHERE r.productName = :productName")
    List<ReviewDto> findReviewDtosByProductName(@Param("productName") String productName);
}
