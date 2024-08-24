package bigbrother.slimdealz.dto;

import bigbrother.slimdealz.entity.product.Price;
import bigbrother.slimdealz.entity.product.Product;

import java.util.stream.Collectors;

public class ProductConverter {
    public static ProductDto toProductDTO(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .shippingFee(product.getShippingFee())
                .prices(product.getPrices().stream()
                        .map(ProductConverter::toPriceDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public static PriceDto toPriceDTO(Price price) {
        return PriceDto.builder()
                .id(price.getId())
                .setPrice(price.getSetPrice())
                .promotion(price.getPromotion())
                .productId(price.getProduct().getId())
                .vendorId(price.getVendor().getId())
                .build();
    }
}