package com.jorken.web;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StringConverterTest {

    @Test
    @DisplayName("Ein String der durch '#' getrennt wurde kann in ein Set umgewandelt werden")
    void test_1() {
        String s = "THEMA1#THEMA2#   THEMA3";
        Set<String> set = StringConverter.stringToSet(s, "#");
        assertThat(set).isEqualTo(Set.of("THEMA1","THEMA2","THEMA3"));
    }

    @Test
    @DisplayName("Ist der String leer, so ist auch das Set leer")
    void test_2() {
        String s = "";
        Set<String> set = StringConverter.stringToSet(s, "#");
        assertThat(set.isEmpty());
    }

    @Test
    @DisplayName("Jeder String kann nur genau ein mal im Set enthalten sein")
    void test_3() {
        String s = "ABC,ABC,AAB";
        Set<String> set = StringConverter.stringToSet(s, ",");
        assertThat(set).isEqualTo(Set.of("ABC", "AAB"));
    }

    @Test
    @DisplayName("Ein Set kann in einen String getrennt durch einen delimiter umgewandelt werden")
    void test_4() {
        Set<String> set = Set.of("ABC", "CBA", "123");
        String s = StringConverter.setToString(set, "#");
        assertThat(s).contains(Set.of("ABC", "CBA", "123", "#"));
    }
}