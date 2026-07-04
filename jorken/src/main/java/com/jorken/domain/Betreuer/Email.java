package com.jorken.domain.Betreuer;

record Email(String email) {

    Email(String email){
        this.email = email;
    }

    static Email of(String email){
        return new Email(email);
    }
}
