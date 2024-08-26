package bigbrother.slimdealz.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistoryDto {
    private Long id;
    private int previousPrice;
    private String updatedAt;
    private String endAt;
}
