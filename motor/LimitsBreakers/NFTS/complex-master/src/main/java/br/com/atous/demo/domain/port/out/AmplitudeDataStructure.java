package br.com.atous.demo.domain.port.out;

public interface AmplitudeDataStructure {
    void initialize(int size);
    void applyOracle(int targetIndex);
    void applyDiffusion();
    int findMaxAmplitudeIndex();
} 