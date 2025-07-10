package br.com.atous.atous.core.domain.consensus.example;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import br.com.atous.atous.core.domain.consensus.*;

/**
 * Example demonstrating how to use the RWABFTConsensus in a real application.
 */
public class ConsensusExample {
    public static void main(String[] args) {
        // 1. Create a reputation service
        SimpleReputationService reputationService = new SimpleReputationService();
        // 2. Set up some initial reputations for our validators
        reputationService.setReputation("node1", 0.9);  // High reputation
        reputationService.setReputation("node2", 0.8);  // High reputation
        reputationService.setReputation("node3", 0.7);  // Medium reputation
        reputationService.setReputation("node4", 0.4);  // Below minimum (0.5), will be excluded
        // 3. Create the consensus instance with a quorum threshold of 67%
        double quorumThreshold = 0.67;
        double minimumReputation = 0.5;
        RWABFTConsensus consensus = new RWABFTConsensus(reputationService, quorumThreshold, minimumReputation);
        try {
            // 4. Define our proposal (could be a block, transaction, or any data)
            String proposal = "Proposed block #123 with transactions [tx1, tx2, tx3]";
            // 5. List of validator node IDs
            List<String> validators = List.of("node1", "node2", "node3", "node4");
            // 6. Define how to request a vote from each validator
            RWABFTConsensus.ValidatorVoteFunction<String> voteFunction = (validatorId, prop) -> {
                System.out.println("Requesting vote from " + validatorId + " for proposal: " + prop);
                switch (validatorId) {
                    case "node1":
                    case "node2":
                        return true;  // Always agree
                    case "node3":
                        return false; // Disagree
                    case "node4":
                        throw new IllegalStateException("Should not be called for node4");
                    default:
                        return false;
                }
            };
            // 7. Reach consensus
            System.out.println("Starting consensus process...");
            CompletableFuture<ConsensusResult> future = consensus.reachConsensus(
                proposal,
                validators,
                voteFunction
            );
            // 8. Wait for the result with a timeout
            ConsensusResult result = future.get(10, TimeUnit.SECONDS);
            // 9. Process the result
            System.out.println("\nConsensus Result:");
            System.out.println("Committed: " + result.isCommitted());
            System.out.println("Agree Votes: " + result.getAgreeVotes());
            System.out.println("Total Votes: " + result.getTotalVotes());
            System.out.println("Agreement Ratio: " + (result.getAgreementRatio() * 100) + "%");
            System.out.println("Quorum Threshold: " + (result.getQuorumThreshold() * 100) + "%");
            if (result.isCommitted()) {
                System.out.println("✅ Consensus reached! Proposal is committed.");
                // Update reputations based on voting behavior
                reputationService.updateReputation("node1", true);
                reputationService.updateReputation("node2", true);
                reputationService.updateReputation("node3", false);
                System.out.println("\nUpdated Reputations:");
                System.out.println("node1: " + reputationService.getReputation("node1"));
                System.out.println("node2: " + reputationService.getReputation("node2"));
                System.out.println("node3: " + reputationService.getReputation("node3"));
                System.out.println("node4: " + reputationService.getReputation("node4"));
            } else {
                System.out.println("❌ Consensus not reached. Proposal rejected.");
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("Error during consensus: " + e.getMessage());
            e.printStackTrace();
        } finally {
            consensus.shutdown();
        }
    }
}
