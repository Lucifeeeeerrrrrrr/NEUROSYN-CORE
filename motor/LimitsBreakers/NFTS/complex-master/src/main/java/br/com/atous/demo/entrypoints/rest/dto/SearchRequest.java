package br.com.atous.demo.entrypoints.rest.dto;

// Usamos record para DTOs imutáveis e concisos
public record SearchRequest(int searchSpaceSize, int targetIndex) {
} 