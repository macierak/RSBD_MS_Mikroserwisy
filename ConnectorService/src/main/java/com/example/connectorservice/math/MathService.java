package com.example.connectorservice.math;

import com.example.connectorservice.LocalePercentageResponse;
import com.example.protocol.BuildingDto;
import com.example.protocol.BuildingType;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MathService {

    public static Map<String, Set<LocalePercentageResponse>> getPercentage(Set<BuildingDto> set) {

        Map<String, Set<LocalePercentageResponse>> resMap = new HashMap<>();
        countriesList(set).forEach(country -> {
            final Set<BuildingDto> itemsForCountry = set.stream()
                    .filter(b -> b.getCountry().equals(country))
                    .collect(Collectors.toSet());

            final Set<LocalePercentageResponse> resForCountry = new HashSet<>();

            Arrays.stream(BuildingType.values()).forEach(buildingType -> {

                final double typeCount = itemsForCountry.stream()
                        .filter(b -> buildingType.toString().equals(b.getType()))
                        .count();

                resForCountry.add(new LocalePercentageResponse(
                        typeCount / itemsForCountry.size() * 100, buildingType.toString())
                );
            });
            resMap.put(country, resForCountry);
        });
        return resMap;
    }

    private static Set<String> countriesList(Set<BuildingDto> dto) {
        Set<String> cSet = new HashSet<>();
        dto.forEach(b -> {
            cSet.add(b.getCountry());
        });
        return cSet;
    }

}
