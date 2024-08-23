package bigbrother.slimdealz.repository.Product;

import bigbrother.slimdealz.entity.product.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
