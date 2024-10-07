package bigbrother.slimdealz.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String productName;
    private String category;
    private String shippingFee;
    private String vendorUrl;
    private String imageUrl;
    private double productRating;
    private int viewCount;
    private Integer score;
    private LocalDateTime viewedAt;
    private List<PriceDto> prices;
}

