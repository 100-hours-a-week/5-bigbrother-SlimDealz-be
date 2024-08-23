package bigbrother.slimdealz.entity.product;

import bigbrother.slimdealz.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "vendors")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vendor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String vendorName;

    @Column(name = "logo")
    String vendorImage;

    @Column(name = "url")
    String vendorUrl;

<<<<<<< Updated upstream
}
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
}
>>>>>>> Stashed changes
