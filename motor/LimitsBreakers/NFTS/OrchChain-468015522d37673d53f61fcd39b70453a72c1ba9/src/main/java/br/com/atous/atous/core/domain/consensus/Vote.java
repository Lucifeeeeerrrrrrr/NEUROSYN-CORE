package br.com.atous.atous.core.domain.consensus;

import java.util.Objects;

/**
 * Represents a vote in the RWA-BFT consensus protocol.
 * Each vote is weighted by the voter's reputation score.
 */
public class Vote {
    private final String voterId;
    private final boolean agrees;
    private final double weight;

    public Vote(String voterId, boolean agrees, double weight) {
        this.voterId = voterId;
        this.agrees = agrees;
        this.weight = weight;
    }

    public String getVoterId() {
        return voterId;
    }

    public boolean isAgrees() {
        return agrees;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return agrees == vote.agrees && 
               Double.compare(vote.weight, weight) == 0 && 
               Objects.equals(voterId, vote.voterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voterId, agrees, weight);
    }

    @Override
    public String toString() {
        return String.format("Vote{voterId='%s', agrees=%s, weight=%.2f}", 
            voterId, agrees, weight);
    }
}
