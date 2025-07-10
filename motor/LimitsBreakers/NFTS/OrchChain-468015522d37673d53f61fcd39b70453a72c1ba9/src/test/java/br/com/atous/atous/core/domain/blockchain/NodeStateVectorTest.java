package br.com.atous.atous.core.domain.blockchain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Atomic test: NodeStateVector bitmask logic
class NodeStateVectorTest {
    @Test
    void testSetAndCheckState() {
        NodeStateVector vector = new NodeStateVector();
        vector.setState(3, true);
        assertTrue(vector.isStateSet(3), "State 3 should be set");
        vector.setState(3, false);
        assertFalse(vector.isStateSet(3), "State 3 should be unset");
    }
}
