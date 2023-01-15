package com.example.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryAvgPrice {
    String country;
    Double price;
}
