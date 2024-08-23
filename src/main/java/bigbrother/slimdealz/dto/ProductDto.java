package bigbrother.slimdealz.dto;

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
    private String name;
    private String image;
    private String brand;
    private String category;
    private String shippingFee;
    private List<PriceDto> prices;
}

