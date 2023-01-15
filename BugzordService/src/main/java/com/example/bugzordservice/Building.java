package com.example.bugzordservice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "buildings")
public class Building {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private BuildingType type;

    private Long price;

    private Short bedrooms;
    private Short bathrooms;

}
