package com.example.bugzordservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepo extends JpaRepository<Buildings, Long> {
}
