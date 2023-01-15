package com.example.krzysztofservice;

import com.example.protocol.BuildingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepo extends JpaRepository<Building, Long> {
    Page<Building> findAll(Pageable p);

    Page<Building> findAllByType(BuildingType type, Pageable p);

    Page<Building> findAllByAddress_Country(String country, Pageable p);

    Page<Building> findAllByPriceAfterAndPriceBefore(Long priceFrom, Long priceTo, Pageable pageable);
}
