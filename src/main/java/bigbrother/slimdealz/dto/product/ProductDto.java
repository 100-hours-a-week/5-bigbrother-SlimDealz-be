package bigbrother.slimdealz.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @PositiveOrZero
    private String shippingFee;
    private String vendorUrl;
    private String imageUrl;
    private List<PriceDto> prices;
}

