package com.example.alphasolutionseksamen.model;

public class User {
    private String username;

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String streetName;
    private String streetNumber;
    private int postNumber;
    private int phoneNumber;

    private String city;
    private String country;

    public User(String username, String firstName, String lastName, String password, String email, String streetName, String streetNumber, int postNumber, String city, int phoneNumber, String country) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.postNumber = postNumber;
        this.city=city;
        this.phoneNumber = phoneNumber;
        this.country = country;
    }

    public User(String username, String firstName, String lastName, String email, String streetName, String streetNumber, int postNumber, int phoneNumber, String city, String country) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.postNumber = postNumber;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.country = country;
    }

    public User(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
