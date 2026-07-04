package com.jorken.persistence.betreuer.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Table("betreuer")
public record BetreuerDto(@Id Integer id,
                          UUID fachId,
                          String firstName,
                          String lastName,
                          String email,
                          String phoneNumber,
                          String gitHubName,
                          Set<TagDto> tags,
                          String beschreibung,
                          Set<ThemaDto> themen,
                          Set<LinkDto> links) {
    public BetreuerDto{
        tags = tags == null ? Set.of() : Set.copyOf(tags);
        themen = themen == null ? Set.of() : Set.copyOf(themen);
        links = links == null ? Set.of() : Set.copyOf(links);
    }
}
