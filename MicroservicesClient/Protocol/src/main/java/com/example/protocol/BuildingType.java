package com.example.protocol;

import java.util.List;

public enum BuildingType {
    Apartment,
    Single,
    Twin,
    Tenement;

    public static List<BuildingType> getListTypes() {
        return List.of(BuildingType.Apartment, BuildingType.Single, BuildingType.Twin, BuildingType.Tenement);
    }
}
