package com.jorken.persistence.betreuer.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Table("betreuer_themen")
public record ThemaDto(@Id Integer id,
                       UUID fachId,
                       String name,
                       String markdown,
                       Set<RequirementDto> requirements,
                       Set<ThemaLinkDto> links) {
    public ThemaDto{
        requirements = requirements == null ? Set.of() : Set.copyOf(requirements);
        links = links == null ? Set.of() : Set.copyOf(links);
    }
}
