package com.example.krzysztofservice;

import com.example.protocol.BuildingDto;
import com.example.protocol.BuildingType;
import com.example.protocol.PriceDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
public class KrzysztofController {

    final BuildingRepo buildingRepo;
    final AddressRepo addressRepo;

    final static Integer LIMIT = 100;
    final static Pageable P = PageRequest.of(0, LIMIT);

    @GetMapping("/api/getBuildings")
    public Set<BuildingDto> buildings() {
        return buildingRepo.findAll(P).map(KrzysztofController::map).toSet();
    }

    @GetMapping("/api/getAddresses")
    public Set<Address> getAddresses() {
        return addressRepo.findAll(P).toSet();
    }

    @GetMapping("/api/buildings/type")
    public Set<BuildingDto> getBuildingsByType(@RequestParam BuildingType type) {
        return buildingRepo.findAllByType(type, P).map(KrzysztofController::map).toSet();
    }

    @GetMapping("/api/buildings/country")
    public Set<BuildingDto> getBuildingsByCountry(@RequestParam String country) {
        return buildingRepo.findAllByAddress_Country(country, P).map(KrzysztofController::map).toSet();
    }

    @GetMapping("/api/buildings/price")
    public Set<BuildingDto> getBuildingsByPrice(@RequestParam(required = false) Long priceFrom,
                                                @RequestParam(required = false) Long priceTo) {

        if (Objects.isNull(priceFrom)) priceFrom = 0L;
        if (Objects.isNull(priceTo)) priceTo = Long.MAX_VALUE;

        return buildingRepo.findAllByPriceAfterAndPriceBefore(priceFrom, priceTo, P).map(KrzysztofController::map).toSet();
    }

    @GetMapping("/api/buildings/rooms-in-type")
    public Map<BuildingType, Double> getRoomsPerBathroom() {
        final Map<BuildingType, Double> result = new HashMap<>();
        Set<Building> buildings = buildingRepo.findAll(P).toSet();
        for (BuildingType buildingType : BuildingType.getListTypes()) {
            result.put(buildingType, calculateRoomsByType(
                    buildings.stream().filter(b -> b.getType().equals(buildingType)).collect(Collectors.toSet())));
        }

        return result;
    }
    private static Double calculateRoomsByType(Set<Building> buildings) {
        return BigDecimal.valueOf(buildings.stream().mapToDouble(Building::getBedrooms).sum() / buildings.stream().mapToDouble(Building::getBathrooms).sum()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @GetMapping("/api/maxprice")
    public Map<String, PriceDto> getMaxPriceForAllCountries() {
        Map<String, PriceDto> result = new HashMap<>();
        buildingRepo.findAll(P).forEach(b -> {
            final String country = b.getAddress().getCountry();
            if (result.containsKey(country)) {
                if (result.get(country).getPrice() < b.getPrice()) {
                    result.replace(country, mapPrice(b));
                }
            } else {
                result.put(country, mapPrice(b));
            }
        });
        return result;
    }

    @GetMapping("/api/minprice")
    public Map<String, PriceDto> getMinPriceForAllCountries() {
        Map<String, PriceDto> result = new HashMap<>();
        buildingRepo.findAll(P).forEach(b -> {
            final String country = b.getAddress().getCountry();
            if (result.containsKey(country)) {
                if (result.get(country).getPrice() > b.getPrice()) {
                    result.replace(country, mapPrice(b));
                }
            } else {
                result.put(country, mapPrice(b));
            }
        });
        return result;
    }

    private PriceDto mapPrice(Building b) {
        return PriceDto.builder()
                .city(b.getAddress().getCity())
                .price(b.getPrice())
                .street(b.getAddress().getStreet())
                .type(b.getType().toString())
                .build();
    }

    private static BuildingDto map(Building b) {
        return BuildingDto.builder()
                .type(b.getType().toString())
                .id(b.getId())
                .price(b.getPrice())
                .country(b.getAddress().getCountry())
                .city(b.getAddress().getCity())
                .bedrooms(b.getBedrooms())
                .bathrooms(b.getBathrooms())
                .street(b.getAddress().getStreet())
                .build();
    }

}
