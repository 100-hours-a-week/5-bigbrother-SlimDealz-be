package bigbrother.slimdealz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDto {
    private Long id;
    private String vendorName;
    private String vendorImage;
    private String vendorUrl;
}
