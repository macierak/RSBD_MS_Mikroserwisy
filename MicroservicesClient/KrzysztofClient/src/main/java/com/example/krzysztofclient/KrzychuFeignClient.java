package com.example.krzysztofclient;

import com.example.protocol.BuildingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@FeignClient(name = "krzysztof", url = "http://localhost:8070")
public interface KrzychuFeignClient {

    @GetMapping("/api/getBuildings")
    Set<BuildingDto> getBuildings();
}
