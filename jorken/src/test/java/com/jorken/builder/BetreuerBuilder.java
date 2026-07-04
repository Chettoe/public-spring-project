package com.jorken.builder;

import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.domain.Betreuer.ThemaView;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class BetreuerBuilder {
    private String firstName = "Jens";
    private String lastName = "Bendisposto";
    private String email = "jens@hhu.de";
    private String phoneNumber = "49123";
    private String gitHubName = "jens";
    private final Set<String> tags = new TreeSet<>();
    private String beschreibung = "test";
    private final Set<ThemaView> themen = new HashSet<>();
    private final Set<String> links = new HashSet<>();

    public static BetreuerBuilder betreuerBuilder() {
        return new BetreuerBuilder();
    }

    public BetreuerBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public BetreuerBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public BetreuerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public BetreuerBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public BetreuerBuilder withGitHubName(String gitHubName) {
        this.gitHubName = gitHubName;
        return this;
    }

    public BetreuerBuilder withTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    public BetreuerBuilder withBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
        return this;
    }

    public BetreuerBuilder withThema(String name, String markdown, Set<String> links, Set<String> requirements) {
        ThemaBuilder t = ThemaBuilder.themaBuilder()
                .withName(name)
                .withMarkdown(markdown);
        links.forEach(t::withLink);
        requirements.forEach(t::withRequirement);
        this.themen.add(t.build());
        return this;
    }

    public BetreuerBuilder withLink(String link) {
        this.links.add(link);
        return this;
    }

    public Betreuer build() {
        Betreuer b = Betreuer.of(
                this.firstName,
                this.lastName,
                this.email,
                this.phoneNumber,
                this.beschreibung,
                this.gitHubName
        );

        this.tags.forEach(b::addTag);
        this.links.forEach(b::addLink);
        this.themen.forEach(e -> {
            b.addThema(e.name(), e.markdown(), e.requirements().stream().toList(), e.links());
        });

        return b;
    }

}
