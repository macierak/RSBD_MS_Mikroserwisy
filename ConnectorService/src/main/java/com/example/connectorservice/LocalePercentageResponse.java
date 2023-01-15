package com.example.connectorservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalePercentageResponse {
    private Double percentage;
    private String buildingType;
}
