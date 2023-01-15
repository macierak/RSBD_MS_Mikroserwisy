package com.example.bugzordservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class Address {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String country;
    private String city;

    private String street;

}
