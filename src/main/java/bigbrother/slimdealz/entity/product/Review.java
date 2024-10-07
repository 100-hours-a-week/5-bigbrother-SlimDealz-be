package bigbrother.slimdealz.entity.product;

import bigbrother.slimdealz.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "reviews")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(name = "customer_rating", nullable = false)
    int customerRating;

    @Column(name = "content", nullable = false)
    String content;

    @Column(name = "product_source", nullable = false)
    String productSource;

    @Column(name = "review_date", nullable = false)
    LocalDate reviewDate;

    @Column(name = "user_name", nullable = false)
    String userName;

}
