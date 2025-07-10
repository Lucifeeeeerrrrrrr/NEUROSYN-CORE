package br.com.atous.demo.entrypoints.rest;

import br.com.atous.demo.application.port.in.QuantumSearchUseCase;
import br.com.atous.demo.domain.model.GroverResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuantumSearchUseCase searchUseCase;

    @Test
    void whenPostValidSearchRequest_thenReturnsSuccess() throws Exception {
        int searchSpace = 100;
        int target = 42;
        GroverResult mockResult = new GroverResult(target, target, true, 5, searchSpace, 7);

        when(searchUseCase.executeSearch(anyInt(), anyInt())).thenReturn(mockResult);

        String requestJson = "{\"searchSpaceSize\": 100, \"targetIndex\": 42}";

        mockMvc.perform(post("/api/v1/search/execute")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Search successful! Found target at index 42"))
               .andExpect(jsonPath("$.result.foundIndex").value(target));
    }

    @Test
    void whenPostInvalidSearchSpaceSize_thenReturnsBadRequest() throws Exception {
        String requestJson = "{\"searchSpaceSize\": 0, \"targetIndex\": 5}";

        mockMvc.perform(post("/api/v1/search/execute")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestJson))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Search space size must be positive, got: 0"));
    }

    @Test
    void whenPostInvalidTargetIndex_thenReturnsBadRequest() throws Exception {
        String requestJson = "{\"searchSpaceSize\": 10, \"targetIndex\": 10}";

        mockMvc.perform(post("/api/v1/search/execute")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestJson))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Target index (10) must be less than search space size (10)"));
    }

    @Test
    void whenPostNegativeTargetIndex_thenReturnsBadRequest() throws Exception {
        String requestJson = "{\"searchSpaceSize\": 10, \"targetIndex\": -1}";

        mockMvc.perform(post("/api/v1/search/execute")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestJson))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Target index must be non-negative, got: -1"));
    }

    @Test
    void whenSearchFails_thenReturnsFailureMessage() throws Exception {
        int searchSpace = 100;
        int target = 42;
        int foundIndex = 15;
        GroverResult mockResult = new GroverResult(foundIndex, target, false, 10, searchSpace, 7);

        when(searchUseCase.executeSearch(anyInt(), anyInt())).thenReturn(mockResult);

        String requestJson = "{\"searchSpaceSize\": 100, \"targetIndex\": 42}";

        mockMvc.perform(post("/api/v1/search/execute")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Search completed but target not found. Found index: 15"))
               .andExpect(jsonPath("$.result.foundIndex").value(foundIndex))
               .andExpect(jsonPath("$.result.success").value(false));
    }
} 