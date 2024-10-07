package bigbrother.slimdealz.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "prices")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "set_price", nullable = false)
    private int setPrice;

    String promotion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Price_Product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Price_Vendor"))
    private Vendor vendor;
}