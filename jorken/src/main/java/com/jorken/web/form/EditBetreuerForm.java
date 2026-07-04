package com.jorken.web.form;

import com.jorken.annotations.FormModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@FormModel
public class EditBetreuerForm {
    @NotNull(message ="Darf nicht leer sein")
    @Email(message = "Muss eine gültige Email sein")
    private final String email;
    private final String phoneNumber;
    private final String beschreibung;
    private final String tags;
    private final String links;

    public EditBetreuerForm(String email, String phoneNumber, String beschreibung, String tags, String links) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.beschreibung = beschreibung;
        this.tags = tags;
        this.links = links;
    }


    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public String getTags() {
        return tags;
    }

    public String getLinks() {
        return links;
    }
}
