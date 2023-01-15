package com.example.connectorservice.math;

import com.example.connectorservice.LocalePercentageResponse;
import com.example.protocol.BuildingDto;
import com.example.protocol.BuildingType;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MathService {

    public static Map<String, Set<LocalePercentageResponse>> getPercentage(Set<BuildingDto> set) {
        long totalItems = set.size();
        Map<String, Set<LocalePercentageResponse>> resMap = new HashMap<>();
        set.stream().forEach(buildingDto -> {
            Set<LocalePercentageResponse> resForCountry = new HashSet<>();
            Arrays.stream(BuildingType.values()).forEach(buildingType -> {
                long typeCount = set.stream().filter(b -> buildingType.toString().equals(b.getType())).count();
                long percentage = typeCount/totalItems;

                LocalePercentageResponse res = LocalePercentageResponse.builder()
                        .buildingType(buildingType.toString())
                        .percentage(percentage).build();
                resForCountry.add(res);
            });
            resMap.put(buildingDto.getCountry(), resForCountry);
        });
        return resMap;
    }


}
