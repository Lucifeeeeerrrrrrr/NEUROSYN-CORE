package br.com.atous.atous.core.domain.consensus;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Implementation of the Reputation-Weighted Asynchronous Byzantine Fault Tolerant (RWA-BFT) consensus.
 * This implementation weights votes by the reputation of the voting peers.
 */
public class RWABFTConsensus {
    private final ReputationService reputationService;
    private final double quorumThreshold;
    private final ExecutorService executorService;
    private final double minimumReputation;

    public RWABFTConsensus(ReputationService reputationService, double quorumThreshold, double minimumReputation) {
        if (reputationService == null) {
            throw new IllegalArgumentException("Reputation service cannot be null");
        }
        if (quorumThreshold <= 0.5 || quorumThreshold > 1.0) {
            throw new IllegalArgumentException("Quorum threshold must be > 0.5 and <= 1.0");
        }
        if (minimumReputation < 0 || minimumReputation > 1.0) {
            throw new IllegalArgumentException("Minimum reputation must be between 0 and 1.0");
        }
        this.reputationService = reputationService;
        this.quorumThreshold = quorumThreshold;
        this.minimumReputation = minimumReputation;
        this.executorService = Executors.newCachedThreadPool();
    }

    /**
     * Reaches consensus on a proposal among a set of validators.
     *
     * @param proposal The proposal to reach consensus on
     * @param validators The list of validator peer IDs
     * @param validatorVoteFunction The function that asks a validator to vote on the proposal
     * @return A CompletableFuture that completes with the consensus result
     */
    public <T> CompletableFuture<ConsensusResult> reachConsensus(
            T proposal,
            List<String> validators,
            ValidatorVoteFunction<T> validatorVoteFunction) {
        Objects.requireNonNull(proposal, "Proposal cannot be null");
        Objects.requireNonNull(validators, "Validators list cannot be null");
        Objects.requireNonNull(validatorVoteFunction, "Validator vote function cannot be null");
        if (validators.isEmpty()) {
            throw new IllegalArgumentException("Validators list cannot be empty");
        }
        // Filter validators by minimum reputation
        List<String> eligibleValidators = validators.stream()
                .filter(validator -> reputationService.getReputation(validator) >= minimumReputation)
                .collect(Collectors.toList());
        if (eligibleValidators.isEmpty()) {
            return CompletableFuture.completedFuture(
                new ConsensusResult(false, 0, 0, quorumThreshold)
            );
        }
        // Collect votes asynchronously
        List<CompletableFuture<Vote>> voteFutures = eligibleValidators.stream()
                .map(validator -> CompletableFuture.supplyAsync(
                    () -> {
                        try {
                            boolean agrees = validatorVoteFunction.requestVote(validator, proposal);
                            double weight = reputationService.getReputation(validator);
                            return new Vote(validator, agrees, weight);
                        } catch (Exception e) {
                            // In case of error, consider it as a disagreement with minimal weight
                            return new Vote(validator, false, 0.0);
                        }
                    },
                    executorService
                ))
                .collect(Collectors.toList());
        // Combine all votes
        return CompletableFuture.allOf(voteFutures.toArray(new CompletableFuture[0]))
                .thenApply(dummy -> {
                    double totalWeight = 0.0;
                    double agreeWeight = 0.0;
                    for (CompletableFuture<Vote> future : voteFutures) {
                        try {
                            Vote vote = future.get();
                            totalWeight += vote.getWeight();
                            if (vote.isAgrees()) {
                                agreeWeight += vote.getWeight();
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    boolean committed = totalWeight > 0 && (agreeWeight / totalWeight) >= quorumThreshold;
                    boolean majorityVote = agreeWeight >= (totalWeight / 2.0);
                    // Update reputations based on voting behavior
                    for (CompletableFuture<Vote> future : voteFutures) {
                        try {
                            Vote vote = future.get();
                            String validatorId = vote.getVoterId();
                            reputationService.updateReputation(
                                validatorId,
                                vote.isAgrees() == majorityVote
                            );
                        } catch (InterruptedException | ExecutionException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    return new ConsensusResult(committed, totalWeight, agreeWeight, quorumThreshold);
                });
    }

    public void shutdown() {
        executorService.shutdown();
    }

    @FunctionalInterface
    public interface ValidatorVoteFunction<T> {
        boolean requestVote(String validatorId, T proposal) throws Exception;
    }
}
