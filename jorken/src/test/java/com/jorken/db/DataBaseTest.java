package com.jorken.db;

import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.persistence.betreuer.BetreuerDataRepository;
import com.jorken.persistence.betreuer.BetreuerRepositoryImpl;
import com.jorken.service.betreuer.BetreuerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ContainerConfig.class)
public class DataBaseTest {

    @Autowired
    BetreuerDataRepository dataRepository;

    BetreuerRepository repository;

    @BeforeEach
    void setup() {
        repository = new BetreuerRepositoryImpl(dataRepository);
    }

    @Test
    @DisplayName("Ein Betreuer kann gespeichert und geladen werden")
    void test_1() {
        Betreuer b = Betreuer.of(
                "Kaco",
                "Jorken",
                "mail",
                "123",
                "",
                "name"
        );

        repository.save(b);
        Optional<Betreuer> loaded = repository.findById(b.getId());

        assertThat(loaded.map(Betreuer::getFirstName).orElseThrow()).isEqualTo("Kaco");
        assertThat(loaded.map(Betreuer::getLastName).orElseThrow()).isEqualTo("Jorken");
        assertThat(loaded.map(Betreuer::getEmail).orElseThrow()).isEqualTo("mail");
        assertThat(loaded.map(Betreuer::getPhoneNumber).orElseThrow()).isEqualTo("123");
        assertThat(loaded.map(Betreuer::getGitHubName).orElseThrow()).isEqualTo("name");
    }

    @Test
    @DisplayName("Es können alle existierenden Betreuer zurückgegeben werden")
    void test_2() {
        Betreuer b1 = Betreuer.of(
                "Kaco",
                "Jorken",
                "mail",
                "123",
                "",
                "name1"
        );

        Betreuer b2 = Betreuer.of(
                "Kaco",
                "Jorken",
                "mail",
                "123",
                "",
                "name2"
        );

        Betreuer b3 = Betreuer.of(
                "Kaco",
                "Jorken",
                "mail",
                "123",
                "",
                "name3"
        );

        repository.save(b1);
        repository.save(b2);
        repository.save(b3);
        Collection<Betreuer> coll = repository.findAll();

        assertThat(coll.size()).isEqualTo(3);
    }
}
