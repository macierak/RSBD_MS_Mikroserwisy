package com.example.bugzordservice;

import com.example.protocol.BuildingDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class BugzordController {

    final BuildingRepo buildingRepo;
    final AddressRepo addressRepo;

    final static Pageable P = PageRequest.of(0, 100);

    @GetMapping("/api/addresses")
    public Set<Address> addresses() {
        return addressRepo.findAll(P).stream().collect(Collectors.toSet());
    }
    @GetMapping("/api/getBuildings")
    public Set<BuildingDto> buildings() {
        return buildingRepo.findAll(P).stream().map(BugzordController::map).collect(Collectors.toSet());
    }

    private static BuildingDto map(Buildings b) {
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
