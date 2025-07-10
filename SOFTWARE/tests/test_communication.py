import numpy as np
from communication.p2p_protocol import P2PProtocol
from communication.hcode_serializer import serialize_to_hcode
from communication.fallback_strategies import select_fallback_strategy

# Test data
error_rates = np.random.rand(5)
test_data = {'test': 123}

# Test P2P Protocol
def test_p2p_protocol():
    p2p = P2PProtocol()
    p2p.add_node(b'node1', {'latency': 0.1})
    peer = p2p.select_peer()
    assert peer in [b'node1']

# Test HCODE Serializer
def test_hcode_serializer():
    serialized = serialize_to_hcode(test_data)
    assert isinstance(serialized, bytes)
    assert len(serialized) > 0

# Test Fallback Strategies
def test_fallback_strategies():
    strategy = select_fallback_strategy(error_rates)
    assert strategy in [0, 1, 2]

if __name__ == '__main__':
    test_p2p_protocol()
    test_hcode_serializer()
    test_fallback_strategies()
    print("All communication tests passed!")