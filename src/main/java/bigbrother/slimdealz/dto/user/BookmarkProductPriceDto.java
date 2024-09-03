package bigbrother.slimdealz.dto.user;

import bigbrother.slimdealz.dto.product.PriceDto;
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

    @JsonProperty("id")  // JSON에서 "name"으로 직렬화
    private Long productId;

    @JsonProperty("name")  // JSON에서 "name"으로 직렬화
    private String productName;

    @JsonProperty("imageUrl")  // JSON에서 "name"으로 직렬화
    private String image; // 제품 이미지 URL 필드 추가

    private String shippingFee;
    private List<PriceDto> prices;  // List to hold multiple prices

    // Optional: 할인율 계산을 위한 필드를 추가
    private Double originalPrice;
    private Double salePrice;
    private Double discountRate; // 할인율을 직접 계산하여 포함

    // 할인율을 계산하는 메서드
    public Double calculateDiscountRate() {
        if (originalPrice != null && salePrice != null && originalPrice > 0) {
            return (originalPrice - salePrice) / originalPrice * 100;
        }
        return 0.0;
    }
}
