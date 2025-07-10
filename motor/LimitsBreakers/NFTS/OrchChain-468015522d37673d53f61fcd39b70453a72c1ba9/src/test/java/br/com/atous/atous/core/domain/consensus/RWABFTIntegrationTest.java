package br.com.atous.atous.core.domain.consensus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class RWABFTIntegrationTest {
    private static final String TEST_PROPOSAL = "test-proposal";
    private static final List<String> TEST_VALIDATORS = Arrays.asList("node1", "node2", "node3", "node4", "node5");
    private static final double QUORUM_THRESHOLD = 0.66;
    private static final double MIN_REPUTATION = 0.5;
    private SimpleReputationService reputationService;
    private RWABFTConsensus consensus;

    @BeforeEach
    void setUp() {
        reputationService = new SimpleReputationService(0.7, MIN_REPUTATION, 0.1, 0.05);
        consensus = new RWABFTConsensus(reputationService, QUORUM_THRESHOLD, MIN_REPUTATION);
        reputationService.setReputation("node1", 0.8);
        reputationService.setReputation("node2", 0.9);
        reputationService.setReputation("node3", 0.7);
        reputationService.setReputation("node4", 0.6);
        reputationService.setReputation("node5", 0.4); // Below minimum
    }

    @Test
    void reachConsensus_ShouldSucceed_WithHonestMajority() throws Exception {
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (validatorId, proposal) -> true;
        ConsensusResult result = consensus.reachConsensus(TEST_PROPOSAL, TEST_VALIDATORS, voteFunction).get(5, TimeUnit.SECONDS);
        assertTrue(result.isCommitted());
        assertEquals(3.0, result.getTotalVotes(), 0.001); // node5 excluded
        assertEquals(3.0, result.getAgreeVotes(), 0.001);
    }

    @Test
    void reachConsensus_ShouldUpdateReputations_BasedOnVoting() throws Exception {
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = new RWABFTConsensus.ValidatorVoteFunction<String>() {
            @Override
            public boolean requestVote(String id, String proposal) {
                return "node1".equals(id) || "node2".equals(id); // Only node1 and node2 agree
            }
        };
        ConsensusResult result = consensus.reachConsensus(TEST_PROPOSAL, TEST_VALIDATORS, voteFunction).get(5, TimeUnit.SECONDS);
        assertFalse(result.isCommitted());
        // node1 and node2 should have increased reputation (agreed with each other)
        assertTrue(reputationService.getReputation("node1") > 0.8);
        assertTrue(reputationService.getReputation("node2") > 0.9);
    }

    @Test
    void reachConsensus_ShouldHandleNetworkPartitions() throws Exception {
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (id, proposal) -> {
            if ("node1".equals(id) || "node2".equals(id)) {
                return true;
            }
            throw new RuntimeException("Network partition");
        };
        ConsensusResult result = consensus.reachConsensus(TEST_PROPOSAL, TEST_VALIDATORS, voteFunction).get(5, TimeUnit.SECONDS);
        assertTrue(result.isCommitted());
        assertEquals(1.7, result.getTotalVotes(), 0.001); // 0.8 + 0.9
        assertEquals(1.7, result.getAgreeVotes(), 0.001);
    }

    @Test
    void reachConsensus_ShouldHandleConcurrentVoting() throws Exception {
        RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (id, proposal) -> {
            try {
                int delay = 50 * (TEST_VALIDATORS.indexOf(id) + 1);
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted", e);
            }
            return true;
        };
        ConsensusResult result = consensus.reachConsensus(TEST_PROPOSAL, TEST_VALIDATORS, voteFunction).get(5, TimeUnit.SECONDS);
        assertTrue(result.isCommitted());
        assertEquals(3.0, result.getTotalVotes(), 0.001); // node5 excluded
        assertEquals(3.0, result.getAgreeVotes(), 0.001);
    }
}
