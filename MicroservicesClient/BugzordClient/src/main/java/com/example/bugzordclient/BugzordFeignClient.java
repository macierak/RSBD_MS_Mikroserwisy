package com.example.bugzordclient;

import com.example.protocol.BuildingDto;
import com.example.protocol.BuildingType;
import com.example.protocol.PriceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "bugzord", url = "http://localhost:8060")
public interface BugzordFeignClient {

    @GetMapping("/api/getBuildings")
    Set<BuildingDto> getBuildings();

    @GetMapping("/api/buildings/type")
    Set<BuildingDto> getBuildingsByType(@RequestParam String type);

    @GetMapping("/api/buildings/country")
    Set<BuildingDto> getBuildingsByCountry(@RequestParam String country);

    @GetMapping("/api/buildings/price")
    Set<BuildingDto> getBuildingsByPrice(@RequestParam(required = false) Long priceFrom,
                                         @RequestParam(required = false) Long priceTo);

    @GetMapping("/api/maxprice")
    Map<String, PriceDto> getMaxPriceForAllCountries();

    @GetMapping("/api/minprice")
    Map<String, PriceDto> getMinPriceForAllCountries();

    @GetMapping("/api/buildings/rooms-in-type")
    Map<BuildingType, Double> getRoomsPerBathroom();
}
