package com.jorken.persistence.betreuer.dto;

import org.springframework.data.relational.core.mapping.Table;

@Table("betreuer_themen_links")
public record ThemaLinkDto(String link) {
}
