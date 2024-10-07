package bigbrother.slimdealz.entity.product;


import bigbrother.slimdealz.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "products")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_category", nullable = false)
    private String category;

    @Column(name = "shipping_price")
    private String shippingFee;

    @Column(name = "vendor_url", nullable = false)
    private String vendorUrl;

    @Column(name = "product_rating")
    private double productRating;

    @Setter
    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    @Column(name = "score")
    private Integer score;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Price> prices;

    // 조회수 추가
    public void incrementViewCount() {
        this.viewCount++;
        this.viewedAt = LocalDateTime.now();
    }

    // 점수 추가
    public void adjustScore(int delta) {
        if(this.score == null) {
            this.score = 0;
        }
        this.score += delta;
    }
}
