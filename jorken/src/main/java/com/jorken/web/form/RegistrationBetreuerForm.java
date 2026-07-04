package com.jorken.web.form;

import com.jorken.annotations.FormModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@FormModel
public class RegistrationBetreuerForm {
    @NotNull(message ="Darf nicht leer sein")
    private final String firstName;
    @NotNull(message ="Darf nicht leer sein")
    private final String lastName;
    @NotNull(message ="Darf nicht leer sein")
    @Email(message = "Muss eine gültige Email sein")
    private final String email;
    private final String phoneNumber;
    @NotNull(message ="Darf nicht leer sein")
    private final String gitHubName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGitHubName() {
        return gitHubName;
    }

    public RegistrationBetreuerForm(String firstName, String lastName, String email, String phoneNumber, String gitHubName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gitHubName = gitHubName;
    }
}
