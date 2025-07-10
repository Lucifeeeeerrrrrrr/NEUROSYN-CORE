package br.com.atous.atous.core.domain.consensus;

import java.util.Objects;

/**
 * Represents the result of a consensus round in the RWA-BFT protocol.
 */
public class ConsensusResult {
    private final boolean committed;
    private final double totalVotes;
    private final double agreeVotes;
    private final double quorumThreshold;

    /**
     * Creates a new consensus result.
     *
     * @param committed Whether consensus was reached
     * @param totalVotes Total weighted votes cast
     * @param agreeVotes Total weighted votes in agreement
     * @param quorumThreshold The quorum threshold that was required
     */
    public ConsensusResult(boolean committed, double totalVotes, double agreeVotes, double quorumThreshold) {
        if (totalVotes < 0 || agreeVotes < 0 || agreeVotes > totalVotes) {
            throw new IllegalArgumentException("Invalid vote counts");
        }
        if (quorumThreshold <= 0 || quorumThreshold > 1.0) {
            throw new IllegalArgumentException("Quorum threshold must be between 0 and 1.0");
        }
        this.committed = committed;
        this.totalVotes = totalVotes;
        this.agreeVotes = agreeVotes;
        this.quorumThreshold = quorumThreshold;
    }

    /**
     * @return true if consensus was reached, false otherwise
     */
    public boolean isCommitted() {
        return committed;
    }

    /**
     * @return The total weighted votes cast
     */
    public double getTotalVotes() {
        return totalVotes;
    }

    /**
     * @return The total weighted votes in agreement
     */
    public double getAgreeVotes() {
        return agreeVotes;
    }

    /**
     * @return The quorum threshold that was required
     */
    public double getQuorumThreshold() {
        return quorumThreshold;
    }

    /**
     * @return The agreement ratio (agreeVotes / totalVotes)
     */
    public double getAgreementRatio() {
        return totalVotes > 0 ? agreeVotes / totalVotes : 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsensusResult that = (ConsensusResult) o;
        return committed == that.committed &&
               Double.compare(that.totalVotes, totalVotes) == 0 &&
               Double.compare(that.agreeVotes, agreeVotes) == 0 &&
               Double.compare(that.quorumThreshold, quorumThreshold) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(committed, totalVotes, agreeVotes, quorumThreshold);
    }

    @Override
    public String toString() {
        return String.format(
            "ConsensusResult{committed=%s, agreeVotes=%.2f, totalVotes=%.2f, ratio=%.2f, threshold=%.2f}",
            committed, agreeVotes, totalVotes, getAgreementRatio(), quorumThreshold
        );
    }
}
