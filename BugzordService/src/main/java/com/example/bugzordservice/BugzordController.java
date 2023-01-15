package com.example.bugzordservice;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@AllArgsConstructor
public class BugzordController {

    final BuildingRepo buildingRepo;
    final AddressRepo addressRepo;

    @GetMapping("/api/addresses")
    public List<Address> eg() {
        return addressRepo.findAll();
    }
    @GetMapping("/api/getBuildings")
    public List<Buildings> egsd() {
        return buildingRepo.findAll();
    }

}
