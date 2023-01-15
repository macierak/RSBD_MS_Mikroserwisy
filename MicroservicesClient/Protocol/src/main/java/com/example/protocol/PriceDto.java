package com.example.protocol;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceDto {
    private String street;
    private String type;
    private Long price;
    private String city;


}
