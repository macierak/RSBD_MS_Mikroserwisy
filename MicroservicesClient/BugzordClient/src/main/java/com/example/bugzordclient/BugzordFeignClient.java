package com.example.bugzordclient;

import com.example.protocol.BuildingDto;
import com.example.protocol.PriceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "bugzord", url = "http://localhost:8060")
public interface BugzordFeignClient {

    @GetMapping("/api/getBuildings")
    Set<BuildingDto> getBuildings();

    @GetMapping("/api/maxprice")
    Map<String, PriceDto> getMaxPriceForAllCountries();

    @GetMapping("/api/minprice")
    Map<String, PriceDto> getMinPriceForAllCountries();
}
