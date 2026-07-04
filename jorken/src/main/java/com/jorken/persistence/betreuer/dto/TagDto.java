package com.jorken.persistence.betreuer.dto;

import org.springframework.data.relational.core.mapping.Table;

@Table("betreuer_tags")
public record TagDto(String tag) {
}
