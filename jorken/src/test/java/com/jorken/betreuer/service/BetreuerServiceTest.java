package com.jorken.betreuer.service;

import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.domain.Betreuer.ThemaView;
import com.jorken.service.betreuer.BetreuerRepository;
import com.jorken.service.betreuer.BetreuerService;
import com.jorken.service.exception.ObjektNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BetreuerServiceTest {
    BetreuerRepository repository = mock(BetreuerRepository.class);

    static List<Betreuer> db = new LinkedList<>();

    @BeforeAll
    static void setup() {
        db.addAll(List.of(
                Betreuer.of(
                        "Jens",
                        "Bendisposto",
                        "jens@hhu.de",
                        "49123",
                        "",
                        "jens"
                ),

                Betreuer.of(
                        "Schurke",
                        "Magmar",
                        "schurke@hhu.de",
                        "49123",
                        "",
                        "schurke"
                ),

                Betreuer.of(
                        "Gangstar",
                        "Mozzarella",
                        "gangstar@hhu.de",
                        "49123",
                        "",
                        "gang"
                )));
    }

    @Test
    @DisplayName("Es kann ein Betreuer hinzugefügt werden")
    void test_1() {
        BetreuerService service = new BetreuerService(repository);
        UUID id = service.addBetreuer(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "jens"
        );

        ArgumentCaptor<Betreuer> captor = ArgumentCaptor.forClass(Betreuer.class);
        verify(repository).save(captor.capture());
        Betreuer saved = captor.getValue();

        assertThat(saved.getFirstName()).isEqualTo("Jens");
        assertThat(saved.getLastName()).isEqualTo("Bendisposto");
        assertThat(saved.getEmail()).isEqualTo("jens@hhu.de");
        assertThat(saved.getPhoneNumber()).isEqualTo("49123");
        assertThat(saved.getGitHubName()).isEqualTo("jens");
        assertThat(saved.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Es können alle Betreuer ausgegeben werden")
    void test_2() {
        BetreuerService service = new BetreuerService(repository);
        when(repository.findAll()).thenReturn(db);

        List<Betreuer> liste = service.allBetreuer();
        assertThat(liste).containsAll(db.subList(0, db.size()-1));
    }

    @Test
    @DisplayName("Es kann ein Thema zu einem Betreuer hinzugefügt werden")
    void test_3() {
        Betreuer b = Betreuer.of(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "",
                "jens"
        );

        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(b));

        BetreuerService service = new BetreuerService(repository);
        service.addThema(UUID.randomUUID(), "Mocking", "Nothing", Set.of("Propra"), Set.of("link"));

        ArgumentCaptor<Betreuer> captor = ArgumentCaptor.forClass(Betreuer.class);
        verify(repository).save(captor.capture());
        Betreuer saved = captor.getValue();

        assertThat(saved.getThemen()).allMatch(e -> {
            String name = e.name();
            String markdown = e.markdown();
            Set<String> set = e.requirements();
            return name.equals("Mocking") && markdown.equals("Nothing") && set.contains("Propra");
        });
    }

    @Test
    @DisplayName("Suche nach einem nicht vorhandem Betreuer wirft eine ObjectNotFound Exception")
    void test_4() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        BetreuerService service = new BetreuerService(repository);
        assertThat(service.allBetreuer()).isEmpty();
        assertThrows(ObjektNotFoundException.class, () -> service.betreuer(id));
    }

    @Test
    @DisplayName("Einem Betreuer kann eine neue Beschreibung hinzugefügt werden")
    void test_5() {
        Betreuer b = Betreuer.of(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "",
                "jens"
        );

        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(b));

        BetreuerService service = new BetreuerService(repository);

        service.addBeschreibung(UUID.randomUUID(), "neue Beschreibung");

        ArgumentCaptor<Betreuer> captor = ArgumentCaptor.forClass(Betreuer.class);
        verify(repository).save(captor.capture());
        Betreuer saved = captor.getValue();

        assertThat(saved.getBeschreibung()).isEqualTo("neue Beschreibung");
    }

    @Test
    @DisplayName("Es kann ein Betreuer mit einem GitHub Namen gefunden werden")
    void test_6() {
        Betreuer b = Betreuer.of(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "",
                "jens"
        );

        when(repository.findByGitHubName("jens")).thenReturn(Optional.of(b));

        BetreuerService service = new BetreuerService(repository);

        Betreuer same = service.betreuerByGitHubName("jens");

        assertThat(same.getGitHubName()).isEqualTo("jens");
    }

    @Test
    @DisplayName("Wenn nach einem GitHub Namen gesucht wird der nicht existiert, wird eine ObjectNotFoundException")
    void test_7() {
        Betreuer b = Betreuer.of(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "",
                "jens"
        );

        when(repository.findByGitHubName("nicht_jens")).thenReturn(Optional.empty());

        BetreuerService service = new BetreuerService(repository);

        assertThrows(ObjektNotFoundException.class, () -> service.betreuerByGitHubName("nicht_jens"));
    }

    @Test
    @DisplayName("Einem Betreuer kann eine neue Email hinzugefügt werden")
    void test_8() {
        Betreuer b = Betreuer.of(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "",
                "jens"
        );

        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(b));

        BetreuerService service = new BetreuerService(repository);

        service.setEmail(UUID.randomUUID(), "new@hhu.de");

        ArgumentCaptor<Betreuer> captor = ArgumentCaptor.forClass(Betreuer.class);
        verify(repository).save(captor.capture());
        Betreuer saved = captor.getValue();

        assertThat(saved.getEmail()).isEqualTo("new@hhu.de");
    }

    @Test
    @DisplayName("Einem Betreuer kann eine neue Telefonnummer hinzugefügt werden")
    void test_9() {
        Betreuer b = Betreuer.of(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "",
                "jens"
        );

        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(b));

        BetreuerService service = new BetreuerService(repository);

        service.setPhoneNumber(UUID.randomUUID(), "499876");

        ArgumentCaptor<Betreuer> captor = ArgumentCaptor.forClass(Betreuer.class);
        verify(repository).save(captor.capture());
        Betreuer saved = captor.getValue();

        assertThat(saved.getPhoneNumber()).isEqualTo("499876");
    }

    @Test
    @DisplayName("Einem Betreuer können tags hinzugefügt werden")
    void test_10() {
        Betreuer b = Betreuer.of(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "",
                "jens"
        );

        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(b));

        BetreuerService service = new BetreuerService(repository);

        service.setTags(UUID.randomUUID(), Set.of("ABC", "ABD", "123"));

        ArgumentCaptor<Betreuer> captor = ArgumentCaptor.forClass(Betreuer.class);
        verify(repository).save(captor.capture());
        Betreuer saved = captor.getValue();

        assertThat(saved.getTags()).isEqualTo(Set.of("ABC", "ABD", "123"));

    }

    @Test
    @DisplayName("Es kann ein Thema eines Betreuers mit einer UUID gefunden werden")
    void test_11() {
        Betreuer b = Betreuer.of(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "",
                "jens"
        );

        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(b));

        BetreuerService service = new BetreuerService(repository);

        service.addThema(b.getId(), "test", "test", Set.of("A","B"), Set.of("link"));
    }

    @Test
    @DisplayName("Es kann nach Betreuern gesucht werden")
    void test_12() {
        Betreuer b = Betreuer.of(
                "Jens",
                "Bendisposto",
                "jens@hhu.de",
                "49123",
                "",
                "jens"
        );

        Betreuer b2 = Betreuer.of(
                "EvilJens",
                "EvilBendisposto",
                "jens@uuh.de",
                "67",
                "",
                "eviljens"
        );

        when(repository.findAll()).thenReturn(List.of(b,b2));

        BetreuerService service = new BetreuerService(repository);

        List<Betreuer> res = service.searchBetreuer("JENS");

        assertThat(res).containsExactlyInAnyOrder(b,b2);
    }

    @Test
    @DisplayName("Wenn die suche nach Betreuern keine Ergebnisse findet, wird eine leere Liste zurückgegeben")
    void test_13() {
        when(repository.findAll()).thenReturn(db);

        BetreuerService service = new BetreuerService(repository);

        List<Betreuer> res = service.searchBetreuer("NICHT_VORHANDEN");

        assertThat(res).isEmpty();
    }

    @Test
    @DisplayName("Wird nach einem leeren String gesucht, wird nichts gefunden")
    void test_14() {
        when(repository.findAll()).thenReturn(db);

        BetreuerService service = new BetreuerService(repository);

        List<Betreuer> res = service.searchBetreuer("    ");

        assertThat(res).isEmpty();
    }

    @Test
    @DisplayName("Es kann nach Themen gesucht werden")
    void test_15() {
        when(repository.findAll()).thenReturn(db);
        db.getFirst().addThema("Compiler","abc", List.of("Propra"), Set.of("link"));
        db.getLast().addThema("Progra","abc", List.of("RA"), Set.of("link"));

        BetreuerService service = new BetreuerService(repository);

        List<ThemaView> res = service.searchThemen("propra");

        assertThat(res.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Es kann ein Thema mithilfe von einer UUID gefunden werden")
    void test_16() {
        Betreuer b = db.getFirst();
        b.addThema("Compiler","abc", List.of("Propra"), Set.of("links"));

        UUID fachId = b.getThemen().getFirst().fachId();

        when(repository.findAll()).thenReturn(List.of(b));

        BetreuerService service = new BetreuerService(repository);

        ThemaView thema = service.themaById(fachId);

        assertThat(thema).isNotNull();
        assertThat(thema.name()).isEqualTo("Compiler");
        assertThat(thema.requirements()).containsExactly("Propra");
    }

}