package br.com.atous.atous.core.domain.consensus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RWABFTConsensusTest {
    @Mock
    private ReputationService reputationService;
    private RWABFTConsensus consensus;
    private static final double QUORUM_THRESHOLD = 0.66;
    private static final double MIN_REPUTATION = 0.5;
    private static final String TEST_PROPOSAL = "test-proposal";
    private static final List<String> TEST_VALIDATORS = Arrays.asList("node1", "node2", "node3");

    @BeforeEach
    void setUp() {
        consensus = new RWABFTConsensus(reputationService, QUORUM_THRESHOLD, MIN_REPUTATION);
    }

    @Test
    void constructor_ShouldThrowException_WhenReputationServiceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new RWABFTConsensus(null, 0.7, 0.5));
    }

    @Test
    void constructor_ShouldThrowException_WhenQuorumThresholdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new RWABFTConsensus(reputationService, 0.4, 0.5));
        assertThrows(IllegalArgumentException.class, () -> new RWABFTConsensus(reputationService, 1.1, 0.5));
    }

    @Test
    void constructor_ShouldThrowException_WhenMinimumReputationIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new RWABFTConsensus(reputationService, 0.7, -0.1));
        assertThrows(IllegalArgumentException.class, () -> new RWABFTConsensus(reputationService, 0.7, 1.1));
    }

    @Test
    void reachConsensus_ShouldReturnFalse_WhenNoValidators() {
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (validator, proposal) -> true;
        assertThrows(IllegalArgumentException.class, () -> consensus.reachConsensus(TEST_PROPOSAL, Collections.emptyList(), voteFunction));
    }

    @Test
    void reachConsensus_ShouldReturnFalse_WhenNoEligibleValidators() throws Exception {
        when(reputationService.getReputation(anyString())).thenReturn(0.4); // Below minimum
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (validator, proposal) -> true;
        ConsensusResult result = consensus.reachConsensus(TEST_PROPOSAL, TEST_VALIDATORS, voteFunction).get();
        assertFalse(result.isCommitted());
        assertEquals(0.0, result.getTotalVotes());
        assertEquals(0.0, result.getAgreeVotes());
    }

    @Test
    void reachConsensus_ShouldReturnTrue_WhenQuorumReached() throws Exception {
        when(reputationService.getReputation("node1")).thenReturn(0.8);
        when(reputationService.getReputation("node2")).thenReturn(0.9);
        when(reputationService.getReputation("node3")).thenReturn(0.7);
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (validator, proposal) -> true;
        ConsensusResult result = consensus.reachConsensus(TEST_PROPOSAL, TEST_VALIDATORS, voteFunction).get();
        assertTrue(result.isCommitted());
        assertEquals(2.4, result.getTotalVotes(), 0.001); // 0.8 + 0.9 + 0.7 = 2.4
        assertEquals(2.4, result.getAgreeVotes(), 0.001); // All agree
    }

    @Test
    void reachConsensus_ShouldReturnFalse_WhenQuorumNotReached() throws Exception {
        when(reputationService.getReputation("node1")).thenReturn(0.8);
        when(reputationService.getReputation("node2")).thenReturn(0.9);
        when(reputationService.getReputation("node3")).thenReturn(0.7);
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (id, proposal) -> "node1".equals(id);
        ConsensusResult result = consensus.reachConsensus(TEST_PROPOSAL, TEST_VALIDATORS, voteFunction).get();
        assertFalse(result.isCommitted());
        assertEquals(2.4, result.getTotalVotes(), 0.001);
        assertEquals(0.8, result.getAgreeVotes(), 0.001); // Only node1 agrees
    }

    @Test
    void reachConsensus_ShouldHandleValidatorErrors() throws Exception {
        when(reputationService.getReputation("node1")).thenReturn(0.8);
        when(reputationService.getReputation("node2")).thenReturn(0.9);
        when(reputationService.getReputation("node3")).thenReturn(0.7);
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (id, proposal) -> {
            if ("node2".equals(id)) {
                throw new RuntimeException("Node error");
            }
            return true;
        };
        ConsensusResult result = consensus.reachConsensus(TEST_PROPOSAL, TEST_VALIDATORS, voteFunction).get();
        double expectedTotal = 0.8 + 0.7; // node1 + node3
        double expectedAgree = 0.8 + 0.7; // both agree
        double requiredAgree = expectedTotal * QUORUM_THRESHOLD;
        assertEquals(expectedTotal, result.getTotalVotes(), 0.001);
        assertEquals(expectedAgree, result.getAgreeVotes(), 0.001);
        assertEquals(expectedAgree >= requiredAgree, result.isCommitted());
    }

    @Test
    void reachConsensus_ShouldHandleConcurrentVotes() throws Exception {
        when(reputationService.getReputation(anyString())).thenReturn(1.0);
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (validator, proposal) -> {
            try {
                Thread.sleep(100); // Simulate network delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return true;
        };
        List<String> manyValidators = Collections.nCopies(10, "node");
        ConsensusResult result = consensus.reachConsensus(TEST_PROPOSAL, manyValidators, voteFunction).get();
        assertTrue(result.isCommitted());
        assertEquals(10.0, result.getTotalVotes(), 0.001);
        assertEquals(10.0, result.getAgreeVotes(), 0.001);
    }
}
