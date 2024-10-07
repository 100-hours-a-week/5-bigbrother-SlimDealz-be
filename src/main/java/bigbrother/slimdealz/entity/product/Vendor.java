package bigbrother.slimdealz.entity.product;

import bigbrother.slimdealz.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "vendors")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vendor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "vendor_url")
    private String vendorUrl;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Price> prices;

}