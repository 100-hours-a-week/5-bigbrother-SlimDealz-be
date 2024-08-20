package bigbrother.slimdealz.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Table(name = "price_history")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "previous_price")
    int previousPrice;

    @Column(name = "current_price")
    int currentPrice;

    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "end_at")
    Instant endAt;

    @ManyToOne
    @JoinColumn(name = "price_id")
    private Price price;
}
