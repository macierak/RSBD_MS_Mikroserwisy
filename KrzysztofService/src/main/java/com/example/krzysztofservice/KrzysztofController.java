package com.example.krzysztofservice;

import com.example.protocol.BuildingDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class KrzysztofController {

    final BuildingRepo buildingRepo;
    final AddressRepo addressRepo;

    @GetMapping("/api/getBuildings")
    public List<Building> getBuildings() {
        return buildingRepo.findAll();
    }

    @GetMapping("/api/getAddresses")
    public List<Address> getAddresses() {
        return addressRepo.findAll();
    }

}
