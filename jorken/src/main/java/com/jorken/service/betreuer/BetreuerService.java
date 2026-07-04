package com.jorken.service.betreuer;

import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.domain.Betreuer.ThemaView;
import com.jorken.service.exception.ObjektNotFoundException;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BetreuerService {

    private final BetreuerRepository repository;

    public BetreuerService(BetreuerRepository repository){
        this.repository = repository;
    }

    public List<Betreuer> allBetreuer(){
        return repository.findAll().stream().sorted().toList();
    }

    public List<ThemaView> allThemen(UUID id) {
        Betreuer b = betreuer(id);
        if(b == null) throw new ObjektNotFoundException();
        return b.getThemen();
    }

    public Betreuer betreuer(UUID id){
        return repository.findById(id).orElseThrow(ObjektNotFoundException::new);
    }

    public UUID addBetreuer(String firstName, String lastName, String email, String phoneNumber, String gitHubName) {
        Betreuer b = Betreuer.of(firstName, lastName, email, phoneNumber, "", gitHubName);
        repository.save(b);
        return b.getId();
    }

    public void addThema(UUID id, String name, String markdown, Set<String> requirements, Set<String> links) {
        Betreuer b = betreuer(id);
        if(b == null) throw new ObjektNotFoundException();
        b.addThema(name, markdown, requirements.stream().toList(), links);
        repository.save(b);
    }

    public void addBeschreibung(UUID id, String beschreibung) {
        Betreuer b = betreuer(id);
        if(b == null) throw new ObjektNotFoundException();
        b.setBeschreibung(beschreibung);
        repository.save(b);
    }

    public void setEmail(UUID id, String email) {
        Betreuer b = betreuer(id);
        if(b == null) throw new ObjektNotFoundException();
        b.changeEmail(email);
        repository.save(b);
    }

    public void setPhoneNumber(UUID id, String phoneNumber) {
        Betreuer b = betreuer(id);
        if(b == null) throw new ObjektNotFoundException();
        b.changePhoneNumber(phoneNumber);
        repository.save(b);
    }

    public void setTags(UUID id, Set<String> tags) {
        Betreuer b = betreuer(id);
        if(b == null) throw new ObjektNotFoundException();
        //neues objekt erstellen, da tags immuatble ist
        Betreuer update = Betreuer.ofWithId(
                b.getId(),
                b.getFirstName(),
                b.getLastName(),
                b.getEmail(),
                b.getPhoneNumber(),
                b.getBeschreibung(),
                b.getGitHubName()
        );
        b.getLinks().forEach(update::addLink);
        tags.forEach(update::addTag);

        List<ThemaView> themen = b.getThemen();
        themen.forEach(e -> update.addThema(e.name(), e.markdown(), e.requirements().stream().toList(), e.links()));
        repository.save(update);
    }

    public Betreuer betreuerByGitHubName(String gitHubName) {
        Betreuer b = repository.findByGitHubName(gitHubName).orElse(null);
        if(b == null) throw new ObjektNotFoundException();
        return b;
    }

    public ThemaView themaWithUUID(UUID betreuerId, UUID themaId) {
        Betreuer b = repository.findById(betreuerId).orElse(null);
        if(b == null) throw new ObjektNotFoundException();
        return b.getThemen()
                .stream()
                .filter(e -> e.fachId().equals(themaId))
                .findFirst().orElseThrow(ObjektNotFoundException::new);
    }

    public void updateThema(UUID betreuerId, ThemaView thema) {
        Betreuer b = betreuer(betreuerId);
        if(b == null) throw new ObjektNotFoundException();
        Betreuer update = Betreuer.ofWithId(
                betreuerId,
                b.getFirstName(),
                b.getLastName(),
                b.getEmail(),
                b.getPhoneNumber(),
                b.getBeschreibung(),
                b.getGitHubName()
                );
        b.getLinks().forEach(update::addLink);
        b.getTags().forEach(update::addTag);
        b.getThemen().forEach(e -> {
            if(e.fachId().equals(thema.fachId())) {
                update.addThemaWithId(
                        thema.fachId(),
                        thema.name(),
                        thema.markdown(),
                        thema.requirements().stream().toList(),
                        new HashSet<>(thema.links())
                );
            }else {
                update.addThemaWithId(
                        e.fachId(),
                        e.name(),
                        e.markdown(),
                        e.requirements().stream().toList(),
                        new HashSet<>(thema.links())
                );
            }
        });

        repository.save(update);
    }

    private boolean contains(String s, String search) {
        return s.toLowerCase().contains(search);
    }

    private boolean containedInCollection(Collection<String> coll, String search) {
        return coll.stream().anyMatch(e -> e.toLowerCase().contains(search));
    }

    public List<Betreuer> searchBetreuer(String search) {
        if(search.isBlank()) return List.of();

        String s = search.toLowerCase();

        return repository.findAll().stream()
                .filter(e ->
                        contains(e.getFirstName(), s) ||
                                contains(e.getLastName(), s) ||
                                contains(e.getGitHubName(), s) ||
                                containedInCollection(e.getTags(), s)
                )
                .toList();
    }

    public List<ThemaView> searchThemen(String search) {
        if(search.isBlank()) return List.of();

        String s = search.toLowerCase();

        Collection<Betreuer> all = repository.findAll();
        List<ThemaView> themen = new LinkedList<>();
        all.forEach(e -> themen.addAll(e.getThemen()));

        return themen.stream()
                .filter(e ->
                        contains(e.name(), s) ||
                                containedInCollection(e.requirements(), s)
                        )
                .toList();
    }

    public ThemaView themaById(UUID id) {
        return allBetreuer().stream()
                .flatMap(e -> e.getThemen().stream())
                .filter(e -> e.fachId().equals(id))
                .findFirst()
                .orElseThrow(ObjektNotFoundException::new);
    }

    public void setLinks(UUID id, Set<String> links) {
        Betreuer b = betreuer(id);
        if(b == null) throw new ObjektNotFoundException();
        Betreuer update = Betreuer.ofWithId(
                b.getId(),
                b.getFirstName(),
                b.getLastName(),
                b.getEmail(),
                b.getPhoneNumber(),
                b.getBeschreibung(),
                b.getGitHubName()
        );
        links.forEach(update::addLink);
        b.getTags().forEach(update::addTag);

        List<ThemaView> themen = b.getThemen();
        themen.forEach(e -> update.addThema(e.name(), e.markdown(), e.requirements().stream().toList(), e.links()));
        repository.save(update);
    }

    public Betreuer betreuerByThema(UUID fachId) {
        Collection<Betreuer> all = repository.findAll();
        for(Betreuer b : all) {
            for(ThemaView t : b.getThemen()) {
                if(t.fachId().equals(fachId)) return b;
            }
        }
        throw new ObjektNotFoundException();
    }
}
