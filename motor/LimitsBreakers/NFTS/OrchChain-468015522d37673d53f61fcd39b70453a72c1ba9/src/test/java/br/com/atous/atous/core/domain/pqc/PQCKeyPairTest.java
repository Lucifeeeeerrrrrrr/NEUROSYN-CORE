package br.com.atous.atous.core.domain.pqc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Atomic test: PQCKeyPair cryptographic validation
class PQCKeyPairTest {
    @Test
    void testKeyPairEquality() {
        byte[] pub = {1,2,3};
        byte[] priv = {4,5,6};
        PQCKeyPair k1 = new PQCKeyPair(pub, priv);
        PQCKeyPair k2 = new PQCKeyPair(pub, priv);
        assertEquals(k1, k2, "Key pairs with same data should be equal");
    }

    @Test
    void testKeyPairInequality() {
        PQCKeyPair k1 = new PQCKeyPair(new byte[]{1}, new byte[]{2});
        PQCKeyPair k2 = new PQCKeyPair(new byte[]{9}, new byte[]{8});
        assertNotEquals(k1, k2, "Key pairs with different data should not be equal");
    }
}
