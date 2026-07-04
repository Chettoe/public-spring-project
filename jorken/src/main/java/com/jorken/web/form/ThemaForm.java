package com.jorken.web.form;

import com.jorken.annotations.FormModel;
import jakarta.validation.constraints.NotNull;

@FormModel
public class ThemaForm {
    @NotNull(message ="Darf nicht leer sein")
    private final String name;
    private final String requirements;
    private final String links;

    public ThemaForm(String name, String requirements, String links) {
        this.name = name;
        this.requirements = requirements;
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getLinks() {
        return links;
    }
}
