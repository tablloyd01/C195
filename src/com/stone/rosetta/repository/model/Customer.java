package com.stone.rosetta.repository.model;

public class Customer extends Entity {
    private String name;
    private Boolean active;
    private Address address;
    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }
    
    public Customer(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Customer(Long id, String name, Boolean active) {
        super(id);
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
}
