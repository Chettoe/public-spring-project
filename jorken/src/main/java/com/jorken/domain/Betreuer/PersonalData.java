package com.jorken.domain.Betreuer;

import jakarta.validation.constraints.NotNull;

class PersonalData {
    private final String firstName;
    private final String lastName;
    private Email email;
    private PhoneNumber phoneNumber;

    private PersonalData(String firstName, String lastName, String email, String phoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = Email.of(email);
        this.phoneNumber = PhoneNumber.of(phoneNumber);
    }

    static PersonalData of(String firstName, String lastName, String email, String phoneNumber){
        return new PersonalData(firstName, lastName, email, phoneNumber);
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    Email getEmail() {
        return email;
    }

    PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    void setEmail(Email email) {
        this.email = email;
    }

    void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
