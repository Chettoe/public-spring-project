package com.jorken.service.betreuer;

import com.jorken.domain.Betreuer.Betreuer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface BetreuerRepository {
    Collection<Betreuer> findAll();
    Optional<Betreuer> findById(UUID id);
    void save(Betreuer betreuer);
    Optional<Betreuer> findByGitHubName(String gitHubName);
}

