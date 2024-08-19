package bigbrother.slimdealz.entity;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "products")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(generator = "id")
    Long id;

    String name;

    String image;

    String category;

    @Column(name = "shipping_fee")
    String shippingFee;
}
