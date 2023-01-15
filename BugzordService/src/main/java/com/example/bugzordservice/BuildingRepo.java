package com.example.bugzordservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BuildingRepo extends JpaRepository<Buildings, Long> {

    Page<Buildings> findAll(Pageable p);
}
