package com.example.connectorservice;

import com.example.bugzordclient.BugzordFeignClient;
import com.example.connectorservice.math.MathService;
import com.example.krzysztofclient.KrzychuFeignClient;
import com.example.protocol.BuildingDto;
import com.example.protocol.PriceDto;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@AllArgsConstructor
public class ConnectorServiceController {

    final BugzordFeignClient client;
    final KrzychuFeignClient krzychuFeignClient;

    final MathService mathService;

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

    @GetMapping("/api/minprice")
    public Map<String, PriceDto> getMinPriceForAllCountries() {
        final Map<String, PriceDto> bMap = client.getMinPriceForAllCountries();
        final Map<String, PriceDto> kMap = krzychuFeignClient.getMinPriceForAllCountries();

        final Map<String, PriceDto> resultMap = new HashMap<>(bMap);

        kMap.forEach((k, v) -> {
            if (!resultMap.containsKey(k)) {
                resultMap.put(k, v);
            } else {
                final PriceDto dto = resultMap.get(k);
                if (dto.getPrice() > v.getPrice()) {
                    resultMap.replace(k, v);
                }
            }
        });

        return resultMap;
    }

    @GetMapping("/api/maxprice")
    public Map<String, PriceDto> getMaxPriceForAllCountries() {
        final Map<String, PriceDto> bMap = client.getMaxPriceForAllCountries();
        final Map<String, PriceDto> kMap = krzychuFeignClient.getMaxPriceForAllCountries();

        final Map<String, PriceDto> resultMap = new HashMap<>(bMap);

        kMap.forEach((k, v) -> {
            if (!resultMap.containsKey(k)) {
                resultMap.put(k, v);
            } else {
                final PriceDto dto = resultMap.get(k);
                if (dto.getPrice() < v.getPrice()) {
                    resultMap.replace(k, v);
                }
            }
        });

        return resultMap;
    }

    @GetMapping("/api/price/statistics")
    public Map<String, Map.Entry<PriceDto, PriceDto>> getPriceStatistics() {
        Map<String, PriceDto> maxPrices = getMaxPriceForAllCountries();
        Map<String, PriceDto> minPrices = getMinPriceForAllCountries();

        Map<String, Map.Entry<PriceDto, PriceDto>> response = new HashMap<>();

        maxPrices.forEach((k, MaxV) -> {
            final PriceDto lowV = minPrices.get(k);

            Map.Entry<PriceDto, PriceDto> pair = new AbstractMap.SimpleEntry<>(MaxV, lowV);
            response.put(k, pair);
        });

        minPrices.forEach((k, v) -> {
            if (!response.containsKey(k)) {
                response.put(k, new AbstractMap.SimpleEntry<>(v, null));
            }
        });

        return response;
    }

    @GetMapping("/api/locals/percentage")
    public Map<String, Set<LocalePercentageResponse>> getPercentage() {
        return MathService.getPercentage(connectBuildings());
    }

}
