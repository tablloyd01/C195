package com.stone.rosetta.repository.model;

public class Country extends Entity {
    private String name;

    public Country(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Country(Long id) {
        super(id);
    }
    

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
