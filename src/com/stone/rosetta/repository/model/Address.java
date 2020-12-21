package com.stone.rosetta.repository.model;

public class Address extends Entity {

    private String line1;
    private String line2;
    private String postalCode;
    private String phone;
    private City city;

    public Address() {
    }

    public Address(Long id, String line1, String line2, String postalCode, String phone, City city) {
        super(id);
        this.line1 = line1;
        this.line2 = line2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.city = city;
    }

    public Address(String line1, String line2, String postalCode, String phone) {
        this.line1 = line1;
        this.line2 = line2;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getFullAddress() {
        return this.line1.concat(", ").concat(line2).concat(", ").concat(city.getName()).concat(", ").concat(city.getCountry().getName());
    }
}
