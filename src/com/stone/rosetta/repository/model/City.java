package com.stone.rosetta.repository.model;

public class City extends Entity {
    private String name;
    private Country country;

    public City(String name) {
        this.name = name;
    }

    public City(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public City(Long id, String name) {
        super(id);
        this.name = name;
    }
    
    

    public City(Long id, String name, Country country) {
        super(id);
        this.name = name;
        this.country = country;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
