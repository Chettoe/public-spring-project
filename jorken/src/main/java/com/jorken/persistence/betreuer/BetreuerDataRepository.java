package com.jorken.persistence.betreuer;

import com.jorken.persistence.betreuer.dto.BetreuerDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface BetreuerDataRepository extends CrudRepository<BetreuerDto, Integer> {
    Collection<BetreuerDto> findAll();
    Optional<BetreuerDto> findByFachId(UUID id);
    BetreuerDto save(BetreuerDto betreuer);
    Optional<BetreuerDto> findByGitHubName(String gitHubName);
}
