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
public class BookmarkProductPriceDto {
    private Long bookmarkId;
    private Long productId;
    private String productName;
    private String shippingFee;
    private List<PriceDto> prices;  // List to hold multiple prices
}
