package com.example.connectorservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LocalePercentageResponse {
    private Long percentage;
    private String buildingType;
}
