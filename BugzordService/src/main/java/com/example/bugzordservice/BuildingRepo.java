package com.example.bugzordservice;

import com.example.protocol.BuildingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BuildingRepo extends JpaRepository<Buildings, Long> {

    Page<Buildings> findAll(Pageable p);

    Page<Buildings> findAllByType(BuildingType type, Pageable p);

    Page<Buildings> findAllByAddress_Country(String country, Pageable p);

    Page<Buildings> findAllByPriceAfterAndPriceBefore(Long priceFrom, Long priceTo, Pageable pageable);
}
