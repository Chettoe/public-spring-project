package com.jorken.domain.Betreuer;

import com.jorken.annotations.AggregateRoot;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

import java.util.*;

@AggregateRoot
public class Betreuer implements Comparable<Betreuer>{
    private final UUID id;
    private final PersonalData data;
    private final String gitHubName;
    private final Set<String> tags = new TreeSet<>();
    private String beschreibung;
    private final Set<Thema> themen = new HashSet<>();
    private final Set<String> links = new TreeSet<>();

    private Betreuer(PersonalData data, String beschreibung, String gitHubName) {
        this.id = UUID.randomUUID();
        this.data = data;
        this.gitHubName = gitHubName;
        this.beschreibung = beschreibung;
    }

    private Betreuer(UUID id, PersonalData data, String beschreibung, String gitHubName) {
        this.id = id;
        this.data = data;
        this.gitHubName = gitHubName;
        this.beschreibung = beschreibung;
    }

    public static Betreuer of(String firstName, String lastName, String email, String phoneNumber, String beschreibung, String gitHubName){
        return new Betreuer(PersonalData.of(firstName, lastName, email, phoneNumber), beschreibung, gitHubName);
    }

    public static Betreuer ofWithId(UUID id, String firstName, String lastName, String email, String phoneNumber, String beschreibung, String gitHubName) {
        return new Betreuer(id, PersonalData.of(firstName, lastName, email, phoneNumber), beschreibung, gitHubName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Betreuer betreuer = (Betreuer) o;
        return Objects.equals(id, betreuer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void changeEmail(String newEmail){
        this.data.setEmail(Email.of(newEmail));
    }

    public void changePhoneNumber(String newPhoneNumber){
        this.data.setPhoneNumber(PhoneNumber.of(newPhoneNumber));
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }

    public void removeTag(String tag){
        this.tags.remove(tag);
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return data.getFirstName();
    }

    public String getLastName() {
        return data.getLastName();
    }

    public String getEmail() {
        return this.data.getEmail().email();
    }

    public String getPhoneNumber() {
        return this.data.getPhoneNumber().phoneNumber();
    }

    public String getGitHubName() {return this.gitHubName;}

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void addThema(String name, String markdown, List<String> requirements, Set<String> links){
        this.themen.add(Thema.of(name, markdown, requirements, links));
    }

    public void addThemaWithId(UUID id, String name, String markdown, List<String> requirements, Set<String> links) {
        this.themen.add(Thema.ofWithId(id, name, markdown, requirements, links));
    }

    public void addLink(String link) {
        links.add(link);
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Set<String> getLinks() {
        return Collections.unmodifiableSet(links);
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public List<ThemaView> getThemen(){
        return themen.stream().map(
                e -> new ThemaView(
                        e.getId(),
                        e.getName(),
                        e.getMarkdown(),
                        e.getRequirements(),
                        e.getLinks()
                )
        ).toList();
    }

    @Override
    public int compareTo(Betreuer betreuer) {
        return this.getId().compareTo(betreuer.getId());
    }
}

