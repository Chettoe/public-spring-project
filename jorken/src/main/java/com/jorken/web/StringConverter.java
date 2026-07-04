package com.jorken.web;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class StringConverter {

    static Set<String> stringToSet(String string, String delimiter) {
        return Arrays.stream(string.split(delimiter))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    static String setToString(Set<String> set, String delimiter) {
        StringBuilder s = new StringBuilder();
        for(String e : set) {
            s.append(delimiter);
            s.append(e);
        }
        return s.toString();
    }
}