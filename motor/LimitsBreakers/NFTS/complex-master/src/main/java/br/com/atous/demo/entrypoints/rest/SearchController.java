package br.com.atous.demo.entrypoints.rest;

import br.com.atous.demo.application.port.in.QuantumSearchUseCase;
import br.com.atous.demo.domain.model.GroverResult;
import br.com.atous.demo.entrypoints.rest.dto.SearchRequest;
import br.com.atous.demo.entrypoints.rest.dto.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final QuantumSearchUseCase searchUseCase;

    public SearchController(QuantumSearchUseCase searchUseCase) {
        this.searchUseCase = searchUseCase;
    }

    @PostMapping("/execute")
    public ResponseEntity<SearchResponse> executeGroverSearch(@RequestBody SearchRequest request) {
        // Validações de entrada aprimoradas
        String validationError = validateRequest(request);
        if (validationError != null) {
            return ResponseEntity.badRequest().body(new SearchResponse(validationError, null));
        }
        
        try {
            GroverResult result = searchUseCase.executeSearch(request.searchSpaceSize(), request.targetIndex());
            
            String message = result.success() ? 
                "Search successful! Found target at index " + result.foundIndex() : 
                "Search completed but target not found. Found index: " + result.foundIndex();
                
            return ResponseEntity.ok(new SearchResponse(message, result));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new SearchResponse("Internal error: " + e.getMessage(), null));
        }
    }
    
    private String validateRequest(SearchRequest request) {
        if (request.searchSpaceSize() <= 0) {
            return "Search space size must be positive, got: " + request.searchSpaceSize();
        }
        
        if (request.searchSpaceSize() > 1_000_000) {
            return "Search space size too large (max: 1,000,000), got: " + request.searchSpaceSize();
        }
        
        if (request.targetIndex() < 0) {
            return "Target index must be non-negative, got: " + request.targetIndex();
        }
        
        if (request.targetIndex() >= request.searchSpaceSize()) {
            return String.format("Target index (%d) must be less than search space size (%d)", 
                request.targetIndex(), request.searchSpaceSize());
        }
        
        return null; // Validação passou
    }
} 