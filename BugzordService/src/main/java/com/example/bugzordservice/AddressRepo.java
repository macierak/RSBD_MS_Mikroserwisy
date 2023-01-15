package com.example.bugzordservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
