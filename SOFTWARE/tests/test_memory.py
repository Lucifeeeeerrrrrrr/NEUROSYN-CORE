import numpy as np
from memory.adaptive_decay import apply_adaptive_decay
from memory.synaptic_pruning import prune_synapses
from memory.cold_storage_manager import ColdStorageManager

# Test data
memory = np.random.rand(100)
relevance = np.random.rand(100)
weights = np.random.rand(10, 10)

# Test Adaptive Decay
def test_adaptive_decay():
    decayed = apply_adaptive_decay(memory, relevance, 0.1)
    assert decayed.shape == memory.shape
    assert not np.array_equal(decayed, memory)

# Test Synaptic Pruning
def test_synaptic_pruning():
    pruned = prune_synapses(weights, 0.5)
    assert pruned.shape == weights.shape
    assert np.sum(pruned != 0) <= np.sum(weights != 0)

# Test Cold Storage Manager
def test_cold_storage():
    csm = ColdStorageManager(b'/tmp/test_config.json')
    test_data = {'test': 123}
    csm.store(b'test_key', test_data)
    retrieved = csm.retrieve(b'test_key')
    assert retrieved is not None

if __name__ == '__main__':
    test_adaptive_decay()
    test_synaptic_pruning()
    test_cold_storage()
    print("All memory tests passed!")