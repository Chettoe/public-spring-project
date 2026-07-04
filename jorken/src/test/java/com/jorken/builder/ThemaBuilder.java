package com.jorken.builder;

import com.jorken.domain.Betreuer.ThemaView;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class ThemaBuilder {
    private String name = "Propra2";
    private String markdown = "Projekt_info";
    private final Set<String> links = new TreeSet<>();
    private final Set<String> requirements = new TreeSet<>();

    public static ThemaBuilder themaBuilder() {
        return new ThemaBuilder();
    }

    public ThemaBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ThemaBuilder withMarkdown(String markdown) {
        this.markdown = markdown;
        return this;
    }

    public ThemaBuilder withLink(String link) {
        this.links.add(link);
        return this;
    }

    public ThemaBuilder withRequirement(String requirement) {
        this.requirements.add(requirement);
        return this;
    }

    public ThemaView build() {
        return new ThemaView(
                UUID.randomUUID(),
                this.name,
                this.markdown,
                this.requirements,
                this.links
                );
    }
}
