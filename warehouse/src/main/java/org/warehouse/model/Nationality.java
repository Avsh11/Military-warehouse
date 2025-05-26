package org.warehouse.model;

public class Nationality {
    private int nationalityID;
    private String country;

    public Nationality(int id, String nationality) {
        this.nationalityID = id;
        this.country = nationality;
    }

    @Override
    public String toString() {
        return country;
    }

    // Getters & Setters

    public int getNationalityID() {
        return nationalityID;
    }

    public void setNationalityID(int nationalityID) {
        this.nationalityID = nationalityID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
