package br.com.atous.atous.core.domain.blockchain;

public class NodeStateVector {
    private int stateMask = 0;

    public void setState(int bit, boolean value) {
        if (value) {
            stateMask |= (1 << bit);
        } else {
            stateMask &= ~(1 << bit);
        }
    }

    public boolean isStateSet(int bit) {
        return (stateMask & (1 << bit)) != 0;
    }
}
