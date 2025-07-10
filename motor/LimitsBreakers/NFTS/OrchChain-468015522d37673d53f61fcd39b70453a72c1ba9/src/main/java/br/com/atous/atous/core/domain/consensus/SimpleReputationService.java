package br.com.atous.atous.core.domain.consensus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple in-memory implementation of the ReputationService for testing purposes.
 */
public class SimpleReputationService implements ReputationService {
    private final Map<String, Double> reputations = new ConcurrentHashMap<>();
    private final double defaultReputation;
    private final double minimumReputation;
    private final double positiveDelta;
    private final double negativeDelta;

    /**
     * Creates a new SimpleReputationService with default values.
     */
    public SimpleReputationService() {
        this(0.7, 0.5, 0.1, 0.05);
    }

    /**
     * Creates a new SimpleReputationService with custom parameters.
     */
    public SimpleReputationService(double defaultReputation, double minimumReputation, double positiveDelta, double negativeDelta) {
        this.defaultReputation = defaultReputation;
        this.minimumReputation = minimumReputation;
        this.positiveDelta = positiveDelta;
        this.negativeDelta = negativeDelta;
    }

    @Override
    public double getReputation(String nodeId) {
        return reputations.getOrDefault(nodeId, defaultReputation);
    }

    @Override
    public void setReputation(String nodeId, double reputation) {
        reputations.put(nodeId, reputation);
    }

    @Override
    public void updateReputation(String nodeId, boolean positive) {
        double current = getReputation(nodeId);
        double delta = positive ? positiveDelta : -negativeDelta;
        double updated = Math.max(0.0, Math.min(1.0, current + delta));
        reputations.put(nodeId, updated);
    }

    @Override
    public double getMinimumReputation() {
        return minimumReputation;
    }
}
