package br.com.atous.atous.core.domain.consensus;

/**
 * Service for managing peer reputations in the RWA-BFT consensus.
 */
public interface ReputationService {
    double getReputation(String nodeId);
    void setReputation(String nodeId, double reputation);
    void updateReputation(String nodeId, boolean positive);
    double getMinimumReputation();
}
