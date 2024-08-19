package bigbrother.slimdealz.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "prices")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {

    @Id
    @GeneratedValue(generator = "id")
    Long id;

    @Column(name = "set_price", nullable = false)
    int setPrice;

    @Column(name = "discounted_price", nullable = false)
    int discountedPrice;

    String promotion;

    @ManyToOne
    @joinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;



}
