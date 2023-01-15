package com.example.connectorservice;

import com.example.bugzordclient.BugzordFeignClient;
import com.example.krzysztofclient.KrzychuFeignClient;
import com.example.protocol.BuildingDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@AllArgsConstructor
public class ConnectorServiceController {

    final BugzordFeignClient client;
    final KrzychuFeignClient krzychuFeignClient;


    @GetMapping("/api/bugzord")
    Set<BuildingDto> getBuildings(){
        return client.getBuildings();
    }

    @GetMapping("/api/krzychu")
    Set<BuildingDto> getBuildingsKrzycha(){
        return krzychuFeignClient.getBuildings();
    }

    @GetMapping("/api/connect")
    Set<BuildingDto> connectBuildings() {
        final Set<BuildingDto> result = new HashSet<>();
        result.addAll(krzychuFeignClient.getBuildings());
        result.addAll(client.getBuildings());

        return result;

    }

}
