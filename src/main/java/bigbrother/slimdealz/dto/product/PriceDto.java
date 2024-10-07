package bigbrother.slimdealz.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {
    private Long id;
    private int setPrice;
    private String promotion;
    private Long productId;
    private VendorDto vendor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}