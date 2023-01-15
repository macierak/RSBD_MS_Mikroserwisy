package com.example.protocol;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuildingDtoBugzord {
    private Long id;
    private String country;
    private String city;
    private String street;
    private String type;

    private Long price;

    private Short bedrooms;
    private Short bathrooms;

}
