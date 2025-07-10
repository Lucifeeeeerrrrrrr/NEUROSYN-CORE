package br.com.atous.demo.entrypoints.cli;

import br.com.atous.demo.application.port.in.QuantumSearchUseCase;
import br.com.atous.demo.domain.model.GroverResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // Não executa este runner durante os testes
public class AlgorithmRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AlgorithmRunner.class);
    private final QuantumSearchUseCase searchUseCase;

    public AlgorithmRunner(QuantumSearchUseCase searchUseCase) {
        this.searchUseCase = searchUseCase;
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("---EXECUTING ALGORITHM DEMO ON STARTUP---");
        
        int searchSpaceSize = 1024;
        int targetElement = 777;

        LOG.info("Search Space Size (N): {}", searchSpaceSize);
        LOG.info("Target Element: {}", targetElement);

        GroverResult result = searchUseCase.executeSearch(searchSpaceSize, targetElement);

        LOG.info("---DEMO RESULTS---");
        LOG.info("Execution Time: {} ms", result.executionTimeMillis());
        LOG.info("Iterations Performed: {}", result.iterations());
        LOG.info("Target Index: {}", result.targetIndex());
        LOG.info("Found Index: {}", result.foundIndex());
        LOG.info("Success: {}", result.success());
        LOG.info("--------------------");
    }
} 