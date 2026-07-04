package com.jorken.domain.Betreuer;

import com.jorken.annotations.View;

import java.util.Set;
import java.util.UUID;
@View
public record ThemaView(UUID fachId, String name, String markdown, Set<String> requirements, Set<String> links) {
}
