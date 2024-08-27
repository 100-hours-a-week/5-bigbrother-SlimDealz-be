package bigbrother.slimdealz.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {
    private Long id;

    @NotNull @Positive
    private int setPrice;
    private String promotion;
    private Long productId;
    private VendorDto vendor;
}