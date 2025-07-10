package br.com.atous.demo.application.usecase;

import br.com.atous.demo.application.port.in.QuantumSearchUseCase;
import br.com.atous.demo.domain.model.GroverResult;
import br.com.atous.demo.domain.port.out.AmplitudeDataStructure;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class ClassicalGroverSearchService implements QuantumSearchUseCase {

    private final ObjectProvider<AmplitudeDataStructure> amplitudeProvider;

    // Usamos ObjectProvider para obter uma nova instância de FenwickTreeAmplitude (escopo prototype) a cada chamada.
    public ClassicalGroverSearchService(ObjectProvider<AmplitudeDataStructure> amplitudeProvider) {
        this.amplitudeProvider = amplitudeProvider;
    }

    @Override
    public GroverResult executeSearch(int searchSpaceSize, int targetIndex) {
        validateInputs(searchSpaceSize, targetIndex);
        
        long startTime = System.nanoTime();

        AmplitudeDataStructure amplitudes = amplitudeProvider.getObject();
        amplitudes.initialize(searchSpaceSize);

        // Cálculo otimizado de iterações com tratamento de casos especiais
        int iterations = calculateOptimalIterations(searchSpaceSize);

        for (int i = 0; i < iterations; i++) {
            amplitudes.applyOracle(targetIndex);
            amplitudes.applyDiffusion();
        }

        int foundIndex = amplitudes.findMaxAmplitudeIndex();
        long endTime = System.nanoTime();
        long durationMillis = (endTime - startTime) / 1_000_000;

        return new GroverResult(
            foundIndex,
            targetIndex,
            foundIndex == targetIndex,
            durationMillis,
            searchSpaceSize,
            iterations
        );
    }
    
    private void validateInputs(int searchSpaceSize, int targetIndex) {
        if (searchSpaceSize <= 0) {
            throw new IllegalArgumentException("Search space size must be positive, got: " + searchSpaceSize);
        }
        
        if (targetIndex < 0) {
            throw new IllegalArgumentException("Target index must be non-negative, got: " + targetIndex);
        }
        
        if (targetIndex >= searchSpaceSize) {
            throw new IllegalArgumentException(
                String.format("Target index (%d) must be less than search space size (%d)", 
                    targetIndex, searchSpaceSize)
            );
        }
    }
    
    /**
     * Calcula o número ótimo de iterações para o algoritmo de Grover.
     * 
     * Teoria: O número ótimo de iterações é aproximadamente π/4 * √N
     * 
     * Para casos especiais:
     * - N = 1: 0 iterações (resultado trivial)
     * - N = 2,3: 1 iteração mínima
     * - N ≥ 4: fórmula padrão π/4 * √N
     */
    private int calculateOptimalIterations(int searchSpaceSize) {
        if (searchSpaceSize == 1) {
            return 0; // Caso trivial: apenas um elemento
        }
        
        if (searchSpaceSize <= 3) {
            return 1; // Para espaços muito pequenos, pelo menos 1 iteração
        }
        
        // Fórmula padrão de Grover: π/4 * √N
        int calculated = (int) Math.floor(Math.PI / 4.0 * Math.sqrt(searchSpaceSize));
        
        // Garantia de pelo menos 1 iteração para evitar casos degenerados
        return Math.max(1, calculated);
    }
} 