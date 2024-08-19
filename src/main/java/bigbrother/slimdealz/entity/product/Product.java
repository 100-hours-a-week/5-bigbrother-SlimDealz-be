package bigbrother.slimdealz.entity.product;


import bigbrother.slimdealz.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "products")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id")
    Long id;

    String name;

    String image;

    String brand;

    String category;

    @Column(name = "shipping_fee")
    String shippingFee;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Price> prices;

}
