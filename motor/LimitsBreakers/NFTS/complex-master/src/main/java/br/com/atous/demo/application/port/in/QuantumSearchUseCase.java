package br.com.atous.demo.application.port.in;

import br.com.atous.demo.domain.model.GroverResult;

public interface QuantumSearchUseCase {
    GroverResult executeSearch(int searchSpaceSize, int targetIndex);
} 