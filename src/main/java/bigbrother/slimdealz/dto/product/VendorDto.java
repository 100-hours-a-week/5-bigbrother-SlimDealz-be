package bigbrother.slimdealz.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDto {
    Long id;
    @NotNull
    String vendorName;
    @NotNull
    String vendorUrl;
}
