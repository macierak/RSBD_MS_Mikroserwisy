package com.example.krzysztofservice;

import com.example.protocol.BuildingDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class KrzysztofController {

    final BuildingRepo buildingRepo;
    final AddressRepo addressRepo;

    @GetMapping("/api/getBuildings")
    public Set<BuildingDto> getBuildings() {
        return buildingRepo.findAll().stream().map(KrzysztofController::map).collect(Collectors.toSet());
    }

    private static BuildingDto map(Building b) {
        return BuildingDto.builder()
                .bathrooms(b.getBathrooms())
                .bedrooms(b.getBedrooms())
                .city(b.getAddress().getCity())
                .country(b.getAddress().getCountry())
                .price(b.getPrice())
                .street(b.getAddress().getStreet())
                .type(b.getType().toString())
                .build();
    }
}
