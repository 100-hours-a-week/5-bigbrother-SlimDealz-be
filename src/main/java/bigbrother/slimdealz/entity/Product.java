package bigbrother.slimdealz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "products")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
