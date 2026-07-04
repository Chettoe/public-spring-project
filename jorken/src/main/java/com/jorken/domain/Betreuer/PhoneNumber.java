package com.jorken.domain.Betreuer;

record PhoneNumber(String phoneNumber) {

    PhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    static PhoneNumber of(String phoneNumber){
        return new PhoneNumber(phoneNumber);
    }
}
