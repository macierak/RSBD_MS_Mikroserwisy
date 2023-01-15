package com.example.connectorservice;

import com.example.bugzordclient.BugzordFeignClient;
import com.example.connectorservice.math.MathService;
import com.example.krzysztofclient.KrzychuFeignClient;
import com.example.protocol.BuildingDto;
import com.example.protocol.CountryAvgPrice;
import com.example.protocol.PriceDto;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ConnectorServiceController {

    final BugzordFeignClient bugzordFeignClient;
    final KrzychuFeignClient krzychuFeignClient;

    final MathService mathService;

    @GetMapping("/api/bugzord")
    Set<BuildingDto> getBuildings(){
        return bugzordFeignClient.getBuildings();
    }

    @GetMapping("/api/krzychu")
    Set<BuildingDto> getBuildingsKrzycha(){
        return krzychuFeignClient.getBuildings();
    }

    @GetMapping("/api/connect")
    Set<BuildingDto> connectBuildings() {
        final Set<BuildingDto> result = new HashSet<>();
        result.addAll(krzychuFeignClient.getBuildings());
        result.addAll(bugzordFeignClient.getBuildings());

        return result;
    }

    @GetMapping("/api/buildings/type/{type}")
    public Set<BuildingDto> getBuildingsByType(@PathVariable @RequestParam BuildingType type) {
        final Set<BuildingDto> result = new HashSet<>();
        result.addAll(krzychuFeignClient.getBuildingsByType(type.name()));
        result.addAll(bugzordFeignClient.getBuildingsByType(type.name()));

        return result;
    }

    @GetMapping("/api/buildings/country/{country}")
    public Set<BuildingDto> getBuildingsByType(@PathVariable @RequestParam String country) {
        final Set<BuildingDto> result = new HashSet<>();
        result.addAll(krzychuFeignClient.getBuildingsByCountry(country));
        result.addAll(bugzordFeignClient.getBuildingsByCountry(country));

        return result;
    }

    @GetMapping("/api/buildings/price")
    public List<BuildingDto> getBuildingsByType(@RequestParam(required = false) Long priceFrom,
                                                @RequestParam(required = false) Long priceTo,
                                                @RequestParam(required = false) String sort) {

        final Set<BuildingDto> result = new HashSet<>();
        result.addAll(krzychuFeignClient.getBuildingsByPrice(priceFrom, priceTo));
        result.addAll(bugzordFeignClient.getBuildingsByPrice(priceFrom, priceTo));

        if (sort.equalsIgnoreCase("DESC")) {
            return result.stream().sorted(Comparator.comparing(BuildingDto::getPrice).reversed()).toList();
        }

        return result.stream().sorted(Comparator.comparing(BuildingDto::getPrice)).toList();
    }

    @GetMapping("/api/minprice")
    public Map<String, PriceDto> getMinPriceForAllCountries() {
        final Map<String, PriceDto> bMap = bugzordFeignClient.getMinPriceForAllCountries();
        final Map<String, PriceDto> kMap = krzychuFeignClient.getMinPriceForAllCountries();

        final Map<String, PriceDto> resultMap = new HashMap<>(bMap);

        kMap.forEach((k, v) -> {
            if (!resultMap.containsKey(k)) {
                resultMap.put(k, v);
            } else {
                final PriceDto dto = resultMap.get(k);
                if (dto.getPrice() > v.getPrice()) {
                    resultMap.replace(k, v);
                }
            }
        });

        return resultMap;
    }

    @GetMapping("/api/maxprice")
    public Map<String, PriceDto> getMaxPriceForAllCountries() {
        final Map<String, PriceDto> bMap = bugzordFeignClient.getMaxPriceForAllCountries();
        final Map<String, PriceDto> kMap = krzychuFeignClient.getMaxPriceForAllCountries();

        final Map<String, PriceDto> resultMap = new HashMap<>(bMap);

        kMap.forEach((k, v) -> {
            if (!resultMap.containsKey(k)) {
                resultMap.put(k, v);
            } else {
                final PriceDto dto = resultMap.get(k);
                if (dto.getPrice() < v.getPrice()) {
                    resultMap.replace(k, v);
                }
            }
        });

        return resultMap;
    }

    @GetMapping("/api/price/statistics")
    public Map<String, Map.Entry<PriceDto, PriceDto>> getPriceStatistics() {
        Map<String, PriceDto> maxPrices = getMaxPriceForAllCountries();
        Map<String, PriceDto> minPrices = getMinPriceForAllCountries();

        Map<String, Map.Entry<PriceDto, PriceDto>> response = new HashMap<>();

        maxPrices.forEach((k, MaxV) -> {
            final PriceDto lowV = minPrices.get(k);

            Map.Entry<PriceDto, PriceDto> pair = new AbstractMap.SimpleEntry<>(MaxV, lowV);
            response.put(k, pair);
        });

        minPrices.forEach((k, v) -> {
            if (!response.containsKey(k)) {
                response.put(k, new AbstractMap.SimpleEntry<>(v, null));
            }
        });

        return response;
    }

    @GetMapping("/api/locals/percentage")
    public Map<String, Set<LocalePercentageResponse>> getPercentage() {
        return MathService.getPercentage(connectBuildings());
    }

    @GetMapping("/api/buildings/rooms-per-bathrooms")
    public Map<com.example.protocol.BuildingType, Double> getRoomsPerBathroom() {

        Map<com.example.protocol.BuildingType, Double> result = new HashMap<>(krzychuFeignClient.getRoomsPerBathroom());
        Map<com.example.protocol.BuildingType, Double> fromClient = bugzordFeignClient.getRoomsPerBathroom();

        for (com.example.protocol.BuildingType buildingType : com.example.protocol.BuildingType.getListTypes()) {
            final Double val = BigDecimal.valueOf((result.get(buildingType) + fromClient.get(buildingType)) / 2).setScale(2, RoundingMode.HALF_UP).doubleValue();
            result.put(buildingType, val);
        }

        return result;
    }

    @GetMapping("/api/buildings/rooms-in-type")
    public Map<com.example.protocol.BuildingType, Double> getRoomsInType() {

        Map<com.example.protocol.BuildingType, Double> result = new HashMap<>(krzychuFeignClient.getRoomsInType());
        Map<com.example.protocol.BuildingType, Double> fromClient = bugzordFeignClient.getRoomsInType();

        for (com.example.protocol.BuildingType buildingType : com.example.protocol.BuildingType.getListTypes()) {
            final Double val = BigDecimal.valueOf((result.get(buildingType) + fromClient.get(buildingType)) / 2).setScale(2, RoundingMode.HALF_UP).doubleValue();
            result.put(buildingType, val);
        }

        return result;
    }

    @GetMapping("/api/average/country")
    public Map<String, Double> getAvgPricePerCountry() {
        final Set<CountryAvgPrice> kSet = krzychuFeignClient.getAvgPricePerCountry();
        final Set<CountryAvgPrice> bSet = bugzordFeignClient.getAvgPricePerCountry();
        final Map<String, Double> result = new HashMap<>();

        kSet.forEach(k -> {
            if (bSet.stream().noneMatch(b -> k.getCountry().equals(b.getCountry()))) {
                result.put(k.getCountry(), k.getPrice());
            } else {
                final CountryAvgPrice bPrice = bSet.stream()
                        .filter(b -> b.getCountry().equals(k.getCountry()))
                        .findFirst().get();
                result.put(k.getCountry(), ( bPrice.getPrice() + k.getPrice() ) / 2);
            }
        });

        bSet.stream().forEach(b -> {
            if (kSet.stream().noneMatch(k -> k.getCountry().equals(b.getCountry()))) {
                result.put(b.getCountry(), b.getPrice());
            }
        });

        return result;
    }

}
