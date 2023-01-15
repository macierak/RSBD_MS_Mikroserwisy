package com.example.connectorservice;

import com.example.bugzordclient.BugzordFeignClient;
import com.example.krzysztofclient.KrzychuFeignClient;
import com.example.protocol.BuildingDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@AllArgsConstructor
public class csc {

    final BugzordFeignClient client;
    final KrzychuFeignClient krzychuFeignClient;


    @GetMapping("/api")
    Set<BuildingDto> getBuildings(){
        return client.getBuildings();
    }

    @GetMapping("/api/krzychu")
    Set<BuildingDto> getBuildingsKrzycha(){
        return krzychuFeignClient.getBuildings();
    }
}
