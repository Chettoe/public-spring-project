package com.jorken.domain.Betreuer;

import org.springframework.data.annotation.Id;

import java.util.*;

class Thema {
    private UUID id;
    private String name;
    private String markdown;
    private final Set<String> links = new TreeSet<>();
    //Files
    private final Set<String> requirements = new TreeSet<>();

    private Thema(String name, String markdown, List<String> requirements, Set<String> links){
        this.id = UUID.randomUUID();
        this.name = name;
        this.markdown = markdown;
        this.requirements.addAll(requirements);
        this.links.addAll(links);
    }

    private Thema(UUID id, String name, String markdown, List<String> requirements, Set<String> links){
        this.id = id;
        this.name = name;
        this.markdown = markdown;
        this.requirements.addAll(requirements);
        this.links.addAll(links);
    }

    static Thema of(String name, String markdown, List<String> requirements, Set<String> links){
        return new Thema(name, markdown, requirements, links);
    }

    static Thema ofWithId(UUID id, String name, String markdown, List<String> requirements, Set<String> links) {
        return new Thema(id, name, markdown, requirements, links);
    }


    public String getName() {
        return name;
    }

    public String getMarkdown() {
        return markdown;
    }

    public Set<String> getRequirements() {
        return Collections.unmodifiableSet(requirements);
    }

    public Set<String> getLinks() {
        return Collections.unmodifiableSet(links);
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Thema thema = (Thema) o;
        return Objects.equals(id, thema.id) && Objects.equals(name, thema.name) && Objects.equals(markdown, thema.markdown) && Objects.equals(requirements, thema.requirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, markdown, requirements);
    }

    public UUID getId() {
        return id;
    }
}
