package com.jorken.persistence.betreuer;

import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.domain.Betreuer.ThemaView;
import com.jorken.persistence.betreuer.dto.*;
import com.jorken.service.betreuer.BetreuerRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BetreuerRepositoryImpl implements BetreuerRepository {

    private final BetreuerDataRepository repository;

    public BetreuerRepositoryImpl(BetreuerDataRepository repository){
        this.repository = repository;
    }

    @Override
    public Collection<Betreuer> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toBetreuer)
                .toList();
    }

    @Override
    public Optional<Betreuer> findById(UUID id) {
        return repository.findByFachId(id).map(this::toBetreuer);
    }

    @Override
    public void save(Betreuer betreuer) {
        Integer existingDbKey = repository.findByFachId(betreuer.getId()).map(BetreuerDto::id).orElse(null);
        BetreuerDto data = toBetreuerDto(existingDbKey, betreuer);
        repository.save(data);
    }

    @Override
    public Optional<Betreuer> findByGitHubName(String gitHubName) {
        return repository.findByGitHubName(gitHubName)
                .map(this::toBetreuer);
    }

    private Betreuer toBetreuer(BetreuerDto data){

        Betreuer b = Betreuer.ofWithId(data.fachId(), data.firstName(), data.lastName(), data.email(), data.phoneNumber(), data.beschreibung(), data.gitHubName());
        data.tags().forEach(e -> b.addTag(e.tag()));
        data.links().forEach(e -> b.addLink(e.link()));
        data.themen().forEach(
                e -> b.addThemaWithId(
                        e.fachId(),
                        e.name(),
                        e.markdown(),
                        new LinkedList<>(e.requirements().stream().map(RequirementDto::requirement).toList()),
                        new TreeSet<>(e.links().stream().map(ThemaLinkDto::link).toList())
                )
        );
        return b;
    }

    private BetreuerDto toBetreuerDto(Integer existingDbKey, Betreuer b){
        Set<TagDto> tags = b.getTags().stream().map(TagDto::new).collect(Collectors.toUnmodifiableSet());

        Set<ThemaDto> themen = b.getThemen().stream().map(e -> new ThemaDto(
                    null,
                    e.fachId(),
                    e.name(),
                    e.markdown(),
                    e.requirements().stream()
                            .map(RequirementDto::new)
                            .collect(Collectors.toUnmodifiableSet()),
                    e.links().stream()
                            .map(ThemaLinkDto::new)
                            .collect(Collectors.toUnmodifiableSet())
            )
        ).collect(Collectors.toUnmodifiableSet());

        Set<LinkDto> links = b.getLinks().stream().map(LinkDto::new).collect(Collectors.toUnmodifiableSet());


        return new BetreuerDto(
                existingDbKey,
                b.getId(),
                b.getFirstName(),
                b.getLastName(),
                b.getEmail(),
                b.getPhoneNumber(),
                b.getGitHubName(),
                tags,
                b.getBeschreibung(),
                themen,
                links
        );
    }
}
