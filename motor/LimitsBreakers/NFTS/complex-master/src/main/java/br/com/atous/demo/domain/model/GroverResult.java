package br.com.atous.demo.domain.model;

public record GroverResult(
    int foundIndex,
    int targetIndex,
    boolean success,
    long executionTimeMillis,
    int searchSpaceSize,
    int iterations
) {} 