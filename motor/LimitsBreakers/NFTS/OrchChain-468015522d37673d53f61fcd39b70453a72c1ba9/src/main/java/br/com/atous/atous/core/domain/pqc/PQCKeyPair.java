package br.com.atous.atous.core.domain.pqc;

import java.util.Objects;

public class PQCKeyPair {
    private final byte[] publicKey;
    private final byte[] privateKey;

    public PQCKeyPair(byte[] publicKey, byte[] privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PQCKeyPair pqcKeyPair = (PQCKeyPair) o;
        return Objects.equals(publicKey, pqcKeyPair.publicKey) &&
               Objects.equals(privateKey, pqcKeyPair.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKey, privateKey);
    }
}
