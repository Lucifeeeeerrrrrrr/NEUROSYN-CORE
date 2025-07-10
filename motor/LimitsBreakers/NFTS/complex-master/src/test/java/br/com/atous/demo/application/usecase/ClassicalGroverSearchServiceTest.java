package br.com.atous.demo.application.usecase;

import br.com.atous.demo.application.port.in.QuantumSearchUseCase;
import br.com.atous.demo.domain.model.GroverResult;
import br.com.atous.demo.domain.port.out.AmplitudeDataStructure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.ObjectProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClassicalGroverSearchServiceTest {

    @Mock
    private ObjectProvider<AmplitudeDataStructure> amplitudeProvider;
    
    @Mock
    private AmplitudeDataStructure amplitudeDataStructure;
    
    private QuantumSearchUseCase searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(amplitudeProvider.getObject()).thenReturn(amplitudeDataStructure);
        searchService = new ClassicalGroverSearchService(amplitudeProvider);
    }

    @Test
    void whenExecuteSearch_thenShouldInitializeAmplitudes() {
        // Given
        int searchSpaceSize = 16;
        int targetIndex = 5;
        when(amplitudeDataStructure.findMaxAmplitudeIndex()).thenReturn(targetIndex);

        // When
        GroverResult result = searchService.executeSearch(searchSpaceSize, targetIndex);

        // Then
        verify(amplitudeDataStructure).initialize(searchSpaceSize);
        assertNotNull(result);
        assertEquals(searchSpaceSize, result.searchSpaceSize());
        assertEquals(targetIndex, result.targetIndex());
    }

    @Test
    void whenExecuteSearch_thenShouldPerformCorrectNumberOfIterations() {
        // Given
        int searchSpaceSize = 16;
        int targetIndex = 5;
        int expectedIterations = (int) Math.floor(Math.PI / 4.0 * Math.sqrt(searchSpaceSize));
        when(amplitudeDataStructure.findMaxAmplitudeIndex()).thenReturn(targetIndex);

        // When
        GroverResult result = searchService.executeSearch(searchSpaceSize, targetIndex);

        // Then
        verify(amplitudeDataStructure, times(expectedIterations)).applyOracle(targetIndex);
        verify(amplitudeDataStructure, times(expectedIterations)).applyDiffusion();
        assertEquals(expectedIterations, result.iterations());
    }

    @Test
    void whenSearchFindsTarget_thenShouldReturnSuccess() {
        // Given
        int searchSpaceSize = 64;
        int targetIndex = 42;
        when(amplitudeDataStructure.findMaxAmplitudeIndex()).thenReturn(targetIndex);

        // When
        GroverResult result = searchService.executeSearch(searchSpaceSize, targetIndex);

        // Then
        assertTrue(result.success());
        assertEquals(targetIndex, result.foundIndex());
    }

    @Test
    void whenSearchFailsToFindTarget_thenShouldReturnFailure() {
        // Given
        int searchSpaceSize = 64;
        int targetIndex = 42;
        int wrongIndex = 15;
        when(amplitudeDataStructure.findMaxAmplitudeIndex()).thenReturn(wrongIndex);

        // When
        GroverResult result = searchService.executeSearch(searchSpaceSize, targetIndex);

        // Then
        assertFalse(result.success());
        assertEquals(wrongIndex, result.foundIndex());
        assertEquals(targetIndex, result.targetIndex());
    }

    @Test
    void whenExecuteSearch_thenShouldMeasureExecutionTime() {
        // Given
        int searchSpaceSize = 16;
        int targetIndex = 5;
        when(amplitudeDataStructure.findMaxAmplitudeIndex()).thenReturn(targetIndex);

        // When
        GroverResult result = searchService.executeSearch(searchSpaceSize, targetIndex);

        // Then
        assertTrue(result.executionTimeMillis() >= 0);
    }
} 